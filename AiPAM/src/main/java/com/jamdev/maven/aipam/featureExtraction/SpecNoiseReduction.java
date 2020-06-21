package com.jamdev.maven.aipam.featureExtraction;

import java.util.ArrayList;

import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.AverageSubtraction;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.KernelSmoothing;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpecNoiseMethod;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpectorgramMedianFilter;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpectrogramThreshold;
import com.jamdev.maven.aipam.layout.featureExtraction.SpectNoisePane;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.utils.Spectrogram;

/**
 * Feature extraction which implements various noise reduction methods on the spectrogram. 
 * <P>
 * The methods are almost a carbon copy from PAMGuard (www.pamguard.org).
 * 
 * @author Jamie Macaulay
 *
 */
public class SpecNoiseReduction implements FeatureExtraction {

	/**
	 * Settings pane for spectrogram noise reduction. 
	 */
	private SpectNoisePane specNoisePane;
	
	/**
	 * The currently implemented spectrogram noise method. 
	 */
	private ArrayList<SpecNoiseMethod> specNoiseMethods; 

	
	public SpecNoiseReduction() {
		specNoiseMethods = new ArrayList<SpecNoiseMethod>(); 
		
		specNoiseMethods.add(new AverageSubtraction()); 
		specNoiseMethods.add(new KernelSmoothing()); 
		specNoiseMethods.add(new SpectorgramMedianFilter()); 
		specNoiseMethods.add(new SpectrogramThreshold()); 
	}
	
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
		if (this.specNoisePane ==null) {
			return specNoisePane = new SpectNoisePane(this);
		}
		return specNoisePane;
	}

	/**
	 * Get the spectrogram noise reduction methods. 
	 * @return the spectrogram noise reduction methods. 
	 */
	public ArrayList<SpecNoiseMethod>  getSpecNoiseMethods() {
		return this.specNoiseMethods;
	}
	
	


}
