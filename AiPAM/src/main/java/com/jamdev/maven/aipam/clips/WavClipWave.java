package com.jamdev.maven.aipam.clips;

import java.io.File;

import org.jamdev.jpamutils.wavFiles.WavFile;

import com.jamdev.maven.aipam.utils.ClipSpectrogram;

public class WavClipWave implements ClipWave {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Plays the audio file without requiring the raw audio data to be stored in memory in wave
	 */
	private AudioPlay audioPlay;
	
	/**
	 * Get spectrogram
	 */
	private ClipSpectrogram spectrogram;
	
	/**
	 * The wav file class. 
	 */
	private File wavFile;
	
	/**
	 * The raw .wav file data in bin format e.g. 16 bit is +32000
	 */
	private short[] data;
	
	/**
	 * The samplerate
	 */
	private int sampleRate;
	
	/**
	 * The time in millis datenum
	 */
	private long date;
	
	/**
	 * The total number of sample sin the file. 
	 */
	private long numSamples; 
	
	/**
	 * Constructor for the wav file clip.
	 * @param wavFile
	 * @param data
	 * @param datalength
	 * @param sampleRate
	 * @param date
	 */
	public WavClipWave(File wavFile, short[] data, int sampleRate, long date, long numSamples) {
		this.wavFile= wavFile;
		this.data = data; 
		this.sampleRate = sampleRate;
		this.date= date; 
		this.numSamples = numSamples; 
		audioPlay = new StandardAudioPlayJFX(wavFile); 
	}
	
	/**
	 * Get the data audio play 
	 */
	public AudioPlay getAudioPlay() {
		return audioPlay;
	}

	
	/**
	 * Get the file. 
	 * @return the file; 
	 */
	public File getFile() {
		return wavFile; 
	}


	/**
	 * Get the file the audio was laoded from
	 * @return -the audio the file was loaded from. 
	 */
	public String getFileName() {
		return wavFile.getAbsolutePath();
	}

	@Override
	public ClipSpectrogram getSpectrogram(int fftLength, int fftHop) {
		return new ClipSpectrogram(this, fftLength, fftHop);
	}


	@Override
	public short[] getSampleAmplitudes() {
		return data;
	}

	@Override
	public double getLengthInSeconds() {
		//System.out.println("Number of samples: " +  numSamples +  " sR: " + getSampleRate()); 
		return (double) numSamples/(double)  getSampleRate();
	}

	@Override
	public int getSampleRate() {
		return sampleRate;
	}

	@Override
	public long getTimeMillis() {
		return date;
	}

	@Override
	public void setSampleAmplitudes(short[] amplitudes) {
		this.data=amplitudes; 
	}

	
	
}
