package com.jamdev.maven.aipam;

import java.util.ArrayList;
import java.util.List;

import com.jamdev.maven.aipam.annotation.Annotation;
import com.jamdev.maven.aipam.clustering.Params;
import com.jamdev.maven.aipam.clustering.tsne.TSNEParams;
import com.jamdev.maven.aipam.featureExtraction.FeatureExtraction;
import com.jamdev.maven.aipam.featureExtraction.FeatureParams;
import com.jamdev.maven.aipam.layout.ColourArray.ColourArrayType;

/**
 * Parameters for the AIPAM. 
 * @author Jamie Macaulay 
 *
 */
public class AIPamParams implements Cloneable  {

	public static int MAX_CHANNELS = 32; 
	//Audio import settings
	
	/**
	 * The maximum length of a clip in seconds.
	 */
	public double maximumClipLength = 3; 

	/**
	 * The channel of the audio file to use. 
	 */
	public Integer channel = 0; 

	/**
	 * The sample rate to decimate to. 
	 */
	public Integer decimatorSR = 36000; 


	/**
	 * The a folder that contain audio files. 
	 */
	public String audioFolder = "";


	//FFT settings 

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
	
	//playback settings
	
	/**
	 * The playback volume. 
	 */
	public double volume = 50;  //0->100
	
	//Annotation settings
	
	/**
	 * The output annotation folder. This folder will hold all output data clips and automatically save settings. 
	 */
	public String outPutAnnotationFolder = "";  
	
	/**
	 * List of current annotations. These can be loaded 
	 */
	public List<Annotation> annotations = new ArrayList<Annotation>();
	

	//Clustering algorithm 

	/**
	 * The parameters of the clustering algorithm used
	 */
	public Params clusterParams = new TSNEParams();
	
	//Feature extraction parameters. 
	
	/**
	 * Params for feature extraction
	 */
	public FeatureParams featureParams = new FeatureParams(); 
	
	
	//General parameters
	
	/**
	 * True to show user prompts. Can get annoying if you know how to use SOudnSort so can be disabled. 
	 */
	public boolean showUserPrompts = true; 


	@Override
	public AIPamParams clone() {
		//deep clone
		try {
			 AIPamParams cloneParams = (AIPamParams) super.clone();
			 cloneParams.clusterParams= clusterParams.clone(); 
			return cloneParams;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}





}
