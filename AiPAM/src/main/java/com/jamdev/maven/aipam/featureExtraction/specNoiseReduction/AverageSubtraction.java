package com.jamdev.maven.aipam.featureExtraction.specNoiseReduction;

import org.jamdev.jpamutils.spectrogram.ComplexArray;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.layout.featureExtraction.AverageSubtractionPane;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;

/**
 * A decaying average spectrogram is computed and subtracted from the current spectrogram value.
 * <p>
 * Almost a carbon copy from PAMGuard (www.pamguard.org)
 * 
 * @author Doug Gillespie
 * @author Jamie Macaulay
 *
 */
public class AverageSubtraction extends SpecNoiseMethod {

	/**
	 * Average subtraction parameters
	 */
	public AverageSubtractionParameters averageSubtractionParameters = new AverageSubtractionParameters();

	/*
	 * update constants for stored and new data.
	 */
	private double newConstant, oldConstant;

	private static final int runInSlices = 10;

	private static final double runInScale = 2;

	int totalSlices;

	/**
	 * Storage of data for each channel
	 */
	private double[][] channelStorage;

	/**
	 * The FX dialog pane. 
	 */
	private AverageSubtractionPane averageSubtractionNode;

	public AverageSubtraction() {
		super();
	}


	@Override
	public String getName() {
		return "Average Subtraction";
	}

	@Override
	public String getDescription() {
		return "<html>A decaying average spectrogram <p>is computed" +
		"and subtracted from <p>the current spectrogram value</html>";
	}

	@Override
	public int getDelay() {
		return 0;
	}


	@Override
	public boolean initialise(int channelMap) {
		channelStorage = new double[AIPamParams.MAX_CHANNELS][];

		totalSlices = 0;
		// can only allocate individual channel data when we know 
		// how long the fft data are !

		newConstant = averageSubtractionParameters.updateConstant;
		oldConstant = 1. - newConstant;

		return true;
	}
	

	@Override
	public boolean runNoiseReduction(ComplexArray fftData) {

		int len = fftData.length();
//		int iChan = PamUtils.getSingleChannel(fftDataUnit.getChannelBitmap());
		int iChan = 0; //for now, SoundSort only support 1 channel data.
		double[] channelData = channelStorage[iChan];
		double dum;
		// the first time this get's called, the channelData will all 
		// be zero, so it won't be possible to update unless a copy
		// of the new data is put in there as a starting reference
		double scale;
		if (channelData == null || channelData.length != len) {
			channelData = channelStorage[iChan] = new double[len];
			totalSlices = 0;
		}
		/*
		 * the log10(fftData) are divided by an additional 
		 * factor of two since it's the log of the squared data and
		 * it's divided off the unsquared data. 
		 */
		if (totalSlices++ < runInSlices) {
			for (int i = 0; i < len; i++) {
				if (fftData.isNaN(i)) {
					continue;
				}
				if ((dum = fftData.magsq(i)) == 0) {
					continue;
				}
				channelData[i] += Math.log10(dum)/2. / runInSlices * runInScale;
				scale = Math.pow(10., channelData[i]);
				fftData.internalTimes(i, 1./runInSlices);
			}	
		}
		else {
			for (int i = 0; i < len; i++) {
				if (fftData.isNaN(i)) {
					continue;
				}
				if ((dum = fftData.magsq(i)) == 0) {
					continue;
				}
				scale = Math.pow(10., channelData[i]);
				channelData[i] *= oldConstant;
				channelData[i] += newConstant * Math.log10(dum)/2;
				if (channelData[i] == Double.POSITIVE_INFINITY || Double.isInfinite(channelData[i])) {
					System.out.println("Infinite channel data");
				}
				fftData.internalTimes(i, 1./scale);
			}
		}


		return true;
	}


	public AverageSubtractionParameters getAverageSubtractionParameters() {
		return averageSubtractionParameters;
	}


	@Override
	public DynamicSettingsPane getSettingsPane() {
		if (averageSubtractionNode==null) {
			averageSubtractionNode = new AverageSubtractionPane(this); 
		}
		return averageSubtractionNode;
	}


	@Override
	public Object getParams(boolean dflt) {
		if (dflt) return new AverageSubtractionParameters();
		return averageSubtractionParameters;
	}


	@Override
	public void setParams(Object newParams) {
		this.averageSubtractionParameters=(AverageSubtractionParameters) newParams; 
		
	}



}
