package com.jamdev.maven.aipam.layout.detectionDisplay;

/**
 * Create the acoustic data unit.
 * @author Jamie Macaulay.
 *
 */
public class AcousticDataUnit implements PAMDataUnit {
	
	long timeMillis = 0;
	
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

	public AcousticDataUnit(short[][] audioData, float samplerate, long startTime) {
		this.timeMillis = startTime; 
		this.samplerate = samplerate; 
		this.audioData = audioData; 
	
		System.out.println("Duration samples: " + audioData[0].length); 
		//calculate the duration in seconds. 
		this.duration = 1000.*audioData[0].length/samplerate; 
	}

	@Override
	public long getTimeMilliseconds() {
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

	@Override
	public long getUID() {
		//TODO //meh...
		return timeMillis;
	}

	public long getTimeNanoseconds() {
		return (long) (duration*1e6);
	}

	public int getChannelBitmap() {
		return 1;
	}

}
