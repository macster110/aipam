package com.jamdev.maven.aipam.clips;

import com.jamdev.maven.aipam.utils.ClipSpectrogram;

/**
 * Passes audio data to a PAMClip. This class can store the audio raw audio data 
 * as it is deleted after the PAMClip has grabbed the spectrogram. 
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
	 * Get the start date time of the audio file in milliseconds date number. 
	 */
	public long getTimeMillis();

	
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
	public ClipSpectrogram getSpectrogram(int fftLength, int fftHop);

	/**
	 * Get the sample amplitudes in bit format 
	 * @return the sample amplitude in bits. 
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
