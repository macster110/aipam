package com.jamdev.maven.aipam.layout.detectionDisplay;

/**
 * The limits of the data that the data provider can provide. 
 * 
 * @author Jamie Macaulay
 *
 */
public class DataLimits {
	
	/**
	 * The maximum 
	 */
	private double max;
	
	/**
	 * The minimum. 
	 */
	private double min;

	public DataLimits(double min, double max) {
		this.max=max; 
		this.min=min;
	}
	
	/**
	 * Get the maximum limit. 
	 * @return the maximum limit. 
	 */
	public double getMaxLimit() {
		return max;
	}
	
	/**
	 * Get the minimum limit. 
	 * @return the minimum limit. 
	 */
	public double getMinLimit() {
		return min;
	}


}
