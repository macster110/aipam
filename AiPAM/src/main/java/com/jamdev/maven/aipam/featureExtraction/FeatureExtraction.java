package com.jamdev.maven.aipam.featureExtraction;

import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.utils.ClipSpectrogram;

import javafx.scene.Node;

/**
 * Interface for feature extraction methods. 
 * 
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
	 * Get the features for input into the algorithm. 
	 * @param spectrogram - the spectrogram data to run feature extraction on. 
	 * @return array containing extracted features. 
	 */
	public double[][] getFeatures(ClipSpectrogram spectrogram);

	/**
	 * Get a dynamic settings pane. 
	 * @return the dynamic settings pane. 
	 */
	public DynamicSettingsPane getSettingsPane();

	/**
	 * Get the default parameters class for the feature extraction params. 
	 * @return the default parameters class
	 */
	public Object getDefaultParams(); 
	
	/**
	 * True to plot these features as log scale. 
	 * @return true to plot the features as log scale.
	 */
	public boolean logPlot(); 

	
}
