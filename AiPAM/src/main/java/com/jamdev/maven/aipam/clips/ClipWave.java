package com.jamdev.maven.aipam.clips;

import com.jamdev.maven.aipam.utils.Spectrogram;

/**
 * Passes audio data to a pamclip. This class can store the audio raw audio data 
 * as it is deleted after the pamclip has grabbed the spectrogram. 
 * 
 * @author Jamie Macaulay 
 *
 */
public interface ClipWave {

	
	/**
	 * Get the data audio play. This plays the audio data
	 */
	public AudioPlay getAudioPlay();

	
	/**
	 * Get the file the audio was loaded from
	 * @return -the audio the file was loaded from. 
	 */
	public String getFileName();

	/**
	 * Get the spectrogram for the clip. 
	 * @param fftLength - the FFT length
	 * @param ffthop - the hop size
	 * @return double[][] array of non normalized fft values
	 */
	public Spectrogram getSpectrogram(int fftLength, int fftHop);

	/**
	 * Get the sample amplitudes in into format 
	 * @return
	 */
	public int[] getSampleAmplitudes();

	/**
	 * Get the length of the file in seconds. 
	 * @return the clip length in seconds
	 */
	public double getLengthInSeconds();

	/**
	 * Get the sample rate. 
	 * @return the sample rate. 
	 */
	public int getSampleRate();

}
