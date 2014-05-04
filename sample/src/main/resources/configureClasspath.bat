@echo off
set CLASSPATH=
for %%i in (".\lib\*.jar") do call :addjar %%i
goto :eof

:addjar
set CLASSPATH=%1;%CLASSPATH%