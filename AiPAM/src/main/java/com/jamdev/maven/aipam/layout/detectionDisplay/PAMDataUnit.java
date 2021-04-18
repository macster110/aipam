package com.jamdev.maven.aipam.layout.detectionDisplay;

/**
 * Basic metadata that every input must have for the detection display
 * @author au671271
 *
 */
public interface PAMDataUnit {

	/**
	 * Get the time  of the detection in milliseconds. 
	 * @return
	 */
	double getTimeMilliseconds();

	double getDurationInMilliseconds();

	long getUID();

	float getSampleRate();


}
