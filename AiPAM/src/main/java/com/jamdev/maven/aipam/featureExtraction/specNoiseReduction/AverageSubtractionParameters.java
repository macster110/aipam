package com.jamdev.maven.aipam.featureExtraction.specNoiseReduction;

/**
 * Parameters for the average subtraction method
 * 
 * @author Doug Gillespie
 * @author Jamie Macaulay
 *
 */
public class AverageSubtractionParameters implements Cloneable {

	static public final long serialVersionUID = 0;
	
	public double updateConstant = 0.02;
	

	@Override
	public AverageSubtractionParameters clone() {
		try {
			return (AverageSubtractionParameters) super.clone();
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
