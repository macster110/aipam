package com.jamdev.maven.clips;


import org.datavec.audio.Wave;

/**
 * A single clip for display on the clip pane. 
 * <p>
 * The clip stores a spectogram image but no raw wav data. 
 * Can be dealing with Gigabyte of clips so cannot store these in memory
 * 
 * @author Jamie Macaulay 
 *
 */
public class PAMClip {
	
	/**
	 * Data for colours for spectrogram is stored as a short.
	 */
	private short[][] spectrogramClip; 
	
	/**
	 * The filename of the clip
	 */
	public String fileName;
	
	
	public PAMClip(Wave wave){
		
	} 


	

}
