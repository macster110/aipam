package com.jamdev.maven.clips;

import java.io.File;

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

	/**
	 * Import a .wav clip. 
	 * @param file - the file to import
	 * @return the 
	 */
	public int importWav(File file) {
		// TODO Auto-generated method stub
		return 1; 
	}
	
	

}
