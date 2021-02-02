package com.jamdev.maven.aipam.clips;

import org.apache.commons.io.FilenameUtils;

import com.jamdev.maven.aipam.annotation.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.jamdev.maven.aipam.utils.ClipSpectrogram;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * 
 * A single clip for display on the clip pane. 
 * <p>
 * The clip stores a spectrogram image but no raw .wav data. 
 * Can be dealing with GB of clips so cannot also store raw .wav 
 * data in memory.
 * 
 * @author Jamie Macaulay 
 *
 */
public class PAMClip implements Comparable<PAMClip> {

//	private static int DEFAULT_FFT_LEN =1024; 
//
//	private static int DEFUALT_FFT_HOP = 512; 

	/**
	 * Data for colours for spectrogram is stored as a short.
	 */
	private ClipSpectrogram spectrogramClip; 


	//	/**
	//	 * The file ID
	//	 */
	//	private StringProperty iD;

	/**
	 * The position defined by the cluster algorithm
	 */
	private double[] clusterPoint;

	/**
	 * The grid ID. 
	 */
	private IntegerProperty gridID;

	/**
	 * Object property for the annotation. 
	 */
	private ObjectProperty<Annotation> annotation;

	//	/**
	//	 * The static grid ID. 
	//	 */
	//	private SimpleIntegerProperty staticGridID; 

	/**
	 * The frequency limits in Hz. 
	 */
	private double[] freqLims = new double[2];

	/**
	 * The date time in milliseconds
	 */
	private long timeMillis = 0;

	/**
	 * The clip wave - holds the raw data of the data. 
	 */
	private ClipWave clipWave;


	public PAMClip(ClipWave wave, int gridID){
		
		//		this.staticGridID = new SimpleIntegerProperty(gridID);  
		this.clipWave = wave; 
		
		this.gridID=new SimpleIntegerProperty(gridID); 

		freqLims[0] = 0;
		freqLims[1] = wave.getSampleRate()/2.;
		
		//create the annotation property
		annotation = new SimpleObjectProperty<Annotation>(); 

		//set the time in  milliseconds - VERY important for allowing clips to be 
		//matched to files...
		timeMillis = wave.getTimeMillis();
	} 


	/**
	 * Clear the audio data from the clip. It will 
	 * need to be reloaded from the file. 
	 */
	public void clearAudioData() {
		clipWave.setSampleAmplitudes(null); 
	}

	/**
	 * Set the audio data for a clip. 
	 * @param spectrogramClip
	 */
	public void setAudioData(ClipWave wave) {
		//System.out.println("Set sample amplitudes"); 
		if (wave==null) {
			this.clipWave.setSampleAmplitudes(null); 
		}
		else {
			this.clipWave.setSampleAmplitudes(wave.getSampleAmplitudes()); 
		}
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
	 * @return the spectrogram object
	 */
	public ClipSpectrogram getSpectrogram(int fftLength, int fftHop) {
		if (clipWave.getSampleAmplitudes()==null) return null; 
		if (spectrogramClip == null || spectrogramClip.getFFTLength()!=fftLength || spectrogramClip.getFFTHop()!=fftHop) {
			return clipWave.getSpectrogram(fftLength, fftHop); 
		}
		return spectrogramClip;
	}


	/**
	 * Get the audio play. This plays the audio files.
	 * @return the audio play
	 */
	public AudioPlay getAudioPlay() {
		return clipWave.getAudioPlay();
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

	/**
	 * Set the grid ID. This is the position of the pamclips on the grid. 
	 * @param i - the grid ID. This should ideally be unique for each pamclip 
	 */
	public void setGridID(int i) {
		this.gridID.set(i);; 

	}

	/**
	 * Get the grid ID. This is the position in the grid (counting from (0,0) along rows)
	 * @return the gridID of the clip. 
	 */
	public int getGridID() {
		return this.gridID.get();
	}

	/**
	 * Get the annotation for the clip. Can be null/ 
	 * @return the annotation of rthe clip
	 */
	public Annotation getAnnotation() {
		return annotation.get();
	}

	/**
	 * Set the annotation. 
	 * @param annotation - the annotation to set. 
	 */
	public void setAnnotation(Annotation annotation) {
		this.annotation.set(annotation);
	}

	/**
	 * Get the annotation property for the clip
	 * @return the annotation property. 
	 */
	public ObjectProperty<Annotation>  annotationProperty(){
		return this.annotation;
	}

	//	/**
	//	 * Get the static grid ID. 
	//	 * @return the static grid id
	//	 */
	//	public int getStaticGridID() {
	//		return staticGridID.get();
	//	}

	/**
	 * Get the filename associated with the clip. 
	 * @return the filename associated with the clip. 
	 */
	public String getFileName() {
		return clipWave.getFileName(); 
	}

	/**
	 * Get the frequency limits of the clip in Hz;
	 * @return the freq limits. 
	 */
	public double[] getFreqLims() {
		return this.freqLims;
	}

	/**
	 * Get the clip length in seconds
	 * @return the clip length in seconds. 
	 */
	public double getClipLength() {
		return clipWave.getLengthInSeconds(); 
	}


	/**
	 * Get the time of the clip in milliseconds. 
	 * @return the time of the clip in millis. 
	 */
	public long getTimeMillis() {
		return timeMillis;
	}

	/**
	 * Set the date time of the clip in milliseconds. 
	 * @param timemillis
	 */
	public void setTimemillis(long timemillis) {
		this.timeMillis = timemillis;
	}

	@Override
	public int compareTo(PAMClip o) {
		long compareage=o.getTimeMillis();

		if (this.timeMillis < compareage)
			return -1;
		else if (this.timeMillis > compareage)
			return 1;
		else
			return 0;

		//		/* For Ascending order*/
		//		return (int) (this.timeMillis-compareage);
	}


	/**
	 * Get the unique name for the clip. This is the filename without the path. 
	 * @return the name of the clip. 
	 */
	public String getClipName() {
		return FilenameUtils.getBaseName(getFileName());
	}

	/**
	 * Get a string of the date time of the start of the clip. 
	 * @return the date time as a string. 
	 */
	public String getDateTimeString() {
	       DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	      return sdf.format(this.getTimeMillis());
	}


}
