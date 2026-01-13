# PowerShell script to download the Maven Wrapper jar into .mvn\wrapper
$wrapperDir = Join-Path -Path $PSScriptRoot -ChildPath "..\.mvn\wrapper"
$wrapperDir = Resolve-Path -Path $wrapperDir -ErrorAction SilentlyContinue
if (-not $wrapperDir) {
  $wrapperDir = Join-Path -Path $PSScriptRoot -ChildPath "..\.mvn\wrapper"
  New-Item -ItemType Directory -Path $wrapperDir -Force | Out-Null
}
$wrapperJar = Join-Path -Path $wrapperDir -ChildPath "maven-wrapper.jar"
$wrapperUrl = 'https://repo1.maven.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar'

Write-Host "Downloading maven-wrapper.jar to $wrapperJar"
try {
    Invoke-WebRequest -Uri $wrapperUrl -OutFile $wrapperJar -UseBasicParsing -ErrorAction Stop
    Write-Host "Downloaded successfully. You can now run .\mvnw -v or .\mvnw spring-boot:run"
} catch {
    Write-Error "Failed to download maven-wrapper.jar: $_"
}
