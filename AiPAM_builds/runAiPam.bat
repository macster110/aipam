@echo off
set path= C:\Program Files\Java\jre1.8.0_131\bin\%path%
java -version
java -Xmx4098m -Dprism.maxvram=2000m -jar aipam_090.jar
pause
exit