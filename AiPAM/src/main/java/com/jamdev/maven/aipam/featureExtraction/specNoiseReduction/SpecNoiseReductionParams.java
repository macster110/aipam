package com.jamdev.maven.aipam.featureExtraction.specNoiseReduction;


/**
 * Parameters for the spectrogram noise reduction method. 
 * @author Jamie Macaulay
 *
 */
public class SpecNoiseReductionParams implements Cloneable {

	/**
	 * A list of paramters for each spec noise method. 
	 */
	public Object[] specNoiseParams;
	
	/**
	 * True or false to run the method. Must be the same size as specNoiseParams. 
	 */
	public boolean[] runMethod;
	
	
	@Override
	public SpecNoiseReductionParams clone() {
		try {
			SpecNoiseReductionParams clonedParmas =  (SpecNoiseReductionParams) super.clone();
			
			for (int i=0; i<specNoiseParams.length; i++) {
				clonedParmas.specNoiseParams[i] =  specNoiseParams[i]; //TODO need to clone these. 
				clonedParmas.runMethod[i] = runMethod[i]; 
			}

			return clonedParmas; 

		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
