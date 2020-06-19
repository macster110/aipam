package com.jamdev.maven.aipam.featureExtraction;

import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.utils.Spectrogram;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Feature extraction which implements various noise reduction methods on the spectrogram. 
 * 
 * @author Jamie Macaulay
 *
 */
public class NoiseReductionFeature implements FeatureExtraction {

	@Override
	public String getName() {
		return "Spectrogram Noise Reduction";
	}

	@Override
	public double[][] getFeatures(Spectrogram spectrogram) {

		return null;
	}

	@Override
	public DynamicSettingsPane getSettingsPane() {
		// TODO Auto-generated method stub
		return null;
	}
	
	


}
