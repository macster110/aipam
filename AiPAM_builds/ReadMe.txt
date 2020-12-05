#Running the aipam.jar file

If using Windows simply download the .exe file and double click to open. For version > 0920 Java 13 is required. This can be downloaded as a JDK from oracle.

Alternatively

runAiPam.bat is a bat file that will run the jar file with the correct vm argumnets

For versions < 0920 you must have java 8 installed for this to work, ideally version 131. You may need to change "set path"
in the .bat file to your java 8 jre or jdk folder. To do this open the file in notepad, change the path, 
save and then double click the file again. Java 131 is the last release before a bug is introduced which prevents SOundSort from playing audi clips. 

Change log


SoundSort_094.exe - requires Java 14. Bug fixes for low frequency recordings and fixes spectrogram hop. 

SoundSort_093a.exe -windows executable. May requie Java 13 download. New feature extraction tools allows users to threshold spectrograms, run median filters etc. Also new system for sound playback to try andget round the JavaFX bug which does not deal with multi channe files. Now supports 24 bit sound files. Better memmory management for spectrograms 

SoundSort_0921b.exe - windows executable. May require Java 13 download.Based on JavaFX 14 and major revision to sound playback to make more reliable. 

aipam_0911: Fixed bug in spectorgram preview 

aipam_091: Added multi clip playback option and spectorgram preview to make changing colour limits easier.

aipam_090: Fixes to import clips task and speed improvements. Fix to assignement problem task so that cancel button works properly. 

aipam_089: Original release.




