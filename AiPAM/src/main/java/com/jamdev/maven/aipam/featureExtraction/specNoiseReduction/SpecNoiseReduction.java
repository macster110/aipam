package com.jamdev.maven.aipam.featureExtraction.specNoiseReduction;


import java.util.ArrayList;

import com.jamdev.maven.aipam.featureExtraction.FeatureExtraction;
import com.jamdev.maven.aipam.featureExtraction.FeatureExtractionManager;
import com.jamdev.maven.aipam.layout.featureExtraction.SpectNoisePane;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.utils.ComplexArray;
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

	private SpecNoiseReductionParams specNoiseReductionParams;

	private SpectrogramThreshold thresholdMethod;

	private FeatureExtractionManager featureExtractionManager; 

	/**
	 * Get the feature extraction manager. 
	 * @return the feature extraction manager
	 */
	public FeatureExtractionManager getFeatureExtractionManager() {
		return featureExtractionManager;
	}

	public SpecNoiseReduction(FeatureExtractionManager featureExtractionManager) {
		this.featureExtractionManager=featureExtractionManager; 
		
		specNoiseMethods = new ArrayList<SpecNoiseMethod>(); 

		specNoiseMethods.add(new AverageSubtraction()); 
		specNoiseMethods.add(new KernelSmoothing()); 
		specNoiseMethods.add(new SpectorgramMedianFilter()); 
		specNoiseMethods.add(thresholdMethod = new SpectrogramThreshold()); 

		//set up the paramters class. 
		specNoiseReductionParams = getDefaultParams(); 
		
		//load param setting here. 

	}

	@Override
	public String getName() {
		return "Spectrogram Noise Reduction";
	}

	@Override
	public double[][] getFeatures(Spectrogram spectrogram) {

		System.out.println("Calc new features: " + thresholdMethod.thresholdParams.thresholdDB); 
		
		for (int i = 0; i < specNoiseMethods.size(); i++) {
			specNoiseMethods.get(i).initialise(1); 
		}


		ComplexArray[] complexArrayOut = new ComplexArray[spectrogram.getFFTData().length]; 

		for (int j=0; j<spectrogram.getFFTData().length; j++) {

			ComplexArray newFFTUnit =  spectrogram.getFFTData()[j].clone();

			for (int i = 0; i < specNoiseMethods.size(); i++) {
				if (specNoiseReductionParams.runMethod[i]) {
					specNoiseMethods.get(i).runNoiseReduction(newFFTUnit);
				}
			}

			ThresholdParams p = (ThresholdParams) thresholdMethod.getParams(false);
			if (p.finalOutput == SpectrogramThreshold.OUTPUT_RAW) {
				thresholdMethod.pickEarlierData(spectrogram.getFFTData()[j], newFFTUnit);
			}

			// and output the data unit. 
			complexArrayOut[j] = newFFTUnit;
		}


		//get the normalised spectrogram for the feature output. 
		return Spectrogram.buildNormalizedSpectram(complexArrayOut); 
	}

	@Override
	public DynamicSettingsPane getSettingsPane() {
		if (this.specNoisePane ==null) {
			specNoisePane = new SpectNoisePane(this);
			specNoisePane.setParams(specNoiseReductionParams);

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

	@Override
	public SpecNoiseReductionParams getDefaultParams() {
		specNoiseReductionParams = new SpecNoiseReductionParams(); 

		specNoiseReductionParams.specNoiseParams = new Object[specNoiseMethods.size()]; 
		specNoiseReductionParams.runMethod = new boolean[specNoiseMethods.size()]; 
		for (int i=0; i<specNoiseMethods.size(); i++) {
			specNoiseReductionParams.runMethod[i] = true;
			specNoiseReductionParams.specNoiseParams[i] = specNoiseMethods.get(i).getParams(true); 
		}
		return specNoiseReductionParams;
	}

	@Override
	public boolean logPlot() {
		return false;
	}

	/**
	 * Get the parameter class for spectrogram noise reduction.
	 * @param specNoiseReductionParams the new parameter class. 
	 */
	public SpecNoiseReductionParams getParams() {
		return specNoiseReductionParams;
	}
	
	/**
	 * Set the parameter class for spectrogram noise reduction.
	 * @param specNoiseReductionParams the new parameter class. 
	 */
	public void setParams(SpecNoiseReductionParams specNoiseReductionParams) {
		this.specNoiseReductionParams= specNoiseReductionParams;
		for (int i=0; i<specNoiseMethods.size(); i++) {
			specNoiseMethods.get(i).setParams(specNoiseReductionParams.specNoiseParams[i]); 
		}

	}





}
