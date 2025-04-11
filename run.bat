@echo off
REM Variables
set JAVA_HOME=JavaEnvironment\zulu19.32.15-ca-fx-jdk19.0.2-win_x64\zulu19.32.15-ca-fx-jdk19.0.2-win_x64
set JAVAFX_PATH=%JAVA_HOME%\lib
set SRC_PATH=src
set OUT_PATH=bin
set LIB_PATH=lib

REM Create output directory if it doesn't exist
if not exist %OUT_PATH% mkdir %OUT_PATH%

REM Compile all Java files
echo Compiling all Java files recursively...
for /R %SRC_PATH% %%f in (*.java) do (
    echo Compiling %%f
    "%JAVA_HOME%\bin\javac" -cp "%LIB_PATH%\*;%SRC_PATH%" -d %OUT_PATH% "%%f"
    if errorlevel 1 (
        echo Failed to compile %%f
        exit /b 1
    )
)

REM Copy resources to output directory
echo Copying resources...
xcopy /E /Y /I %SRC_PATH%\resources %OUT_PATH%\resources

REM Run the application
echo Running the application...
"%JAVA_HOME%\bin\java" --add-modules javafx.controls,javafx.fxml -cp "%OUT_PATH%;%LIB_PATH%\*" com.dashapp.Main
