@echo off
@REM $Id: rdfserver.bat,v 1.1 2011/08/01 15:25:27 nick Exp $

if not "%JOSEKIROOT%" == "" goto :ok
echo Environment variable JOSEKIROOT not set
goto :theEnd

:ok
call bin\joseki_path.bat

REM set LOGCONFIG=file:%JOSEKIROOT%\etc\log4j-detail.properties
set LOGCONFIG=file:%JOSEKIROOT%\etc\log4j.properties
set LOG=-Dlog4j.configuration=%LOGCONFIG%

set JAVA=java.exe

:endJavaHome

%JAVA% -cp %CP% %LOG% joseki.rdfserver %1 %2 %3 %4 %5 %6 %7 %8 %9

:theEnd
