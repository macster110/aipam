package com.jamdev.maven.aipam.clips;

/**
 * Listener for audio import. 
 * @author Jamie Macaulay 
 *
 */
public interface AudioImporterListener {
	
	/**
	 * Updates the audio import progress. This might be used either 
	 * to show multiple file progress or progres of one or more larger files...
	 * @param progress - the progress from 0 to 1. 
	 * @param fileN - the file number being imported. 
	 * @param nFiles - the total number of files. 
	 */
	public void updateProgress(double progress, int fileN, int nFiles); 
	
	/**
	 * True if the import is cancelled. 
	 * @return
	 */
	public boolean isCancelled(); 


}
