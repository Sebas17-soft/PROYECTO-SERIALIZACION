@echo off
chcp 65001 > nul
echo ======================================================================
echo   INICIANDO MODO 3: INTERFAZ GRAFICA JAVAFX
echo ======================================================================
java -Dfile.encoding=UTF-8 --enable-native-access=javafx.graphics --module-path lib --add-modules javafx.controls -cp out Main 3
if %ERRORLEVEL% neq 0 (
    echo.
    echo [AVISO] Si JavaFX no inicio, puede ejecutar el Modo 2 Swing con ejecutar_swing.bat
    pause
)
