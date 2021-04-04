package com.jamdev.maven.aipam.layout.detectionDisplay;

/**
 * A listener for new audio data. 
 * @author Jamie Macaulay 
 *
 */
public interface DataProviderListener {
	
	/**
	 * Called whenever new audio data is added. 
	 * @param dataUnit - the new data unit. 
	 */
	public void newAudioData(PAMDataUnit dataUnit);

}
