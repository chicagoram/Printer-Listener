@echo on
setlocal enableextensions enabledelayedexpansion

pushd "%~1"

for  %%f in (*) do (


set ARG=%%~nxf
rename "%%f" !ARG: =!

)

for  %%f in (*) do (


set ARG=%%~nxf
rename "%%f" !ARG:_MD=!


)
endlocal