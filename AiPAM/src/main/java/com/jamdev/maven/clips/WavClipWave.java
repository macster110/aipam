package com.jamdev.maven.clips;

import java.io.File;

import com.jamdev.maven.aipam.utils.Spectrogram;
import com.jamdev.maven.aipam.utils.WavFile;

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
	private Spectrogram spectrogram;
	
	/**
	 * The wav file class. 
	 */
	private WavFile wavFile;
	
	/**
	 * The raw .wav file data in bin format e.g. 16 bit is +32000
	 */
	private int[] data;
	
	/**
	 * The samplerate
	 */
	private int sampleRate; 
	
	
	public WavClipWave(WavFile wavFile, int[] data, int sampleRate) {
		this.wavFile= wavFile;
		this.data = data; 
		this.sampleRate = sampleRate;
		audioPlay = new StandardAudioPlay(wavFile.getFile()); 
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
		return wavFile.getFile(); 
	}


	/**
	 * Get the file the audio was laoded from
	 * @return -the audio the file was loaded from. 
	 */
	public String getFileName() {
		return wavFile.getFile().getAbsolutePath();
	}

	@Override
	public Spectrogram getSpectrogram(int fftLength, int fftHop) {
		return new Spectrogram(this, fftLength, fftHop);
	}


	@Override
	public int[] getSampleAmplitudes() {
		return data;
	}

	@Override
	public double getLengthInSeconds() {
		return data.length/(double)  getSampleRate();
	}

	@Override
	public int getSampleRate() {
		return sampleRate;
	}

	
	
}
