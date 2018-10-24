package com.jamdev.maven.clips;

import java.io.File;
import java.io.InputStream;

import org.datavec.audio.Wave;
import org.datavec.audio.WaveHeader;

/**
 * Audio data with an added AudioPlay class
 * @author Jamie Macaulay 
 *
 */
public class ClipWave extends Wave {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Plays the audio file without requiring the raw audio data to be stored in memory in wave
	 */
	private AudioPlay audioPlay;
	
	/**
	 * Original file the audio came from 
	 */
	private File file; 

	public ClipWave(File audioFile, WaveHeader wavHeader, byte[] data) {
		super(wavHeader, data);
		//System.out.println("data length: " + data.length + " chan: " + wavHeader.getChannels() + " file seconds: " + this.length() + " samples: "+ this.getSampleAmplitudes().length);
		//trimClip(maxLen); //this does not work. 
		this.file=audioFile; 
		this.audioPlay = new StandardAudioPlay(audioFile); 
	}
	
	
	/**
	 * Get the data audio play 
	 */
	public AudioPlay getAudioPlay() {
		return audioPlay;
	}
	
	
	/**
	 * Trim the clip so it meets the clipLength 
	 * @param clipLength - the maximum clip length in seconds. 
	 */
	@Deprecated 
	private void trimClip(double clipLength) {
		double length = this.length();
		if (length<clipLength) return; 
		//now need to trim from the centre- we want o trim the front and 
		//back of the clip so the centre is preserved. 
		double trimLen = (length-clipLength)/2.; 
		
		//System.out.println("Hello: " + trimLen + "  clip len: " +length);
		this.leftTrim(trimLen);
		
	}

	/**
	 * Get the file the audio was laoded from
	 * @return -the audio the file was loaded from. 
	 */
	public String getFileName() {
		return file.getAbsolutePath();
	}

}
