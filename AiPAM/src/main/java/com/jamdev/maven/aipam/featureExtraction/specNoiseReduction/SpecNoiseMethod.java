package com.jamdev.maven.aipam.featureExtraction.specNoiseReduction;

import com.jamdev.maven.aipam.layout.featureExtraction.SpecNoiseNodeFX;
import com.jamdev.maven.aipam.utils.ComplexArray;

/**
 * Interface for multiple plugins to the spectrogram noise
 * reduction system. 
 * @author Doug Gillespie
 *
 */
public abstract class SpecNoiseMethod {

	/**
	 * Get a name for the method.
	 * @return the name of the method. 
	 */
	public abstract String getName();
	
	/**
	 * Get a longer description of the method
	 * in HTML format for hover texts in dialogs. 
	 * @return HTML description
	 */
	public abstract String getDescription();
	
	/**
	 * Run the noise reduction on the data in place. 
	 * @param fftData array of FFT data (half FFT length)
	 * @return true if ran OK/ 
	 */
	public abstract boolean runNoiseReduction(ComplexArray fftDataUnit);
	
	/**
	 * 
	 * @return the delay imposed on the data by this operation.
	 */
	public abstract int getDelay();
	
	/**
	 * Set up the noise reduction process
	 * @return true if initialised OK. 
	 */
	public abstract boolean initialise(int channelMap);

	/**
	 * Get the FX node for the spectrogram method. 
	 * @return the FX node for the spectrogram method. 
	 */
	public abstract SpecNoiseNodeFX getSettingsPane();
	
}