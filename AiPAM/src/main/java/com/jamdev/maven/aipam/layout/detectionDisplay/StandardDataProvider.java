package com.jamdev.maven.aipam.layout.detectionDisplay;

import java.util.ArrayList;
import java.util.List;

/*
 * A standard data provider which allows the input of a single chunk of audio data. 
 */
public class StandardDataProvider implements DataProvider {

	/**
	 * The audio chunk length. The audio is segmented into discrete length with unique IDs. This 
	 * helps with tracking which data units have been added to the plot. 
	 */
	public static final double AUDIO_CHUNK_LEN = 400; 

	/**
	 * The current data listeners. 
	 */
	ArrayList<DataProviderListener> dataListeners = new ArrayList<DataProviderListener> ();

	/**
	 * The start time of the audio in java millis
	 */
	private double startTime;

	/**
	 * The sample rate of the audio in samples per second. 
	 */
	private float sampleRate;

	/**
	 * The raw audio data in 16bit samples. 
	 */
	private short[][] audioData;

//	/**
//	 * Last time in millis start request
//	 */
//	private long laststart;
//
//	/**
//	 * Last time in millis long request. 
//	 */
//	private long lastend;

	/**
	 * Unique identifier.
	 */
	private long uid;

	/**
	 * The data limits. 
	 */
	private DataLimits dataLimits; 

	/**
	 * 
	 * @param audioData
	 * @param samplerate
	 * @param startTime
	 */

	public StandardDataProvider(short[][] audioData, float samplerate, double startTime) {
		this.startTime = startTime; 
		this.sampleRate = samplerate; 
		this.audioData = audioData; 
		
		
		this.dataLimits = new DataLimits(0, this.audioData[0].length/samplerate);
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

	private void addAudioData(short[][] audioData, float samplerate, double startTime, long uid) {
		if (audioData[0]==null) return;
		PAMDataUnit dataUnit = new AcousticDataUnit(audioData, samplerate, startTime, uid); 
		for (int i=0; i<this.getDataProviderListeners().size(); i++) {
			this.getDataProviderListeners().get(i).newAudioData(dataUnit);
		}
	}

	@Override
	public void requestData(double start, double end) {

		System.out.println("Request data between: start: " + start + " end: " +  end + " Total Millis: " + (end-start) ); 

		//the sample rate
		double sampleStart = sampleRate*((start - startTime)/1000.);
		double sampleEnd = sampleRate*((end - startTime)/1000.);
		
		//System.out.println("Request data between: sampleStart: " + sampleStart + " sampleStart: " +  sampleEnd + " Total Millis: " + (end-start) + "sR: " + this.sampleRate); 

		//figure out the start and end chunks.
		int modStart = (int) Math.floor(sampleStart/AUDIO_CHUNK_LEN); 
		int modEnd = (int) Math.floor(sampleEnd/AUDIO_CHUNK_LEN)+1; 

//		//System.out.println("SampleStart: " + sampleStart + " sampleEnd: " +sampleEnd + "   " + audioData[0].length + " start millis: " +start);
//		sampleStart = Math.max(modStart*AUDIO_CHUNK_LEN, 0);
//		sampleEnd = Math.min(modEnd*AUDIO_CHUNK_LEN, audioData[0].length-1);
		
		//System.out.println("modStart: " + modStart + " modEnd: " + modEnd + " sampleStart: " + sampleStart + " sampleEnd: " + sampleEnd);
		
		if (sampleStart==sampleEnd) {
			System.err.println("StandardDataProvider: Requested data is less than 1 sample"); 
			return; 
		}

		short[][] wavedata ;
		int sampleStartChnk; 
		int sampleEndChnk;
		
		double startTimeChnk; 
		for (int j = modStart; j<modEnd ; j++) {
			
			
			wavedata = new short[audioData.length][]; 
			
			sampleStartChnk = (int) (j*AUDIO_CHUNK_LEN);
			sampleEndChnk = (int) ((j+1)*AUDIO_CHUNK_LEN);


			short[] audioDataChunk;
			int len = -1; 
			//fastest way to copy arrays. Copy each channel. 
			for (int i=0; i<audioData.length; i++) {
				len = sampleEndChnk-sampleStartChnk;
				if (sampleStartChnk+len>audioData[i].length) {
					len = audioData[i].length - sampleStartChnk; 
				}
				if (len<=0) {
					continue;
				}
				audioDataChunk = new short[len]; 
				System.arraycopy(audioData[i], sampleStartChnk, audioDataChunk, 0, len);
				wavedata[i] = audioDataChunk;
			}

			startTimeChnk = (startTime+1000.*(sampleStartChnk/sampleRate));

			this.uid = j;
			
			//System.out.println("----- modStart: " + modStart +  " modeEnd: " + modEnd + " sampleStartChnk: " + sampleStartChnk + " sampleEndChnk " + sampleEndChnk + " seconds start: "  + (sampleStartChnk/(double) this.sampleRate) + " millis start: "+ startTimeChnk + " last chunk: " + wavedata[0][wavedata[0].length-2]);


			//send the audio data off to the listeners. 
			addAudioData(wavedata,  sampleRate, startTimeChnk, uid);
		}
	}

//		@Override
//		public void requestData(long start, long end) {
//			
//			//TODO
//			//could make a hash map of start, end UID - would be more processor efficient
//			//important for ensuring plot segments accept data as the same uID vlaue will not be written
//			//Need to think more on how to do this...
//			if (laststart!=start || lastend!=end) {
//				this.uid++; 
//			}
//			
//			//the sample rate
//			int sampleStart = (int) Math.round(sampleRate*((start - startTime)/1000.));
//			int sampleEnd = (int) Math.round(sampleRate*((end - startTime)/1000.));
//			
//			//System.out.println("SampleStart: " + sampleStart + " sampleEnd: " +sampleEnd + "   " + audioData[0].length + " start millis: " +start);
//			sampleStart = Math.max(sampleStart, 0);
//			sampleEnd = Math.min(sampleEnd, audioData[0].length-1);
//			
//			if (sampleStart==sampleEnd) {
//				System.err.println("StandardDataProvider: Requested data is less than 1 sample"); 
//				return; 
//			}
//			
//			short[][] wavedata = new short[audioData.length][]; 
//			
//			short[] audioDataChunk;
//			//fastest way to copy arrays. Copy each channel. 
//			for (int i=0; i<audioData.length; i++) {
//				audioDataChunk = new short[sampleEnd-sampleStart]; 
//				System.arraycopy(audioData[i], sampleStart, audioDataChunk, 0, sampleEnd-sampleStart);
//				wavedata[i] = audioDataChunk;
//			}
//	
//			//send the audio data off to the listeners. 
//			addAudioData(wavedata,  sampleRate, start, uid);
//		}

	@Override
	public DataLimits getDataLimits() {
		return dataLimits;
	}

}
