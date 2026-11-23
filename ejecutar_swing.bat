@echo off
chcp 65001 > nul
echo ======================================================================
echo   INICIANDO MODO 2: INTERFAZ GRAFICA SWING
echo ======================================================================
start "Copa Mundial - Swing" javaw -Dfile.encoding=UTF-8 -cp "out;lib/*" Main 2
