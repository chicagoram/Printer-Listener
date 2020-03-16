@echo off
forfiles /p "C:\Processed_41056P" /s /m *.tif /D -5 /C "cmd /c del @path"

