@echo off
chcp 65001 > nul
echo ======================================================================
echo   LANZADOR PRINCIPAL - COPA MUNDIAL DE FUTBOL
echo ======================================================================
java -Dfile.encoding=UTF-8 --enable-native-access=javafx.graphics --module-path lib --add-modules javafx.controls -cp "out;lib/*" Main
pause
