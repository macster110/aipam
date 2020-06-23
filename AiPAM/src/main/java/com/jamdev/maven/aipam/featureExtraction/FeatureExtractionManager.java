package com.jamdev.maven.aipam.featureExtraction;

import java.util.ArrayList;

import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpecNoiseReduction;

/**
 * 	Manages the extraction of feature from clips. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class FeatureExtractionManager {

	/**
	 * Define the different types of feature extraction
	 * @author Jamie Macaulay
	 *
	 */
	enum FeatureExtractionType {
		NONE,
		SPEC_SMOOTHING,
	}


	public ArrayList<FeatureExtraction> featureExtractor = new ArrayList<FeatureExtraction>();
	
	/**
	 * Reference to the aiPamControl. 
	 */
	private AiPamController aiPamControl; 

	/***
	 * Constructor for Feature Extraction Manager
	 */
	public FeatureExtractionManager(AiPamController aiPamControl) {
		this.aiPamControl=aiPamControl; 
		featureExtractor.add(new NoFeatureExtraction()); 
		featureExtractor.add(new SpectrogramNormalisation()); 
		featureExtractor.add(new SpecNoiseReduction(this)); 
		
		aiPamControl.getParams().featureParams = new FeatureParams(); 
		aiPamControl.getParams().featureParams .featureParams = new Object[featureExtractor.size()]; 
		for (int i=0; i<featureExtractor.size(); i++) {
			aiPamControl.getParams().featureParams .featureParams[i] = featureExtractor.get(i).getDefaultParams(); 
		}
	}

	public void loadSettings() {
		//TODO - when loading settings i permitted then each of the feature extractors should have it's setting supdated. 
	}
	
	/**
	 * Notify that a settings has changed and the preview clip may need updated. 
	 */
	public void notifySelectedClipChange() {
		this.aiPamControl.updateMessageListeners(AiPamController.FEATURES_CHANGED, null);
	}

	/**
	 * 	Get all possible feature extractors. 
	 * @return all implemented feature extractors. 
	 */
	public ArrayList<FeatureExtraction> getFeatureExtractors(){
		return featureExtractor;
	}

	/**
	 * 	Get the currently selected feature extractor. 
	 * @return the currently selected feature extractor. 
	 */
	public FeatureExtraction getCurrentFeatureExtractor(){
		return featureExtractor.get(getFeatureParams().currentFeatureIndex); 
	}

	
	/**
	 * The feature extraction parameters. 
	 * @return the feature extraction parameters. 
	 */
	public FeatureParams getFeatureParams() {
		return aiPamControl.getParams().featureParams;
	}


}
