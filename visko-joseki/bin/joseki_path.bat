@echo off
@REM Script to build the Joseki classpath
@REM $Id: joseki_path.bat,v 1.1 2011/08/01 15:25:27 nick Exp $

@REM Must be windows format \ not /
if "%JOSEKIROOT%" == ""  goto noRoot

set CP=""
REM Do this to put the developement .class files first
REM NB no space before the ")"
if EXIST %JOSEKIROOT%\classes (
  if "%CP%" == "" (set CP=%JOSEKIROOT%\classes) ELSE (set CP=%CP%;%JOSEKIROOT%\classes)
)

pushd %JOSEKIROOT%
for %%f in (lib\*.jar) do call :oneStep %%f
popd
goto noMore

:oneStep
if "%CP%" == "" (set CP=%JOSEKIROOT%\%1) ELSE (set CP=%CP%;%JOSEKIROOT%\%1)
exit /B

:noRoot
echo Environment variable JOSEKIROOT needs to be set

:noMore

