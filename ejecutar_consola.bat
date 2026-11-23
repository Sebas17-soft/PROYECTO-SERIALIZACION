@echo off
chcp 65001 > nul
echo ======================================================================
echo   INICIANDO MODO 1: CONSOLA DE TEXTO (SCANNER)
echo ======================================================================
java -Dfile.encoding=UTF-8 -cp "out;lib/*" Main 1
pause
