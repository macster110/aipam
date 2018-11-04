package com.jamdev.maven.aipam.utils;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import com.jamdev.maven.clips.ClipWave;

/**
 * Bespoke spectrogram class inspired by DeepLearning4j Spectrogram class. 
 * 
 * @author Jamie Macaulay
 *
 */
public class Spectrogram {

	/**
	 * The wave to calculate a spectrogram for. 
	 */
	private ClipWave wave;

	private int fftSampleSize =1024; 

	private int overlapFactor = 512;

	private int numFrames;

	private int framesPerSecond;

	private double[][] absoluteSpectrogram;

	private double[][] spectrogram;

	private int numFrequencyUnit;

	private double frequencyBinSize;

	private double frequencyRange; 

	public Spectrogram(ClipWave wave, int fftLength, int fftHop) {
		this.wave=wave; 
		this.fftSampleSize=fftLength;
		this.overlapFactor=fftHop; 
		this.buildSpectrogram();
	}

	/**
	 * Get the absolute spectrogram. 
	 * @return the absolute spectrogram
	 */
	public double[][] getAbsoluteSpectrogram() {
		return absoluteSpectrogram;
	}

	/**
	 * Get the normalized spectrogram. 
	 * @return the normalized spectrogram. 
	 */
	public double[][] getSpectrogram() {
		return spectrogram;
	}


	private void buildSpectrogram() {

		int[] amplitudes = wave.getSampleAmplitudes();
		int numSamples = amplitudes.length;

		int pointer = 0;

		// overlapping
		if (overlapFactor > 1) {

			int numOverlappedSamples = numSamples * overlapFactor;
			int backSamples = fftSampleSize * (overlapFactor - 1) / overlapFactor;
			int fftSampleSize_1 = fftSampleSize - 1;
			int[] overlapAmp = new int[numOverlappedSamples];
			pointer = 0;
			for (int i = 0; i < amplitudes.length; i++) {
				overlapAmp[pointer++] = amplitudes[i];
				if (pointer % fftSampleSize == fftSampleSize_1) {
					// overlap
					i -= backSamples;
				}
			}
			numSamples = numOverlappedSamples;
			amplitudes = overlapAmp;
		}

		numFrames = numSamples / fftSampleSize;
		framesPerSecond = (int) (numFrames / wave.getLengthInSeconds());

		// set signals for fft
		WindowFunction window = new WindowFunction();
		window.setWindowType(WindowFunction.HAMMING);
		double[] win = window.generate(fftSampleSize);

		double[][] signals = new double[numFrames][];
		for (int f = 0; f < numFrames; f++) {
			signals[f] = new double[fftSampleSize];
			int startSample = f * fftSampleSize;
			for (int n = 0; n < fftSampleSize; n++) {
				signals[f][n] = amplitudes[startSample + n] * win[n];
			}
		}
		
		absoluteSpectrogram = new double[numFrames][];
	    // for each frame in signals, do fft on it
	    FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
	    Complex[] specData;
	    for (int i = 0; i < numFrames; i++) {
	    	specData = fft.transform(signals[i], TransformType.FORWARD); 
	    	absoluteSpectrogram[i] = new double[specData.length/2];
	        for (int j = 0; j < absoluteSpectrogram[i].length; j++) {
	        	absoluteSpectrogram[i][j] = Math.sqrt(specData[j].getReal() * specData[j].getReal() + specData[j].getImaginary() * specData[j].getImaginary());
	        }
	    }
	 
		if (absoluteSpectrogram.length > 0) {

			numFrequencyUnit = absoluteSpectrogram[0].length;
			frequencyBinSize = (double) wave.getSampleRate() / 2 / numFrequencyUnit; // frequency could be caught within the half of
			// nSamples according to Nyquist theory
			frequencyRange = wave.getSampleRate() / 2;

			// normalization of absoultSpectrogram
			spectrogram = new double[numFrames][numFrequencyUnit];

			// set max and min amplitudes
			double maxAmp = Double.MIN_VALUE;
			double minAmp = Double.MAX_VALUE;
			for (int i = 0; i < numFrames; i++) {
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
			for (int i = 0; i < numFrames; i++) {
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
		}
	}


}