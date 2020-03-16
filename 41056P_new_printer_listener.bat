TITLE 41056P NEW PRINTER LISTENER

REM Log File Name
set A_LOG="activity_41056P"

REM The listener watches this directory for batch files
set WATCH="C:\41056P"

REM Processed batches are moved to this folder
set PROCESSED="C:\processed_41056P"

REM Delay between successive batches
set DELAY=60

REM Hotfolder Directory
set HF=c:\41056H

REM folder.cfg directory
set CF=C:\41056H\[_EFI_HotFolder_]

REM NAME OF THE LISTENER
set PRT="41056_PRINTER_LISTENER"

REM TYPE OF WRAPS BEING PRINTED  AT ANY GIVEN POINT OF TIME
set HF_PAT="*.*"

REM TYPE OF BATCHES BEING SENT BY MEDIALYNX
set BT_PAT="*.txt"

REM NUMBER OF ITERATIONS THE LISTENER WILL DO LOOKING FOR FILES IN HOTFOLDER DIRECTORY
set HF_CHK=2

REM NUMBER OF SECONDS (PER ITERATION) WAIT BEFORE SENDING FILES TO HOTFOLDER ...IN THIS INSTANCE ITS 30*2 = 60 SECONDS
set HF_WAIT=30 

REM Content layering process log
set CT_LOG=C:\41056P_new_printer_listener_logs\art_conversion_status.log

REM Content layering process  (single sided)
set CT_PRC_SINGLE=C:\41056P_new_printer_listener\layer_single_sided.bat

REM Content layering process  (single sided)
set CT_PRC_DOUBLE=C:\41056P_new_printer_listener\layer_double_sided.bat

REM Email Host Parms
set HOST=smtp.googlemail.com
set PORT=587
set USER=alliedvaughnits
set PWD=26Baba17#
set FROM=chicagoram@gmail.com
set TO=ram_1726@yahoo.com,mark.duranty@alliedvaughn.com
		
 

C:\java13\bin\java -Dactivity_log=%A_LOG% -cp .;C:/41056P_new_printer_listener;C:/41056P_new_printer_listener/41056_new_printer_listener.jar; com.print.service.DirectoryWatchService %WATCH% %PROCESSED% %DELAY%  %HF% %CF% %HF_CHK%  %PRT% %HF_PAT% %HF_WAIT% %BT_PAT% %CT_LOG% %CT_PRC_SINGLE% %CT_PRC_DOUBLE% %HOST% %PORT% %USER% %PWD% %FROM% %TO%
pause