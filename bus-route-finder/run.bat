@echo off
echo Building project...
if not exist "target\classes" mkdir target\classes
dir /s /b src\main\java\*.java > sources.txt
javac -encoding UTF-8 -d target/classes @sources.txt
del sources.txt

if %errorlevel% neq 0 (
    echo.
    echo Compilation failed. Make sure Java JDK 17+ is installed and on your PATH.
    echo Download from: https://adoptium.net
    pause
    exit /b %errorlevel%
)

echo.
echo Starting Bhopal Bus Route Finder...
java -cp target/classes com.busroute.Main
pause
