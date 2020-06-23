package com.jamdev.maven.aipam.layout.featureExtraction;

import org.apache.commons.math3.complex.Complex;

import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;

/**
 * Interface for multiple plugins to the spectrogram noise
 * reduction system. This is inspired from PAMGuard. www.pamguard.org
 * 
 * @author Doug Gillespie
 * @author Jamie Macaulay
 *
 */
public abstract class SpecNoiseReduction {

	/**
	 * Get a name for the method.
	 * @return String
	 */
	public abstract String getName();
	
	/**
	 * Get a longer description of the method
	 * in HTML format for hover texts in dialogs. 
	 * @return html description
	 */
	public abstract String getDescription();
	
	/**
	 * Run the noise reduction on the data in place. 
	 * @param fftData array of fft data (half fft length)
	 * @return true if ran OK/ 
	 */
	public abstract boolean runNoiseReduction(Complex[][] fftDataUnits);
	
	
	/**
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
	public abstract DynamicSettingsPane getNode();
	
}