package com.jamdev.maven.aipam.clips;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.utils.AiPamUtils;
import com.jamdev.maven.aipam.utils.ArrayScaler;
import com.jamdev.maven.aipam.utils.WavFile;

import uk.me.berndporr.iirj.Butterworth;
/**
 * Standard audio importer for single .wav clips
 * @author Jamie Macaulay 
 *
 */
public class StandardAudioImporter implements AudioImporter {

	@Override
	public ArrayList<ClipWave> importAudio(File audioFile, AIPamParams aiPamParams, AudioImporterListener audioImporterListener) {

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
			ClipWave wave = initWaveWithInputStream(audioFile, aiPamParams.channel, aiPamParams.maximumClipLength, aiPamParams.decimatorSR); 

			//add to arrya because that is is what interface needs.  
			ArrayList<ClipWave> waves = new ArrayList<ClipWave>();
			waves.add(wave); 
			return waves; 

		} catch (IOException | UnsupportedAudioFileException| NullPointerException e) {
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
	 * @throws WavFileException 
	 * @throws UnsupportedAudioFileException 
	 */
	private ClipWave initWaveWithInputStream(File audioFile, int channel, double maxLen, int decimatorSr) throws IOException, UnsupportedAudioFileException {
		// reads the first 44 bytes for header
		WavFile wavFile = new  WavFile(audioFile);
		AudioFormat format = wavFile.getAudioFileFormat().getFormat(); 

		int channels = format.getChannels(); 

		int maxSamples=(int) (format.getSampleRate()*maxLen);

		// load data
		AudioInputStream inputStream  = wavFile.getAudioInputStream(); 

		//first downsample
		//now downsample the data if need bed 
		byte[] data;
		//		if (decimatorSr < format.getSampleRate()) {
		//
		//			AudioFormat sourceFormat = inputStream.getFormat();
		//			AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
		//					decimatorSr,
		//					sourceFormat.getSampleSizeInBits(),
		//					sourceFormat.getChannels(),
		//					sourceFormat.getFrameSize(),
		//					sourceFormat.getFrameRate(),
		//					sourceFormat.isBigEndian());
		//
		//			AudioInputStream decimatorStream = AudioSystem.getAudioInputStream(targetFormat, inputStream);
		//			//int nWrittenBytes = AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE, new File("C:\\Users\\macst\\Desktop\\hello.wav"));
		//
		//			data = new byte[decimatorSr*sourceFormat.getSampleSizeInBits()];  
		//
		//			int nWrittenBytes =  decimatorStream.read(data);
		//			maxSamples = (int) (targetFormat.getSampleRate()*maxLen);
		//			format=targetFormat;
		//			
		//			System.out.println("Written bytes:  " + nWrittenBytes);
		//
		//		}
		//		else { 
		data = new byte[inputStream.available()];
		inputStream.read(data);	  
		//		}

		if (channels==1) {
			//no need to do anythiong else. 
			data=WavFile.trim( format, data,  maxSamples); 

		}
		else {
			//extract single channel data 
			data = WavFile.getSingleChannelByte(format, data,  channel); 
			data= WavFile.trim( format, data,  maxSamples); 
		}

		int[] samples = WavFile.getSampleAmplitudes(format, data);

		//return new WavClipWave(wavFile, WavFile.getSampleAmplitudes(format, data)); 

		int sampleRate = (int) format.getSampleRate();
		if (decimatorSr < format.getSampleRate()) {
			
			//first have to filter the data
			sampleRate = decimatorSr;
			Butterworth butterworth = new Butterworth();
			butterworth.lowPass(4,format.getSampleRate(), decimatorSr/2);
			
			double[] wavArray = new double[samples.length]; 
			double bitSize = Math.pow(2, format.getSampleSizeInBits()); 
			for (int i=0; i<wavArray.length; i++) {
				wavArray[i] = butterworth.filter((double) samples[i]/bitSize);
			}
		
			//now have to down sample. 
			ArrayScaler arrayScaler = new ArrayScaler(wavArray); 
			//work out the desired length of the array
			int arraySize = (int) (wavArray.length*(decimatorSr/((double) format.getSampleRate())));
			//interpolate the samples. 
			wavArray = arrayScaler.getScaled(arraySize); 
			
			int[] samplesDecimated = new int[wavArray.length]; 
			for (int i=0; i<wavArray.length; i++) {
				samplesDecimated[i]=(int) (bitSize*wavArray[i]);
			}

			samples=samplesDecimated; 
		}

		return new WavClipWave(wavFile, samples, sampleRate); 

		//System.out.println("singleChan: " + singleChan.length + " all bytes: " + data.length );

		//			//set wave header to have only one channel. Important for future calculations within deeplearning4j
		//			waveHeader.setChannels(1); 
		//			waveHeader.setSubChunk2Size(waveHeader.getSubChunk2Size()-(data.length-singleChan.length));
		//
		//			waveHeader.setByteRate(waveHeader.getByteRate()/channels);
		//			//waveHeader.setChunkSize(waveHeader.getChunkSize()/channels);
		//			waveHeader.setBlockAlign(waveHeader.getBlockAlign()/channels);
		//			waveHeader.setChunkSize(waveHeader.getChunkSize()-(data.length-singleChan.length));
		//			return new ClipWave(audioFile, waveHeader, singleChan); 
		//		} 
		//		// end load data
		//		else {
		//			System.err.println("Invalid Wave Header");
		//			return null; 
		//		}
	}



	@Override
	public AudioInfo getAudioInfo(File audioFileDirectory, AudioImporterListener audioImporterListener) {

		List<File> files  = AiPamUtils.listFiles(audioFileDirectory.getAbsolutePath(), "wav");

		int unopenablefiles = 0; 
		Integer[] channels = new Integer[files.size()];
		Float[] sampleRate = new Float[files.size()];
		
		audioImporterListener.updateProgress(0, 0, files.size()); 
		
		for (int j=0; j<files.size(); j++) {
			try {
				if (audioImporterListener.isCancelled()) {
					return null; 
				}
//				System.out.println(files.get(j)); 
				WavFile wavFile = new  WavFile(files.get(j));
				AudioFileFormat format = wavFile.getAudioFileFormat(); 
				//				inputStream = new FileInputStream(files.get(j));
				//				WaveHeader waveHeader = new WaveHeader(inputStream);

				channels[j]=format.getFormat().getChannels();
				sampleRate[j] = (float) format.getFormat().getSampleRate();
							
				audioImporterListener.updateProgress((j/(double) files.size()), j, files.size()); 

			} 
			catch (IOException  | UnsupportedAudioFileException | NullPointerException e) {
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

//		System.out.println(" chnnlListElements: " + chnnlListElements);
//		System.out.println(" srListElements: " + srListElements);

		if (chnnlListElements.size()==1) audioInfo.isSameChannels=true;
		if (srListElements.size()==1) audioInfo.isSameSampleRate=true;

		audioInfo.channels=chnnlListElements.get(0); 
		audioInfo.sampleRate=srListElements.get(0);
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
