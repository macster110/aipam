package com.jamdev.maven.aipam;

import java.util.ArrayList;
import java.util.List;

import com.jamdev.maven.aipam.annotation.Annotation;
import com.jamdev.maven.aipam.clips.SpecParams;
import com.jamdev.maven.aipam.clips.datetime.DateTimeParams;
import com.jamdev.maven.aipam.clips.datetime.StandardDateTimeParams;
import com.jamdev.maven.aipam.clustering.Params;
import com.jamdev.maven.aipam.clustering.tsne.TSNEParams;
import com.jamdev.maven.aipam.featureExtraction.FeatureParams;

/**
 * Parameters for the AIPAM. 
 * @author Jamie Macaulay 
 *
 */
public class AIPamParams implements Cloneable  {

	/**
	 * The maximum number of allowed channels. 
	 */
	public static int MAX_CHANNELS = 32; 
	
	//Audio import settings
	
	/**
	 * The maximum length of a clip in seconds. -1 indicates that clips should not 
	 * be trimmed. 
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
	
	/**
	 * The maximum number of clips per page. 
	 */
	public int maxPageClips = 2000; 
	
	/**
	 * Paramters for the datetime
	 */
	public DateTimeParams datetimeSettings = new StandardDateTimeParams(); 


	//FFT settings
	
	/**
	 * Parameters for generating a spectrogram. Includes information such as FFT length, FFT hop, 
	 * and information for colour maps. 
	 */
	public SpecParams spectrogramParams = new SpecParams(); 
	
	
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
	public boolean showUserPrompts = false;

	/**
	 * Show features on clip pane instead of spectrograms 
	 */
	public boolean showFeatures = false; 

	/**
	 * Show collapsed menu pane. This shows only the icons, not the text of each menu. 
	 */
	public boolean collapseMenu = false; 

	@Override
	public AIPamParams clone() {
		//deep clone
		try {
			 AIPamParams cloneParams = (AIPamParams) super.clone();
			 cloneParams.clusterParams= clusterParams.clone(); 
			 cloneParams.spectrogramParams= spectrogramParams.clone(); 

			return cloneParams;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}





}
