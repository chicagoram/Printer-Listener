@echo off
forfiles /p "C:\Processed_41056P" /s /m *.* /D -2 /C "cmd /c del @path"
forfiles /p "C:\41056H\[_EFI_HotFolder_]\[MoveFolder]" /s /m *.* /D -2 /C "cmd /c del @path"



