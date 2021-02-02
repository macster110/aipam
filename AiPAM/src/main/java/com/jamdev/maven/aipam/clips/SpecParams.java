package com.jamdev.maven.aipam.clips;

import com.jamdev.maven.aipam.layout.ColourArray.ColourArrayType;

/**
 * Parameters for creating a spectrogram e.g. the FFT length, FFT hop and colour information for
 * display.  
 * 
 * @author Jamie Macaulay 
 *
 */
public class SpecParams implements Cloneable  {

	/**
	 * The fft length for clip spectrograms. 
	 */
	public int fftLength = 512; 

	/**
	 * The fft hop for clip spectrograms
	 */
	public int fftHop = 256;

	/**
	 * The colour array type for the clip spectrograms. 
	 */
	public ColourArrayType spectrogramColour = ColourArrayType.GREY; 

	/**
	 * The colour limits for the spectrogram. Closer limits usually increase contrast.
	 */
	public double[] colourLims = new double[]{20, 100};  
	//	public double[] colourLims = new double[]{0, 100};  
	
	@Override
	public SpecParams clone() {
		//deep clone
		try {
			SpecParams cloneParams = (SpecParams) super.clone();
			return cloneParams;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
