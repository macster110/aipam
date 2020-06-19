package com.jamdev.maven.aipam.featureExtraction;

import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.utils.Spectrogram;

import javafx.scene.Node;

/**
 * Interface for feature extraction
 * @author Jamie Macaulay
 *
 */
public interface FeatureExtraction {
	
	/**
	 * Get the name for the feature extraction. 
	 * @return the feature extraction. 
	 */
	public String getName(); 

	/**
	 * Get the features for input into the algorothm 
	 * @param spectrogram - the features
	 * @return
	 */
	public double[][] getFeatures(Spectrogram spectrogram);

	/**
	 * Get a dynamic settings pane. 
	 * @return the dynamic settings pane. 
	 */
	public DynamicSettingsPane getSettingsPane(); 

	
}
