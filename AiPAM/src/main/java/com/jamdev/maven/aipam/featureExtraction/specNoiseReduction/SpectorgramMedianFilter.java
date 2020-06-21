package com.jamdev.maven.aipam.featureExtraction.specNoiseReduction;

import com.jamdev.maven.aipam.layout.featureExtraction.MedianFilterPane;
import com.jamdev.maven.aipam.layout.featureExtraction.SpecNoiseNodeFX;
import com.jamdev.maven.aipam.utils.ComplexArray;
import com.jamdev.maven.aipam.utils.MedianFilter;


/**
 * A median filter for reducing spectrogram noise. 
 * <p>
 * Within each spectrogram slice, the median value about each point is taken and subtracted from that point.
 * <p>
 * Almost copied directly from PAMGuard (www.pamguard.org)
 *  
 * @author Doug Gillespie
 * @author Jamie Macaulay
 */
public class SpectorgramMedianFilter extends SpecNoiseMethod {

	public MedianFilterParams medianFilterParams = new MedianFilterParams();
	
	/**
	 * The median filter 
	 */
	MedianFilter medianFilter;
	
	/**
	 * The FX dialog bits
	 */
	private MedianFilterPane medianFilterNodeFX;

	
	public SpectorgramMedianFilter() {
		super();
		medianFilter = new MedianFilter();
	}


	@Override
	public String getName() {
		return "Median Filter";
	}
	
	@Override
	public String getDescription() {
		return "<html>Within each spectrogram slice, the <p>"+
		             "median value about each point is <p>"+
		             "taken and subtracted from that point</html>";
	}

	@Override
	public int getDelay() {
		return 0;
	}
	

	@Override
	public boolean initialise(int channelMap) {
		// don't need to do anything here. 
		return medianFilterParams.filterLength > 0;
	}

	private double[] medData; // temp array for real median filter input
	private double[] filterOut; // temp array for real median filter input

	@Override
	public boolean runNoiseReduction(ComplexArray fftData ) {
		if (medianFilterParams.filterLength <= 0) {
			return false;
		}
		// run the median filter
		
		medData = checkAlloc(medData, fftData.length());
		filterOut = checkAlloc(filterOut, fftData.length());
		
		for (int m = 0; m < fftData.length(); m++) {
			medData[m] = fftData.mag(m);
		}
		medianFilter.medianFilter(medData, filterOut, medianFilterParams.filterLength);
		for (int m = 0; m < fftData.length(); m++) {
			if (filterOut[m] > 0) {
				fftData.internalTimes(m, 1./filterOut[m]);
			}
		}
		return true;
	}
	private double[] checkAlloc(double[] array, int len) {
		if (array == null || array.length != len) {
			array = new double[len];		}
		
		return array;
	}


	public MedianFilterParams getMedianFilterParams() {
		return medianFilterParams;
	}

	@Override
	public SpecNoiseNodeFX getSettingsPane() {
		if (medianFilterNodeFX==null) {
			medianFilterNodeFX= new MedianFilterPane(this); 
		}
		return medianFilterNodeFX;
	}
}
