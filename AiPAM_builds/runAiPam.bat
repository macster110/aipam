@echo off
set path= C:\Program Files\Java\jdk1.8.0_191\bin\%path%
java -version
java -Xmx4098m -Dprism.maxvram=2000m -jar aipam.jar
pause
exit