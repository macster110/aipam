package com.jamdev.maven.aipam.utils;

import org.jamdev.jpamutils.spectrogram.ComplexArray;
import org.jamdev.jpamutils.wavFiles.AudioData;

import com.jamdev.maven.aipam.clips.WavClipWave;

/**
 * Bespoke spectrogram class inspired by DeepLearning4j Spectrogram class. 
 * 
 * @author Jamie Macaulay
 *
 */
public class ClipSpectrogram extends org.jamdev.jpamutils.spectrogram.Spectrogram {


	public ClipSpectrogram(AudioData wave, int fftLength, int fftHop) {
		super(wave, fftLength, fftHop);
		//want to save data in a compressed short 
	}

	public ClipSpectrogram(WavClipWave wavClipWave, int fftLength, int fftHop) {
		super(new AudioData(wavClipWave.getSampleAmplitudes(), wavClipWave.getSampleRate()), fftLength, fftHop);
		
		System.out.println("Create spectrogram: " + wavClipWave.getSampleAmplitudes()[0]);
	}

	/**
	 * Calculates the normalized spectrogram. Note this is not saved within the Spectrogram 
	 * object and so is recalculated every time this function is called. 
	 * @return the normalized spectrogram. 
	 */
	public double[][] getNormalisedSpectrogram() {
		return buildNormalizedSpectram(super.getFFTData());
	}

	/**
	 * Build the normalised spectrogram. 
	 * @return the normalised spectrogram. 
	 */
	public static double[][] buildNormalizedSpectram(ComplexArray[] complexSpectrogram) {
		double[][] absoluteSpectrogram = buildAbsoluteSpectram(complexSpectrogram);

		if (absoluteSpectrogram!=null && absoluteSpectrogram.length > 0) {

			int numFrequencyUnit = absoluteSpectrogram[0].length;
			//			double frequencyBinSize = (double) sR / 2.0 / numFrequencyUnit; // frequency is half of

			// normalization of absoultSpectrogram
			double[][]  spectrogram = new double[complexSpectrogram.length][numFrequencyUnit];

			// set max and min amplitudes
			double maxAmp = Double.MIN_VALUE;
			double minAmp = Double.MAX_VALUE;
			for (int i = 0; i < complexSpectrogram.length; i++) {
				for (int j = 0; j < numFrequencyUnit; j++) {
					if (absoluteSpectrogram[i][j] > maxAmp) {
						maxAmp = absoluteSpectrogram[i][j];
					}
					else if (absoluteSpectrogram[i][j] < minAmp) {
						minAmp = absoluteSpectrogram[i][j];
					}
				}
			}

			// normalization
			// avoiding divided by zero
			double minValidAmp = 0.00000000001F;
			if (minAmp == 0) {
				minAmp = minValidAmp;
			}

			double diff = Math.log10(maxAmp / minAmp); // perceptual difference
			for (int i = 0; i < complexSpectrogram.length; i++) {
				for (int j = 0; j < numFrequencyUnit; j++) {
					if (absoluteSpectrogram[i][j] < minValidAmp) {
						spectrogram[i][j] = 0;
					}
					else {
						spectrogram[i][j] = (Math.log10(absoluteSpectrogram[i][j] / minAmp)) / diff;
						// System.out.println(spectrogram[i][j]);
					}
				}
			}
			return spectrogram;  
		}
		else return null; 	
	}


}