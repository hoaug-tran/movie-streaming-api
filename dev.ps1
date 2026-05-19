$ErrorActionPreference = "Continue"

# ====================================================================
#  Gio Phim Backend Dev Bootstrap
#    1. Nginx        (HLS / public assets - optional)
#    2. Port 8080    (don dep PID cu)
#    3. Ollama       (start + warm-up model)
#    4. Spring Boot  (profile=local, Xmx=2g)
# ====================================================================

$ollamaModel = "qcwind/qwen3-8b-instruct-Q4-K-M:latest"
$ollamaBase  = "http://localhost:11434"
$nginxDir    = "C:\Users\hoaug\nginx"
$nginxExe    = Join-Path $nginxDir "nginx.exe"
$springPort  = 8080

Write-Host ""
Write-Host "=== Gio Phim Backend Dev Bootstrap ===" -ForegroundColor Cyan
Write-Host ""

# ----------------------------------------------------------------
# 1. NGINX (optional)
# ----------------------------------------------------------------
$nginxAvailable = Test-Path $nginxExe
if ($nginxAvailable) {
    & $nginxExe -p "$nginxDir/" -c "conf/nginx.conf" -t 2>$null | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[Nginx] config test FAILED - check $nginxDir\logs\error.log" -ForegroundColor Red
        exit 1
    }

    $nginxRunning = Get-Process -Name "nginx" -ErrorAction SilentlyContinue
    if ($nginxRunning) {
        Write-Host "[Nginx] dang chay - reload config (graceful)..." -ForegroundColor Yellow
        & $nginxExe -p "$nginxDir/" -c "conf/nginx.conf" -s reload 2>$null | Out-Null
        if ($LASTEXITCODE -ne 0) {
            Write-Host "[Nginx] reload that bai - quit + restart" -ForegroundColor Yellow
            & $nginxExe -p "$nginxDir/" -c "conf/nginx.conf" -s quit 2>$null | Out-Null
            Start-Sleep -Milliseconds 1500
            Get-Process -Name "nginx" -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
            Start-Process -FilePath $nginxExe -ArgumentList @("-p", "$nginxDir/", "-c", "conf/nginx.conf") -WindowStyle Hidden
            Start-Sleep -Seconds 2
        }
    } else {
        Write-Host "[Nginx] khoi dong..." -ForegroundColor Yellow
        Start-Process -FilePath $nginxExe -ArgumentList @("-p", "$nginxDir/", "-c", "conf/nginx.conf") -WindowStyle Hidden
        Start-Sleep -Seconds 2
    }

    if (Get-Process -Name "nginx" -ErrorAction SilentlyContinue) {
        Write-Host "[Nginx] OK - http://localhost" -ForegroundColor Green
    } else {
        Write-Host "[Nginx] khong start duoc - check $nginxDir\logs\error.log" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "[Nginx] khong tim thay $nginxExe - bo qua" -ForegroundColor DarkYellow
}

# ----------------------------------------------------------------
# 2. Don dep port 8080
# ----------------------------------------------------------------
Write-Host ""
Write-Host "[Port] kiem tra $springPort..."
$conns = Get-NetTCPConnection -LocalPort $springPort -State Listen -ErrorAction SilentlyContinue
foreach ($c in $conns) {
    Write-Host "[Port] kill PID $($c.OwningProcess) giu $springPort" -ForegroundColor Yellow
    Stop-Process -Id $c.OwningProcess -Force -ErrorAction SilentlyContinue
}

# ----------------------------------------------------------------
# 3. OLLAMA
# ----------------------------------------------------------------
Write-Host ""
$ollamaProc = Get-Process -Name "ollama" -ErrorAction SilentlyContinue
if (-not $ollamaProc) {
    Write-Host "[Ollama] chua chay - khoi dong 'ollama serve'..." -ForegroundColor Yellow
    Start-Process -FilePath "ollama" -ArgumentList "serve" -WindowStyle Hidden
    Start-Sleep -Seconds 3
} else {
    Write-Host "[Ollama] da chay (PID $($ollamaProc.Id -join ','))" -ForegroundColor Green
}

$ready = $false
for ($i = 0; $i -lt 15; $i++) {
    try {
        Invoke-WebRequest -Uri "$ollamaBase/api/tags" -UseBasicParsing -TimeoutSec 2 | Out-Null
        $ready = $true
        break
    } catch {
        Start-Sleep -Seconds 2
    }
}

if ($ready) {
    Write-Host "[Ollama] san sang." -ForegroundColor Green
    Write-Host "[Ollama] warm-up model $ollamaModel..." -ForegroundColor Yellow
    try {
        $body = @{
            model   = $ollamaModel
            prompt  = "Hi"
            stream  = $false
            options = @{ num_predict = 1 }
        } | ConvertTo-Json -Compress
        Invoke-WebRequest -Uri "$ollamaBase/api/generate" -Method POST -Body $body `
            -ContentType "application/json" -UseBasicParsing -TimeoutSec 180 | Out-Null
        Write-Host "[Ollama] warm-up xong." -ForegroundColor Green
    } catch {
        Write-Host "[Ollama] warm-up loi (bo qua): $($_.Exception.Message)" -ForegroundColor DarkYellow
    }
} else {
    Write-Host "[Ollama] khong san sang sau 30s - bo qua warm-up" -ForegroundColor DarkYellow
}

# ----------------------------------------------------------------
# 4. JVM env + Spring Boot
# ----------------------------------------------------------------
Write-Host ""
Write-Host "=== Khoi dong Spring Boot (profile=local, Xmx=2g) ===" -ForegroundColor Cyan
Write-Host ""

$env:MAVEN_OPTS = "-Xms256m -Xmx1g"
$jvmArgs = "-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxMetaspaceSize=512m -XX:+ExitOnOutOfMemoryError"

$springExit = 0
try {
    & .\mvnw.cmd spring-boot:run `
        "-Dspring-boot.run.arguments=--spring.profiles.active=local" `
        "-Dspring-boot.run.jvmArguments=$jvmArgs"
    $springExit = $LASTEXITCODE
} finally {
    Write-Host ""
    Write-Host "=== Spring Boot da dung (exit=$springExit) ===" -ForegroundColor Cyan

    if ($nginxAvailable) {
        # Read-Host khong support timeout, nen mac dinh giu nginx chay (tuong duong choice /D N)
        $stop = Read-Host "Stop nginx? (y/N)"
        if ($stop -match '^[Yy]') {
            & $nginxExe -p "$nginxDir/" -c "conf/nginx.conf" -s quit 2>$null | Out-Null
            Start-Sleep -Milliseconds 800
            Get-Process -Name "nginx" -ErrorAction SilentlyContinue | Stop-Process -Force -ErrorAction SilentlyContinue
            Write-Host "[Nginx] da stop." -ForegroundColor Green
        } else {
            Write-Host "[Nginx] van chay tai http://localhost" -ForegroundColor DarkYellow
        }
    }
}

exit $springExit
