package com.jamdev.maven.aipam.clips;

import java.io.File;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * This is an example program that demonstrates how to play back an audio file
 * using the SourceDataLine in Java Sound API.
 * @author www.codejava.net
 *
 */
public class JavaSoundAPITest {

	// size of the byte buffer used to read/write the audio stream
	private static final int BUFFER_SIZE = 4096;

	private static final int CHAN_PLAY_BACK = 2; //playback two channels. 
	
	/**
	 * Play a given audio file.
	 * @param audioFilePath Path of the audio file.
	 */
	void play(String audioFilePath) {
		File audioFile = new File(audioFilePath);
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);



			AudioFormat audioStreamFormat = audioStream.getFormat();

			AudioFormat outputFormat;
			if (audioStreamFormat.getChannels()<=CHAN_PLAY_BACK) {
				outputFormat = audioStreamFormat; 
			}
			else {
				outputFormat = new AudioFormat(audioStreamFormat.getEncoding(),
						audioStreamFormat.getSampleRate(), audioStreamFormat.getSampleSizeInBits(), 
						CHAN_PLAY_BACK, CHAN_PLAY_BACK*audioStreamFormat.getSampleSizeInBits()/8 ,audioStreamFormat.getFrameRate(), false);
			}

			DataLine.Info info = new DataLine.Info(SourceDataLine.class, outputFormat);

			SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);

			System.out.println(audioLine.toString()); 

			audioLine.open(outputFormat);

			audioLine.start();

			System.out.println("Playback started.");

			byte[] bytesBuffer = new byte[BUFFER_SIZE];
			byte[] bytesbuffer2chan = new byte[BUFFER_SIZE];

			int bytesRead = 0;
			int bytecount = 0; 
			int bytecount2chan = 0; 


			//			//now we have to be careful and play only the first two channels. 
			//			int count=0; 

			/** 
			 * If the file has one or two channels then keep it simple and play back all bytes 
			 */
			if (audioStreamFormat.getChannels()<=2) {
				while ((bytesRead = audioStream.read(bytesBuffer))!=-1 ) {
					if (bytesRead==BUFFER_SIZE) {
						audioLine.write(bytesBuffer, 0, bytesRead);
					}
				}
			}
			else {
				/**
				 * If we have a multi channel file then only play back the preferred number
				 * of channels. So have to skip those extra cghannels 
				 */
				while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
					//now need to remove the extra channels 
					bytecount = 0; 
					bytecount2chan=0;

					//now only use the first bytes in the frame. 
					while (bytecount<bytesRead) {
						if (bytecount%audioStreamFormat.getFrameSize()<audioStreamFormat.getSampleSizeInBits()/8*CHAN_PLAY_BACK) {
							bytesbuffer2chan[bytecount2chan] = bytesBuffer[bytecount]; 
							bytecount2chan++; 
						}
						bytecount++; 
					}
					audioLine.write(bytesbuffer2chan, 0, bytecount2chan);
				}

			}

			audioLine.drain();
			audioLine.close();
			audioStream.close();

			System.out.println("Playback completed.");

		} catch (UnsupportedAudioFileException ex) {
			System.out.println("The specified audio file is not supported.");
			ex.printStackTrace();
		} catch (LineUnavailableException ex) {
			System.out.println("Audio line for playing back is unavailable.");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Error playing the audio file.");
			ex.printStackTrace();
		}      
	}

	public static void main(String[] args) throws InterruptedException {

		String audioFilePath = "C:\\Users\\macst\\Desktop\\Ish20180811_092333_319_2chan.wav";

		//	         audioFilePath = "E:\\Google Drive\\Example_PAM\\Sperm_Whale_IFAW\\MF20070419_101650.wav";

		JavaSoundAPITest player = new JavaSoundAPITest();
		player.play(audioFilePath);

		Thread.sleep(2000);


	}




}
