package com.jamdev.maven.aipam;

import com.jamdev.maven.aipam.clustering.ClusterParams;
import com.jamdev.maven.aipam.clustering.TSNEParams;
import com.jamdev.maven.aipam.layout.ColourArray.ColourArrayType;

/**
 * Parameters for the AIPAM. 
 * @author Jamie Macaulay 
 *
 */
public class AIPamParams {
	
	//Audio import settings
	
	/**
	 * The maximum length of a clip in seconds.
	 */
	public double maximumClipLength = 3; 
	
	/**
	 * The channel of the audio file to use. 
	 */
	public int channel = 0; 
	
	/**
	 * The a folder that contain audio files. 
	 */
	public String audioFolder = "";
	
	
	//FFT settings 
	
	/**
	 * The fft length for clip spectrograms. 
	 */
	public int fttLength = 512; 
	
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

	
	//Clustering algorithm 
	
	/**
	 * The parameters of the clustering algorithm used
	 */
	public ClusterParams clusterParams = new TSNEParams(); 
	
	
	


}
