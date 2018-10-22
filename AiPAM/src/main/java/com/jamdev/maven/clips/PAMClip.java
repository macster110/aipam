package com.jamdev.maven.clips;


import org.datavec.audio.Wave;
import org.datavec.audio.extension.Spectrogram;

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
	
	/**
	 * The default fft length
	 */
	private static int DEFAULT_FFT=1024; 
	
	/**
	 * The default FFT hop; 
	 */
	private static int DEFAULT_FFT_HOP=512; 

	
	/**
	 * Data for colours for spectrogram is stored as a short.
	 */
	private Spectrogram spectrogramClip; 
	
	/**
	 * The filename of the clip
	 */
	public String fileName;

	/**
	 * The fingerprint of the wave used in classification
	 */
	private byte[] fingerprint;



	public PAMClip(Wave wave){
		spectrogramClip=wave.getSpectrogram(); 
		fingerprint = wave.getFingerprint(); 
		System.out.println("The spectorgram size is: " + 
		spectrogramClip.getAbsoluteSpectrogramData().length + "x" +spectrogramClip.getAbsoluteSpectrogramData()); 
		//do not want the raw wave data in memory so wave is not saved
	} 

	/**
	 * Get the fingerprint for classification
	 * @return the fingerprint classifcation. 
	 */
	public byte[] getFingerprint() {
		return fingerprint;
	}

	

}
