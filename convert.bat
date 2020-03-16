REM c:\progra~1\ImageMagick-7.0.7-Q16\convert.exe %1 %~2 %~3 %~4 %~5 %~6
REM sourceFile, density, overlayFile, destFile, page, geometry
c:\progra~1\ImageMagick-7.0.7-Q16\convert.exe  %1 ( -clone %~5 -density  %~2 %~3 -geometry %~6 -composite ) -delete %~5 -insert %~5 %~4
set error=%ERRORLEVEL%
IF %error% NEQ 0 ( set Timestamp=%date:~4,2%%date:~7,2%%date:~10,4%_%time:~0,2%%time:~3,2%%time:~6,2%
call C:\41056P_new_printer_listener\mailme_failure.bat %~1 "return_code=%error%"
echo Layering failed for file %~1 with return code=%error% >> "C:\41056P_new_printer_listener_logs\content_layering_error_%error%_%Timestamp%.log"


