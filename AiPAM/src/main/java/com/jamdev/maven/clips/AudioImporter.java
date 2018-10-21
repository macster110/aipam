package com.jamdev.maven.clips;

import java.io.File;
import java.util.ArrayList;

import org.datavec.audio.Wave;

/**
 * Handles importing of files to clips. Clips could be .wav, .mp3 etc. or even
 * bespoke file types e.g. binary files. 
 * @author Jamie Macaulay
 *
 */
public interface AudioImporter {
	
	/**
	 * Imports clips from a file. The file may be one clip or it's possible files
	 * which contain multiple clips could exist. Thus returns an array of audio clips. 
	 * @param audioFile - the file to import a clip(s) from. 
	 * @return a list of clips contained in the file. 
	 */
	public ArrayList<Wave> importAudio(File audioFile);

	

}
