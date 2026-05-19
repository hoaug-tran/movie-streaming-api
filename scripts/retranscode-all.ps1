# Re-transcode all episodes that have a source.mp4 on disk.
# Usage:
#   .\retranscode-all.ps1 -Token "YOUR_JWT" [-ApiBase "http://localhost:8080"]

param(
  [Parameter(Mandatory = $true)]
  [string]$Token,

  [string]$ApiBase = "http://localhost:8080",

  [string]$DataDir = "F:\movie-storage\data\series\episodes"
)

if (-not (Test-Path $DataDir)) {
  Write-Error "Data dir not found: $DataDir"
  exit 1
}

$headers = @{ Authorization = "Bearer $Token" }
$episodeDirs = Get-ChildItem -Path $DataDir -Directory | Where-Object { $_.Name -match '^\d+$' }

Write-Host "Found $($episodeDirs.Count) episode dirs in $DataDir" -ForegroundColor Cyan
Write-Host "API base: $ApiBase`n" -ForegroundColor Cyan

foreach ($dir in $episodeDirs) {
  $episodeId = $dir.Name
  $sourceFile = Join-Path $dir.FullName "source.mp4"
  if (-not (Test-Path $sourceFile)) {
    Write-Host "[Episode $episodeId] No source.mp4, skipping" -ForegroundColor Yellow
    continue
  }
  try {
    $url = "$ApiBase/api/v1/admin/media/episodes/$episodeId/retranscode"
    Invoke-RestMethod -Method POST -Uri $url -Headers $headers -TimeoutSec 10 | Out-Null
    Write-Host "[Episode $episodeId] Triggered" -ForegroundColor Green
    Start-Sleep -Milliseconds 200
  }
  catch {
    Write-Host "[Episode $episodeId] FAILED: $($_.Exception.Message)" -ForegroundColor Red
  }
}

Write-Host "`nDone. Watch backend logs for [Transcode] progress." -ForegroundColor Cyan
