@echo off
chcp 65001 > nul
echo ======================================================================
echo   COMPILANDO SISTEMA DE GESTION DE LA COPA MUNDIAL DE FUTBOL
echo ======================================================================
if not exist out mkdir out
javac -encoding UTF-8 -cp "lib/*" -d out src/*.java
if %ERRORLEVEL% equ 0 (
    echo.
    echo  [OK] Compilacion completada con exito. Clases generadas en /out
    echo.
) else (
    echo.
    echo  [ERROR] Ocurrio un error durante la compilacion.
    echo.
)
pause
