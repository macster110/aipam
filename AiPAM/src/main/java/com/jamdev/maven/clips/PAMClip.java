package com.jamdev.maven.clips;

import java.util.UUID;

import com.jamdev.maven.aipam.utils.DownSampleImpl;

/**
 * A single clip for display on the clip pane. 
 * <p>
 * The clip stores a spectogram image but no raw wav data. 
 * Can be dealing with Gigabyte of clips so cannot store these in memory
 * 
 * @author Jamie Macaulay 
 *
 */
public class PAMClip {
	
	private static int DEFAULT_FFT_LEN =1024; 

	private static int DEFUALT_FFT_HOP = 512; 

	/**
	 * Data for colours for spectrogram is stored as a short.
	 */
	private double[][] spectrogramClip; 
	
	/**
	 * The filename of the clip
	 */
	public String fileName;

	/**
	 * The fingerprint of the wave used in classification
	 */
	private byte[] fingerprint;

	/**
	 * The audio play is stored so the clip can be played. 
	 */
	private AudioPlay audioPlay;

	/**
	 * A unique ID for a clip. 
	 */
	private UUID iD;

	
	public PAMClip(ClipWave wave){
		this(wave , DEFAULT_FFT_LEN, DEFUALT_FFT_HOP); 
	} 
	
	public PAMClip(ClipWave wave, int fftLength, int fftHop){
		spectrogramClip=wave.getSpectrogram(fftLength, fftLength/fftHop).getAbsoluteSpectrogramData();
		
//		spectrogramClip =  DownSampleImpl.largestTriangleThreeBuckets(spectrogramClip, 50);
//		System.out.println("The spectrogram clip is: " +  spectrogramClip.length + " x " +  spectrogramClip[0].length);
	
		fingerprint = wave.getFingerprint(); 
//		System.out.println("The spectorgram size is: " + 
//		spectrogramClip.getAbsoluteSpectrogramData().length + "x" +spectrogramClip.getAbsoluteSpectrogramData()); 
		audioPlay=wave.getAudioPlay(); 
		
		fileName=wave.getFileName(); 
		
		this.iD=UUID.randomUUID(); 
		
		//do not want the raw wave data in memory so wave is not saved
		
		wave = null; //garbage collector probably gets rid of this anyway but makes me feel better. 
	} 

	/**
	 * Get the fingerprint for classification
	 * @return the fingerprint classifcation. 
	 */
	public byte[] getFingerprint() {
		return fingerprint;
	}

	/**
	 * Get the spectrogram data for the clip. 
	 * @return
	 */
	public double[][] getSpectrogram() {
		return spectrogramClip;
	}
	
	/**
	 * Get the audio play. This plays the audio files.
	 * @return the audio play
	 */
	public AudioPlay getAudioPlay() {
		return audioPlay;
	}

	/**
	 * 
	 * Get a unique ID number for the clip. 
	 * @return the ID
	 */
	public UUID getID() {
		return getID();
	}

	

}
