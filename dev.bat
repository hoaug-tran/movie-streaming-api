@echo off
setlocal ENABLEDELAYEDEXPANSION

REM ====================================================================
REM  Gio Phim Backend Dev Bootstrap
REM    1. Nginx        (HLS / public assets - optional)
REM    2. Port 8080    (don dep PID cu)
REM    3. Ollama       (start + warm-up model)
REM    4. Spring Boot  (profile=local, Xmx=2g)
REM ====================================================================

set "OLLAMA_MODEL=qcwind/qwen3-8b-instruct-Q4-K-M:latest"
set "OLLAMA_BASE=http://localhost:11434"
set "NGINX_DIR=C:\Users\hoaug\nginx"
set "NGINX_EXE=%NGINX_DIR%\nginx.exe"
set "NGINX_AVAILABLE=0"
set "SPRING_PORT=8080"
set "SPRING_EXIT=0"

echo.
echo === Gio Phim Backend Dev Bootstrap ===
echo.

REM --------------------------------------------------------------
REM 1. NGINX (optional - skip neu khong cai)
REM --------------------------------------------------------------
if not exist "%NGINX_EXE%" (
  echo [Nginx] khong tim thay "%NGINX_EXE%" - bo qua nginx.
  goto skipNginx
)
set "NGINX_AVAILABLE=1"

REM Test config truoc khi dong vao process tree
"%NGINX_EXE%" -p "%NGINX_DIR%/" -c "conf/nginx.conf" -t >NUL 2>&1
if errorlevel 1 (
  echo [Nginx] config test FAILED - kiem tra "%NGINX_DIR%\logs\error.log".
  pause
  exit /b 1
)

tasklist /FI "IMAGENAME eq nginx.exe" 2>NUL | find /I "nginx.exe" >NUL
if not errorlevel 1 (
  echo [Nginx] dang chay - reload config (graceful)...
  "%NGINX_EXE%" -p "%NGINX_DIR%/" -c "conf/nginx.conf" -s reload >NUL 2>&1
  if errorlevel 1 (
    echo [Nginx] reload that bai - quit + restart...
    "%NGINX_EXE%" -p "%NGINX_DIR%/" -c "conf/nginx.conf" -s quit >NUL 2>&1
    powershell -NoProfile -Command "Start-Sleep -Milliseconds 1500"
    tasklist /FI "IMAGENAME eq nginx.exe" 2>NUL | find /I "nginx.exe" >NUL
    if not errorlevel 1 taskkill /F /IM nginx.exe >NUL 2>&1
    start "" /B "%NGINX_EXE%" -p "%NGINX_DIR%/" -c "conf/nginx.conf"
    powershell -NoProfile -Command "Start-Sleep -Seconds 2"
  )
) else (
  echo [Nginx] khoi dong...
  start "" /B "%NGINX_EXE%" -p "%NGINX_DIR%/" -c "conf/nginx.conf"
  powershell -NoProfile -Command "Start-Sleep -Seconds 2"
)

tasklist /FI "IMAGENAME eq nginx.exe" 2>NUL | find /I "nginx.exe" >NUL
if errorlevel 1 (
  echo [Nginx] khong start duoc - check "%NGINX_DIR%\logs\error.log".
  pause
  exit /b 1
)
echo [Nginx] OK - http://localhost

:skipNginx

REM --------------------------------------------------------------
REM 2. Don dep port 8080 (Spring Boot session truoc)
REM --------------------------------------------------------------
echo.
echo [Port] kiem tra %SPRING_PORT%...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":%SPRING_PORT% " ^| find "LISTENING"') do (
  echo [Port] kill PID %%a giu %SPRING_PORT%
  taskkill /F /PID %%a >NUL 2>&1
)

REM --------------------------------------------------------------
REM 3. OLLAMA (start neu chua chay + warm-up model)
REM --------------------------------------------------------------
echo.
tasklist /FI "IMAGENAME eq ollama.exe" 2>NUL | find /I "ollama.exe" >NUL
if errorlevel 1 (
  echo [Ollama] chua chay - khoi dong "ollama serve" o background
  start "Ollama Serve" /MIN cmd /c "ollama serve"
  powershell -NoProfile -Command "Start-Sleep -Seconds 3"
) else (
  echo [Ollama] da chay
)

set /a tries=0
:waitOllama
powershell -NoProfile -Command "try { Invoke-WebRequest -Uri '%OLLAMA_BASE%/api/tags' -UseBasicParsing -TimeoutSec 2 | Out-Null; exit 0 } catch { exit 1 }" >NUL 2>&1
if errorlevel 1 (
  set /a tries+=1
  if !tries! GEQ 15 (
    echo [Ollama] khong san sang sau 30s - bo qua warm-up
    goto skipWarmup
  )
  powershell -NoProfile -Command "Start-Sleep -Seconds 2"
  goto waitOllama
)
echo [Ollama] san sang.

echo [Ollama] warm-up model %OLLAMA_MODEL%...
powershell -NoProfile -Command "$body = @{ model='%OLLAMA_MODEL%'; prompt='Hi'; stream=$false; options=@{ num_predict=1 } } | ConvertTo-Json -Compress; try { Invoke-WebRequest -Uri '%OLLAMA_BASE%/api/generate' -Method POST -Body $body -ContentType 'application/json' -UseBasicParsing -TimeoutSec 180 | Out-Null; Write-Host '[Ollama] warm-up xong'; } catch { Write-Host '[Ollama] warm-up loi (bo qua):' $_.Exception.Message }"
:skipWarmup

REM --------------------------------------------------------------
REM 4. JVM env (Xmx=2g de song chung Ollama ~5.5GB)
REM --------------------------------------------------------------
set "MAVEN_OPTS=-Xms256m -Xmx1g"
set "JDK_JAVA_OPTIONS=-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxMetaspaceSize=512m -XX:+ExitOnOutOfMemoryError"

echo.
echo === Khoi dong Spring Boot (profile=local, Xmx=2g) ===
echo.
call .\mvnw.cmd spring-boot:run ^
  -Dspring-boot.run.arguments=--spring.profiles.active=local ^
  -Dspring-boot.run.jvmArguments="-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxMetaspaceSize=512m -XX:+ExitOnOutOfMemoryError"
set "SPRING_EXIT=%ERRORLEVEL%"

REM --------------------------------------------------------------
REM 5. Cleanup khi Spring Boot thoat
REM --------------------------------------------------------------
echo.
echo === Spring Boot da dung (exit=%SPRING_EXIT%) ===
echo.

if "%NGINX_AVAILABLE%"=="0" goto end

REM Auto-default = N sau 10s, tranh treo terminal khi dev iterate
choice /C YN /T 10 /D N /M "Stop nginx ?"
if errorlevel 2 goto end
echo [Nginx] dang stop...
"%NGINX_EXE%" -p "%NGINX_DIR%/" -c "conf/nginx.conf" -s quit >NUL 2>&1
powershell -NoProfile -Command "Start-Sleep -Milliseconds 800"
tasklist /FI "IMAGENAME eq nginx.exe" 2>NUL | find /I "nginx.exe" >NUL
if not errorlevel 1 taskkill /F /IM nginx.exe >NUL 2>&1
echo [Nginx] da stop.

:end
endlocal & exit /b %SPRING_EXIT%
