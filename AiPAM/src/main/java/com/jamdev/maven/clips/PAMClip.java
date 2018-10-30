package com.jamdev.maven.clips;

import java.io.File;

/**
 * A single clip for display on the clip pane. 
 * <p>
 * The clip stores a spectogram image but no raw wav data. 
 * Can be dealing with Gigabytes of clips so cannot store these in memory
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
	 * The audio play is stored so the clip can be played. 
	 */
	private AudioPlay audioPlay;

	/**
	 * The file ID
	 */
	private String iD;
	
	/**
	 * The position defined by the cluster algorithm
	 */
	private double[] clusterPoint; 
		
	public PAMClip(ClipWave wave){
		this(wave , DEFAULT_FFT_LEN, DEFUALT_FFT_HOP); 
	} 
	
	public PAMClip(ClipWave wave, int fftLength, int fftHop){
		spectrogramClip=wave.getSpectrogram(fftLength, fftLength/fftHop).getAbsoluteSpectrogram();
			
		//spectrogramClip =  DownSampleImpl.largestTriangleThreeBuckets(spectrogramClip, 50);
//		System.out.println("The spectrogram clip is: " +  spectrogramClip.length + " x " +  spectrogramClip[0].length);
	
//		System.out.println("The spectorgram size is: " + 
//		spectrogramClip.getAbsoluteSpectrogramData().length + "x" +spectrogramClip.getAbsoluteSpectrogramData()); 
		audioPlay=wave.getAudioPlay(); 
		
		fileName=wave.getFileName(); 
		
		//use the file name as the ID because this allows easy exporting of data and alos allows for 
		//clustering algorithm to be reoloaded. Condition of program is therefore that no two file names 
		//can be equal. 
		iD=new File(wave.getFileName()).getName(); 
		
		//do not want the raw wave data in memory so wave is not saved
		
		wave = null; //garbage collector probably gets rid of this anyway but makes me feel better. 
	} 
	
	
	@SuppressWarnings("unused")
	private double[][] clipSpectrogram(double[][] clip, int binsFreq) {
		double[][] newSpec = new double[clip.length][]; 
		for (int i=0; i<clip.length; i++) {
			double[] newSpecLine= new double[binsFreq]; 
			for (int j=0; j<binsFreq; j++) {
				newSpecLine[j]=clip[i][j]; 
			}
			newSpec[i]=newSpecLine; 
		}
		return newSpec; 
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
	public String getID() {
		return iD; 
	}
	
	/**
	 * Get the cluster point for the clip after having been 
	 * @return the cluster point in x,y and maybe z or even more. Can be null if no clustering. 
	 */
	public double[] getClusterPoint() {
		return clusterPoint; 
	}

	/**
	 * Set the cluster point.
	 * @param clusterPoint - the cluster point. 
	 */
	public void setClusterPoint(double[] clusterPoints) {
		this.clusterPoint=clusterPoints; 
	}

	

}
