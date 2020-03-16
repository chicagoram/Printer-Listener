@set /a UHH=%TIME:~0,2%
@set OHH=0%UHH%
@set HH=%OHH:~-2%
@SET NOW=%date:~10,4%-%date:~4,2%-%date:~7,2%--%HH%.%time:~3,2%

C:\41056P_new_listener\mailsend1.19 -d alliedvaughn.com -smtp "smtp.gmail.com" -to "ram_1726@yahoo.com,mark.duranty@alliedvaughn.com" -ssl -port 465 -auth -f alliedvaughnits@gmail.com -user alliedvaughnits -pass "26Baba17#"  -sub " Content Layering failed for file %1 on  %COMPUTERNAME% " -M %2
