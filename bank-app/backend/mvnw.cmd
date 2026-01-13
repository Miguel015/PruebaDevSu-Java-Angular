@echo off
setlocal
set MAVEN_WRAPPER_DIR=%~dp0\.mvn\wrapper
if exist "%MAVEN_WRAPPER_DIR%\maven-wrapper.jar" (
  java -jar "%MAVEN_WRAPPER_DIR%\maven-wrapper.jar" %*
) else (
  echo Maven wrapper jar not found. Please run scripts\install-maven-wrapper.ps1 to download it.
  exit /b 1
)
