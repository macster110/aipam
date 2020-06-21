package com.jamdev.maven.aipam.featureExtraction.specNoiseReduction;

/**
 * Parameters for the median filter. 
 * @author Jamie Macaulay
 *
 */
public class MedianFilterParams implements Cloneable {
	
	public int filterLength = 61;
	
	@Override
	public MedianFilterParams clone() {
		try {
			return (MedianFilterParams) super.clone();
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
