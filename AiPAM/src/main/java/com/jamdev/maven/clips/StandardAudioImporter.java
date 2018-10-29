package com.jamdev.maven.clips;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.datavec.audio.WaveHeader;

import com.jamdev.maven.aipam.utils.AiPamUtils;
/**
 * Standard audio importer for single .wav clips
 * @author Jamie Macaulay 
 *
 */
public class StandardAudioImporter implements AudioImporter {

	@Override
	public ArrayList<ClipWave> importAudio(File audioFile, int channel, double maxLen) {

		//FIXME
		//The Wave class is actually a bit naff but integrates with deeplearning4j so we 
		//need it. It does not handle multi channel .wav files very well. So we need to open a multi channel
		//wav file, get the byte stream for 1 channel, alter the header so it's single channel and then 
		//use the Wave(wavHeader, byte[]) constructor instead of the direct file constructor. 
		//Messy but best option untill the deeplearning4j library is updated. 

		//first open the file

		// reads the first 44 bytes for header
		try {
			//a wave 
			ClipWave wave = initWaveWithInputStream(audioFile, channel, maxLen); 

			//add to arrya because that is is what interface needs.  
			ArrayList<ClipWave> waves = new ArrayList<ClipWave>();
			waves.add(wave); 
			return waves; 

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Open a wav file and extract a single channel of the raw byte data. Then package this single channel 
	 * data into a wave class. Not ideal but Wav e does not handle multi channel .wav files very well. 
	 * @param audioFile - the audio file to open
	 * @param channel - the channel to extract
	 * @param maxLen - the max length the clip can be in seconds. If above this length it will be trimmed. 
	 * @return a single channel ClipWave instance. 
	 * @throws IOException - exception thrown if any general file errors occur. 
	 */
	private ClipWave initWaveWithInputStream(File audioFile, int channel, double maxLen) throws IOException {
		// reads the first 44 bytes for header

		InputStream inputStream = new FileInputStream(audioFile);

		WaveHeader waveHeader = new WaveHeader(inputStream);
		int channels = waveHeader.getChannels(); 

		int maxSamples=(int) (waveHeader.getSampleRate()*maxLen);

		if (waveHeader.isValid()) {
			// load data
			byte[] data = new byte[inputStream.available()];
			inputStream.read(data);	  

			if (channels==1) {
				//no need to do anythiong else. 
				data=trim( waveHeader, data,  maxSamples); 
				return new ClipWave(audioFile, waveHeader, data); 
			}

			//extract single channel data 
			byte[] singleChan = getSingleChannelByte(waveHeader, data,  channel); 
			singleChan=trim( waveHeader, singleChan,  maxSamples); 

			//System.out.println("singleChan: " + singleChan.length + " all bytes: " + data.length );

			//set wave header to have only one channel. Important for future calculations within deeplearning4j
			waveHeader.setChannels(1); 
			waveHeader.setSubChunk2Size(waveHeader.getSubChunk2Size()-(data.length-singleChan.length));

			waveHeader.setByteRate(waveHeader.getByteRate()/channels);
			//waveHeader.setChunkSize(waveHeader.getChunkSize()/channels);
			waveHeader.setBlockAlign(waveHeader.getBlockAlign()/channels);
			waveHeader.setChunkSize(waveHeader.getChunkSize()-(data.length-singleChan.length));


			return new ClipWave(audioFile, waveHeader, singleChan); 

		} 
		// end load data
		else {
			System.err.println("Invalid Wave Header");
			return null; 
		}
	}

	/**
	 * Trim the byte data. Trimmed from center left and right so that the size is no larger than maxSamples. 
	 * @param wavHeader - the wave file header 
	 * @param bytes - a SINGLE-channel byte array 
	 * @param maxSamples - the sample size of the trimmed clip. 
	 * @return the byte array for one channel. 
	 */
	public byte[] trim(WaveHeader wavHeader, byte[] bytes, int maxSamples) {

		int bytePerSample = wavHeader.getBitsPerSample() / 8; 
		int numSamples = bytes.length / bytePerSample; 

		int trimSamples=numSamples - maxSamples; 

		byte[] trimByteChan = new byte[maxSamples*bytePerSample];
		System.out.println();

		int pointer=0; 
		int n=0; 
		for (int i = (trimSamples/2); i < numSamples-(trimSamples/2); i++) {
			//short amplitude = 0;
			for (int byteNumber = 0; byteNumber < bytePerSample; byteNumber++) {
				// little endian
				//amplitude |= (short) ((bytes[pointer++] & 0xFF) << (byteNumber * 8));
				trimByteChan[n++]=bytes[pointer++]; 
			}
		}
		return trimByteChan; 
	}


	/**
	 *  Get a multi channel .wav file and resturn the byte stream for just one of the channels.
	 * @param wavHeader - the wave file header 
	 * @param bytes - the multi-channel byte array 
	 * @param channel - the channel to extract. This should never be higher than the total number of channels. 
	 * @return the byte array for one channel. 
	 */
	public byte[] getSingleChannelByte(WaveHeader wavHeader, byte[] bytes, int channel) {

		int bytePerSample = wavHeader.getChannels()*wavHeader.getBitsPerSample() / 8;
		int bytesPerSampleChannel = wavHeader.getBitsPerSample() / 8;
		int numSamples = bytes.length / bytePerSample; 

		byte[] singleByteChan = new byte[numSamples*bytesPerSampleChannel];

		//channel=1; 

		int pointer = 0;
		int n = 0; 
		for (int i = 0; i < numSamples; i++) {
			//short amplitude = 0;
			for (int j=0; j<wavHeader.getChannels(); j++) {
				for (int byteNumber = 0; byteNumber < bytesPerSampleChannel; byteNumber++) {
					// little endian
					//amplitude |= (short) ((bytes[pointer++] & 0xFF) << (byteNumber * 8));
					if (j==channel) {
						singleByteChan[n]=(byte) (bytes[pointer] & 0xFF);
						n++;
					}
					pointer++;
				}
			}
		}


		return singleByteChan;		
	}

	@Override
	public AudioInfo getAudioInfo(File audioFileDirectory) {
		
		List<File> files  = AiPamUtils.listFiles(audioFileDirectory.getAbsolutePath(), "wav");
		
		int unopenablefiles = 0; 
		Integer[] channels = new Integer[files.size()];
		Float[] sampleRate = new Float[files.size()];

		for (int j=0; j<files.size(); j++) {
			InputStream inputStream;
			try {
				inputStream = new FileInputStream(files.get(j));
				WaveHeader waveHeader = new WaveHeader(inputStream);
				
				channels[j]=waveHeader.getChannels();
				sampleRate[j] = (float) waveHeader.getSampleRate(); 
				
			} catch (FileNotFoundException e) {
				unopenablefiles++;; 
				channels[j] = -1; 
				sampleRate[j] = (float) -1.0; 
				e.printStackTrace();
			}
		}
		
		//convert to list
		Collection<Integer> chnnlList = Arrays.asList(channels); 
		Collection<Float> srList = Arrays.asList(sampleRate); 

		// Get collection without duplicate i.e. distinct only
		List<Integer> chnnlListElements = chnnlList.stream().distinct().collect(Collectors.toList());
		List<Float> srListElements = srList.stream().distinct().collect(Collectors.toList());
		
		chnnlListElements.remove(new Integer(-1));
		srListElements.remove(new Float(-1.0));
		

		AudioInfo audioInfo = new AudioInfo();
		
		System.out.println(chnnlListElements);
		System.out.println(srListElements);

		if (chnnlListElements.size()==1) audioInfo.isSameChannels=true;
		if (srListElements.size()==1) audioInfo.isSameSampleRate=true;
		
		audioInfo.channels=chnnlListElements.get(0); 
		audioInfo.sampleRate=chnnlListElements.get(0);
		audioInfo.nFiles=files.size();
		audioInfo.nFilesCorrupt = unopenablefiles; 
		audioInfo.file = audioFileDirectory.getAbsolutePath();

		return audioInfo;
	}

	@Override
	public List<File> listAudioFiles(File audioFileDirectory) {
		List<File> files  = AiPamUtils.listFiles(audioFileDirectory.getAbsolutePath(), "wav");		
		return files;
	}


	//	 /**
	//     * Get the amplitudes of the wave samples (depends on the header). Had to make
	//     * a function that actually reads data alright. 
	//     * 
	//     * @return amplitudes array (signed 16-bit)
	//     */
	//	
	//    public short[][] getSampleAmplitudesM(int nChannel) {
	//        int bytePerSample = this.getWaveHeader().getChannels()*this.getWaveHeader().getBitsPerSample() / 8;
	//        int bytesPerSampleChannel = this.getWaveHeader().getBitsPerSample() / 8;
	//        int numSamples = this.getBytes().length / bytePerSample; 
	//        short[][] amplitudes = new short[nChannel][numSamples];
	//
	//        int pointer = 0;
	//        for (int i = 0; i < numSamples; i++) {
	//        	short amplitude = 0;
	//        	for (int j=0; j<nChannel; j++) {
	//        		for (int byteNumber = 0; byteNumber < bytesPerSampleChannel; byteNumber++) {
	//        			// little endian
	//        			amplitude |= (short) ((getBytes()[pointer++] & 0xFF) << (byteNumber * 8));
	//        		}
	//        		amplitudes[j][i] = amplitude;
	//        	}
	//        }
	//
	//        return amplitudes;
	//    }

	//    @Override
	//    public short[] getSampleAmplitudes() {
	//    	//had to override this 
	//    	return getSampleAmplitudesM(3)[channel]; 
	//    }

}
