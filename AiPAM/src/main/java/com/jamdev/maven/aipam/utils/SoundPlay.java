package com.jamdev.maven.aipam.utils;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Play multi-channel .wave clips based on the the Java SOund API. 
 * @author Jamie Macaulay 
 *
 */
public class SoundPlay {


	// size of the byte buffer used to read/write the audio stream
	private static final int BUFFER_SIZE = 4096;

	/**
	 * The default number of channels to play back. 
	 */
	private static final int CHAN_PLAY_BACK = 2; //play back two channels. 

	/**
	 * Maximum allowed sample rate for soundcard. 
	 */
	private static final float SAMPLERATE = 40000; //play back two channels. 


	/**
	 * True to stop any playback
	 */
	private boolean stop;

	/**
	 * The volume in dB
	 */
	private float volumedB = 0; 

	/**
	 * The current audio line
	 */
	private SourceDataLine audioLine;

	/**
	 * True if currently playiung. 
	 */
	private volatile boolean isPlaying = false;

	/**
	 * The audio stream format of the current file 
	 */
	private AudioFormat audioStreamFormat;

	/**
	 * The audio stream from the file to play
	 */
	private AudioInputStream audioStream;

	/**
	 * The number of times to down sample. i.e. sR = sR/downsample;  
	 */ 
	private int downsample = 1; 


	/**
	 * Setup the audi0 file for playing. 
	 * @param audioFile - true to play the file. 
	 * @return true if the file seems OK to play
	 */
	public boolean prepFile(File audioFile) {
		try {
			audioStream = AudioSystem.getAudioInputStream(audioFile);

			audioStreamFormat = audioStream.getFormat();

			AudioFormat outputFormat;

			//figure out if we have to downsample the data 
			downsample = 1; 
			float sR = 	audioStreamFormat.getSampleRate(); 

			while (sR>SAMPLERATE) {
				downsample = downsample*2; 
				sR= sR/2; 
			}

			//			System.out.println("Downsample: " + downsample + " new sR: " +sR); 

			if (audioStreamFormat.getChannels()<=CHAN_PLAY_BACK && audioStreamFormat.getSampleRate()<=SAMPLERATE) {
				//don;t mess it up if we don;t have to. 
				outputFormat = audioStreamFormat; 
			}
			else {
				int chan = Math.min(audioStreamFormat.getChannels(), CHAN_PLAY_BACK); 
				outputFormat = new AudioFormat(audioStreamFormat.getEncoding(),
						audioStreamFormat.getSampleRate()/downsample, audioStreamFormat.getSampleSizeInBits(), 
						chan,  chan*audioStreamFormat.getSampleSizeInBits()/8 ,audioStreamFormat.getFrameRate(), false);
			}

			System.out.println("Input"); 
			System.out.println(audioStreamFormat.toString()); 
			System.out.println("Output"); 
			System.out.println(outputFormat.toString()); 

			DataLine.Info info = new DataLine.Info(SourceDataLine.class, outputFormat);

			audioLine = (SourceDataLine) AudioSystem.getLine(info);

			//System.out.println(audioLine.toString()); 

			audioLine.open(outputFormat);

			return true;

		} catch (UnsupportedAudioFileException ex) {
			System.err.println("UnsupportedAudioFileException: The specified audio file is not supported.");
			//ex.printStackTrace();
			return false; 
		} catch (LineUnavailableException ex) {
			System.err.println("LineUnavailableException: Audio line for playing back is unavailable.");
			//ex.printStackTrace();
			return false; 
		} catch (IOException ex) {
			System.err.println("IOException: Error playing the audio file.");
			//ex.printStackTrace();
			return false; 
		}
		catch (Exception e) {
			System.err.println("Exception: Error playing the audio file.");
			//e.printStackTrace();
			return false; 
		}

	}

	/**
	 * Play a given audio file.
	 * @param audioFilePath Path of the audio file.
	 */
	public void play(File audioFile) {

		stop= false; 

		prepFile( audioFile);

		try {

			updateVolume();
			audioLine.start();

			System.out.println("Playback started.");

			byte[] bytesBuffer = new byte[BUFFER_SIZE];
			byte[] bytesbuffer2chan = new byte[BUFFER_SIZE];
			byte[] bytes2SR =  new byte[BUFFER_SIZE];
			
			int bytesRead = 0;
			int bytecount = 0; 
			int bytecount2chan = 0; 


			//			//now we have to be careful and play only the first two channels. 
			//			int count=0; 

			audioLine.addLineListener(( event)->{
				if (event.getType().equals(LineEvent.Type.STOP)) {
					isPlaying=false; 
				}
			});

			isPlaying = true; 

			/** 
			 * If the file has one or two channels then keep it simple and play back all bytes 
			 */
			if (audioStreamFormat.getChannels()<=2) {
				while ((bytesRead = audioStream.read(bytesBuffer))!=-1 ) {
					if (bytesRead==BUFFER_SIZE) {
						writeDownSample(bytesBuffer, 0, bytesRead, bytes2SR,  downsample,  audioStreamFormat); 
						//audioLine.write(bytesBuffer, 0, bytesRead);
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
					writeDownSample(bytesbuffer2chan, 0, bytecount2chan, bytes2SR,  downsample,  audioStreamFormat); 
					//audioLine.write(bytesbuffer2chan, 0, bytecount2chan);
					if (stop) {
						audioLine.stop();
					}
				}

			}

			audioLine.drain();
			audioLine.close();
			audioStream.close();

			isPlaying = false; 
			System.out.println("Playback completed.");

		} catch (IOException ex) {
			System.out.println("Error playing the audio file.");
			ex.printStackTrace();
		}      
	}



	/**
	 * Write downsampled audio to the buffer. 
	 */
	private void writeDownSample(byte[] b, int off, int len, byte[] downsmpleb, int downsample, AudioFormat audioStreamFormat) {
		if (downsample==1) {
			//no need to downsample the audio
			audioLine.write(b, 0, len);
			return; 
		}
		
		//TODO really need to put in an anti-aliasing filter here...

		//wav files are in order of frame where each frame contains samples form all channels. 
		//samply need to skip frames. 
		int frameNum = 0; 
		int n=0; 
		for (int i=0; i<len; i++) {
			//can we add? 
			if (i%audioStreamFormat.getFrameSize()==0){
				frameNum++; 
			} 

			if (frameNum%downsample==0) {
				downsmpleb[n]=b[i]; 
				n++; 
			}
		}

		//System.out.println("Play audio " + n + "  " + len + " " + frameNum); 
		audioLine.write(downsmpleb, 0, n);
	}


	/**
	 * Check whether audio is playing. 
	 * @return true if playing. 
	 */
	public boolean isPlaying() {
		if (audioLine!=null) {
			return isPlaying; 
		}
		else return false; 
	}

	/*
	 * Stop the audio from playing
	 */
	public void stop() {
		this.stop = true;
		if (audioLine!=null) {
			audioLine.stop();
		}
	}

	/**
	 * Set the volume in dB. 0 is default and +/- values are dB gain. 
	 * @param volumedB - the volume gain in dB. 
	 */
	public void setVolume(float volumedB) {
		this.volumedB = volumedB; 
		updateVolume();
	}

	/**
	 * Update the volume if audioLine is available. 
	 */
	private void updateVolume() {
		if (audioLine!=null && audioLine.isControlSupported( FloatControl.Type.MASTER_GAIN)) {
			FloatControl gainControl = 
					(FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volumedB); //reduce or increase by volume dB 
		}

	}

}