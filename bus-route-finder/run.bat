@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.13.11-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo Building project...
if not exist "target\classes" mkdir target\classes
dir /s /b src\main\java\*.java > sources.txt
javac -encoding UTF-8 -d target/classes @sources.txt
del sources.txt

if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)

echo Starting Bhopal Bus Route Finder...
java -cp target/classes com.busroute.Main
pause
