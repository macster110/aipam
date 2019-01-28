package com.jamdev.maven.aipam.clips;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.jamdev.maven.aipam.AIPamParams;

/**
 * Handles importing of files to clips. Clips could be .wav, .mp3 etc. or even
 * bespoke file types e.g. binary files. 
 * 
 * 
 * @author Jamie Macaulay
 */
public interface AudioImporter {
	
	/**
	 * List all the audio file in a directory. 
	 * @param audioFileDirectory - the audio file directory. 
	 * @return a list of all the audio files which can be imported. 
	 */
	public List<File> listAudioFiles(File audioFileDirectory); 
	
	/**
	 * Imports clips from a file. The file may be one clip or it's possible files
	 * which contain multiple clips could exist. Thus returns an array of audio clips. 
	 * @param audioFile - the file to import a clip(s) from. 
	 * @param maxLen - the maximum length of the file allowed. 
	 * @return a list of clips contained in the file. 
	 */
	public ArrayList<ClipWave> importAudio(File audioFile,  AIPamParams aiPamParams, AudioImporterListener audioImporterListener);
	
	/**
	 * Get the audio information on a directory of files. This is a first run which does not import the
	 * audio but scans the directory for files an ensures some basic pre-conditions are met. 
	 * @param audioFile - the audio file directory  
	 * @return the audio info class on the directory., 
	 */
	public AudioInfo getAudioInfo(File audioFileDirectory, AudioImporterListener audioImporterListener); 

	

}
