package com.jamdev.maven.clips;

/**
 * Holds some basic information on the audio files. 
 * 
 * 
 * @author Jamie Macaulay 
 *
 */
public class AudioInfo {

	/**
	 * The number of files 
	 */
	public int nFiles = 0 ;
	
	/**
	 * The sample rate of the files
	 */
	public float sampleRate = 0; 
	
	/**
	 * The number of channels each file has. 
	 */
	public int channels = 0; 
	
	/**
	 * Are all the files the same sample rate
	 */
	public boolean isSameSampleRate = false; 
	
	/**
	 * Are all the files the same channels. 
	 */
	public boolean isSameChannels = false;

	/**
	 * The number of corrupt files. 
	 */
	public int nFilesCorrupt;

	public String file; 
}
