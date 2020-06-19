package com.jamdev.maven.aipam.featureExtraction;

import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.utils.Spectrogram;


/**
 * Feature extraction which extracts no features and simply sends the raw data to 
 * 
 * @author Jamie Macaulay 
 *
 */
public class NoFeatureExtraction implements FeatureExtraction {
	
	

	@Override
	public double[][] getFeatures(Spectrogram spectrogram) {
		return spectrogram.getAbsoluteSpectrogram();
	}

	@Override
	public String getName() {
		return "None";
	}

	@Override
	public DynamicSettingsPane getSettingsPane() {
		return null;
	}


}