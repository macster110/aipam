package com.jamdev.maven.aipam.layout.detectionDisplay;

import java.util.ArrayList;
import java.util.List;

/*
 * A standard data provider which allows the input of a single chunk of audio data. 
 */
public class StandardDataProvider implements DataProvider {
	
	/**
	 * The current data listeners. 
	 */
	ArrayList<DataProviderListener> dataListeners = new ArrayList<DataProviderListener> ();
	
	/**
	 * The start time of the audio in java millis
	 */
	private long startTime;
	
	/**
	 * The sample rate of the audio in samples per second. 
	 */
	private long sampleRate;
	
	/**
	 * The raw audio data in 16bit samples. 
	 */
	private short[][] audioData; 

	public StandardDataProvider(short[][] audioData, long samplerate, long startTime) {
		this.startTime = startTime; 
		this.sampleRate = samplerate; 
		this.audioData = audioData; 
	}
	

	@Override
	public void addListener(DataProviderListener dataProviderListener) {
		dataListeners.add(dataProviderListener); 
	}

	@Override
	public boolean removeListener(DataProviderListener dataProviderListener) {
		return dataListeners.remove(dataProviderListener); 
	}

	@Override
	public List<DataProviderListener> getDataProviderListeners() {
		return dataListeners;
	}
	
	private void addAudioData(short[][] audioData, long samplerate, long startTime) {
		PAMDataUnit dataUnit = new AcousticDataUnit(audioData, samplerate, startTime); 
		for (int i=0; i<this.getDataProviderListeners().size(); i++) {
			this.getDataProviderListeners().get(i).newAudioData(dataUnit);
		}
	}

	@Override
	public void requestData(long start, long end) {
		
		int sampleStart = (int) Math.round(sampleRate*((start - startTime)/1000.));
		int sampleEnd = (int) Math.round(sampleRate*((end - startTime)/1000.));
		
		System.out.println("SampleStart: " + sampleStart + " sampleEnd: " +sampleEnd + "   " + audioData[0].length + " start millis: " +start);
	
		sampleStart = Math.max(sampleStart, 0);
		sampleEnd = Math.min(sampleEnd, audioData[0].length-1);
		
		if (sampleStart==sampleEnd) {
			System.err.println("StandardDataProvider: Requested data is less than 1 sample"); 
			return; 
		}
		
		short[][] wavedata = new short[audioData.length][]; 
		
		short[] audioDataChunk;
		//fastest way to copy arrays. Copy each channel. 
		for (int i=0; i<audioData.length; i++) {
			audioDataChunk = new short[sampleEnd-sampleStart]; 
			System.arraycopy(audioData[i], sampleStart, audioDataChunk, 0, sampleEnd-sampleStart);
			wavedata[i] = audioDataChunk;
		}

		//send the audio data off to the listeners. 
		addAudioData(wavedata,  sampleRate, start);
	}

	@Override
	public DataLimits getDataLimits() {
		// TODO Auto-generated method stub
		return null;
	}

}
