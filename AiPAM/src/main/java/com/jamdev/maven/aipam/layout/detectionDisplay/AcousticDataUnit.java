package com.jamdev.maven.aipam.layout.detectionDisplay;

/**
 * Create the acoustic data unit.
 * @author Jamie Macaulay.
 *
 */
public class AcousticDataUnit implements PAMDataUnit {
	
	double timeMillis = 0;
	
	/**
	 * The sample rate. 
	 */
	private float samplerate;
	
	/**
	 * The audio data
	 */
	private short[][] audioData; 
	
	/**
	 * The duration. 
	 */
	private double duration;

	/**
	 * Unique identifier. 
	 */
	private long uid;

	public AcousticDataUnit(short[][] audioData, float samplerate, double startTime) {
		this.timeMillis = startTime; 
		this.samplerate = samplerate; 
		this.audioData = audioData; 
	
		//calculate the duration in seconds. 
		this.duration = 1000.*(audioData[0].length/samplerate); 
		
		//System.out.println("Duration: " + duration);
	}
	
	public AcousticDataUnit(short[][] audioData, float samplerate, double startTime, long uid) {
		this(audioData,  samplerate, startTime); 
		this.setUID(uid);
	}

	/**
	 * Set the UID. 
	 * @param uid
	 */
	private void setUID(long uid) {
		this.uid = uid; 
	}

	@Override
	public double getTimeMilliseconds() {
		return timeMillis;
	}
	
	/**
	 * Get the sample rate in samples per second.. 
	 * @return the sample rate in samples per second. 
	 */
	public float getSampleRate() {
		return samplerate;
	}
	
	/**
	 * Get the audio data as 16 bit samples. 
	 * @return the audio data. 
	 */
	public short[][] getAudioData() {
		return audioData;
	}

	@Override
	public double getDurationInMilliseconds() {
		return duration;
	}

//	@Override
//	public long getUID() {
//		//TODO //meh...
//		return timeMillis;
//	}

	public long getTimeNanoseconds() {
		return (long) (duration*1e6);
	}

	public int getChannelBitmap() {
		return 1;
	}

	@Override
	public long getUID() {
		return this.uid;
	}

}
