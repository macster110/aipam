package com.jamdev.maven.aipam.featureExtraction;

import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.utils.Spectrogram;

/**
 * A very simple spectrogram normalisation. 
 * @author Jamie Macaulay 
 *
 */
public class SpectrogramNormalisation implements FeatureExtraction {

	@Override
	public String getName() {
		return "Spectrogram Normalisation";
	}

	@Override
	public double[][] getFeatures(Spectrogram spectrogram) {
		return spectrogram.getNormalisedSpectrogram();
	}

	@Override
	public DynamicSettingsPane getSettingsPane() {
		return null;
	}

}
