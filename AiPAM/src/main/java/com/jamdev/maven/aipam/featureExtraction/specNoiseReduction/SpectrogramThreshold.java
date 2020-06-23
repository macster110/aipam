package com.jamdev.maven.aipam.featureExtraction.specNoiseReduction;

import com.jamdev.maven.aipam.layout.featureExtraction.ThresholdPane;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.utils.ComplexArray;

/**
 * Threshold a spectrogram to make the image binary.
 * <p>
 * Copied from PAMGuard (www.pamguard.org) 
 * 
 * @author Jamie Macaulay
 *
 */
public class SpectrogramThreshold extends SpecNoiseMethod{


	public static final int OUTPUT_BINARY = 0;
	public static final int OUTPUT_INPUT = 1;
	public static final int OUTPUT_RAW = 2;
	
	/**
	 * The parameters/ 
	 */
	public ThresholdParams thresholdParams = new ThresholdParams();
	
	private double powerThreshold;
		
	/**
	 * FX bits of the dialog. 
	 */
	private ThresholdPane thresholdNodeFX;
	
	public SpectrogramThreshold() {

	}
	
	@Override
	public String getName() {
		return "Thresholding";
	}

	@Override
	public String getDescription() {
		return "<html>A threshold is applied and all data<p>" +
				"falling below that threshold set to 0</html>";
	}

	@Override
	public int getDelay() {
		return 0;
	}
	

	@Override
	public boolean initialise(int channelMap) {
		powerThreshold = Math.pow(10.,thresholdParams.thresholdDB/10.);
		return true;
	}

	@Override
	public boolean runNoiseReduction(ComplexArray fftData) {
		for (int i = 0; i < fftData.length(); i++) {
			if (fftData.magsq(i) < powerThreshold) {
				fftData.set(i,0,0);
			}
			else if (thresholdParams.finalOutput != OUTPUT_INPUT) {
				fftData.set(i,1,0);
			}
		}
		return true;
	}
	
	/**
	 * go through an array of other data, and 
	 * copy data that's in earlyData into thresholdData
	 * if the threhsoldData is > 0;
	 * @param fftData data to pick from (generally raw input fft data to noise process)
	 * @param binaryChoice output from runNoiseReduction()
	 */
	public void pickEarlierData(ComplexArray fftData, ComplexArray complexArray) {
		for (int i = 0; i < fftData.length(); i++) {
			if (complexArray.getReal(i) > 0) {
				complexArray.set(i, fftData.getReal(i), fftData.getImag(i));
			}
		}
	}


	public ThresholdParams getThresholdParams() {
		return thresholdParams;
	}

	@Override
	public DynamicSettingsPane getSettingsPane() {
		if (thresholdNodeFX==null) {
			thresholdNodeFX= new ThresholdPane(this);
		}
		return thresholdNodeFX;
	}
	
	@Override
	public Object getParams(boolean dflt) {
		if (dflt) return new ThresholdParams();
		return thresholdParams; 
	}

	@Override
	public void setParams(Object newParams) {
		this.thresholdParams= (ThresholdParams) newParams; 
		
	}
}
