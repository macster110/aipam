package com.jamdev.maven.aipam.clips;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.jamdev.jpamutils.wavFiles.WavFile;
import org.jamdev.pambinaries.audiomoth.AudioMothTData;
import org.jamdev.pambinaries.audiomoth.AudioMothTFile;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.clips.datetime.DateTimeParser;
import com.jamdev.maven.aipam.clips.datetime.StandardDateTimeParser;
import com.jamdev.maven.aipam.utils.AiPamUtils;
import com.jamdev.maven.aipam.utils.ArrayScaler;

import uk.me.berndporr.iirj.Butterworth;
/**
 * Standard audio importer for clips. This handles multiple file types such as AudioMoth clips, 
 * wav file clips, and PAMGuard binary files. 
 * 
 * 
 * @author Jamie Macaulay 
 *
 */
public class StandardAudioImporter implements AudioImporter {
	
	/**
	 * The file type is a wave clip. 
	 */
	public static final int WAV_CLIP = 0; 
	
	/**
	 * AudioMoth trigger file 
	 */
	public static final int AUDIO_MOTH = 1; 
	
	/**
	 *  PAMGUARD binary file
	 */
	public static final int PG_BINARY = 2; 

	/**
	 * The default bit size to encode bits as. This will mean that some 24-bit files are downs-sampled. 
	 */
	private static final int DEFAULT_BIT_SIZE = 16; 

	/**
	 * The datetime parser for wav files. 
	 */
	private DateTimeParser datetimeParser;

	public StandardAudioImporter() {
		this.datetimeParser = new StandardDateTimeParser(); 
	}

	@Override
	public ArrayList<ClipWave> importAudio(File audioFile, AIPamParams aiPamParams, AudioImporterListener audioImporterListener, boolean saveWave) {

		ArrayList<ClipWave> waves = null; 
		
//		System.out.println("StandardAudioImporter: Is AudioMoth? file: " + AudioMothTFile.isAudioMothTFile(audioFile));

		boolean isAudioMoth = AudioMothTFile.isAudioMothTFile(audioFile); 
		//now lets figure out what type of file we have. 
		if (isAudioMoth) {
			//is this an AudioMoth trigger file?
			ArrayList<AudioMothTData> audioMothChunks;
			try {
				
				audioMothChunks = AudioMothTFile.loadTFile(audioFile);

				//System.out.println("Load AudioMothTFile: N: " + audioMothChunks.size()); 

				waves = new ArrayList<ClipWave>();
				for (AudioMothTData audioMothTData : audioMothChunks) {
					waves.add(new WavClipWave(audioFile, audioMothTData.wave, (int) audioMothTData.sR, audioMothTData.timeMillis, audioMothTData.wave.length)); 
					
//					System.out.println("Load AudioMothTFile: duration: " + (audioMothTData.wave.length/audioMothTData.sR)
//							+ " timeMillis: " + audioMothTData.timeMillis + " sR: " + audioMothTData.sR); 
//					System.out.println("Length: " + waves.get(waves.size()-1).getLengthInSeconds()); 

				}
				return waves; 
			} catch (UnsupportedAudioFileException | IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} 
		}
		else {
			//we have wav files so assume each wav fiel is a clip. 
			// reads the first 44 bytes for header
			try {
				//a wave 
				ClipWave wave = initWaveWithInputStream(audioFile, aiPamParams.channel, aiPamParams.maximumClipLength, aiPamParams.decimatorSR, saveWave); 

				//add to arrya because that is is what interface needs.  
				waves = new ArrayList<ClipWave>();
				waves.add(wave); 
				return waves; 

			} catch (IOException | UnsupportedAudioFileException| NullPointerException e) {
				e.printStackTrace();
				return null;
			}
		}

	}


	/**
	 * Get the number of samples. 
	 * @param audioFileFormat - the audio format. 
	 * @return 
	 */
	private int getNumSamples(int bytes, AudioFileFormat audioFileFormat) {
		//System.out.println("Byte length: "+ bytes);
		return (int) (bytes
				/(double)  (audioFileFormat.getFormat().getFrameSize()));
	}


	/**
	 * Open a .wav file and extract a single channel of the raw byte data. Then package this single channel 
	 * data into a wave class. Not ideal but .wav e does not handle multi-channel .wav files very well. 
	 * @param audioFile - the audio file to open
	 * @param channel - the channel to extract
	 * @param maxLen - the max length the clip can be in seconds. If above this length it will be trimmed. 
	 * @return a single channel ClipWave instance. 
	 * @throws IOException - exception thrown if any general file errors occur. 
	 * @throws WavFileException 
	 * @throws UnsupportedAudioFileException 
	 */
	public ClipWave initWaveWithInputStream(File audioFile, int channel, double maxLen, int decimatorSr, boolean saveWave) throws IOException, UnsupportedAudioFileException {
		// reads the first 44 bytes for header
		WavFile wavFile = new  WavFile(audioFile);

		AudioFormat format = wavFile.getAudioFileFormat().getFormat(); 

		long dateTime = datetimeParser.getTimeFromFile(audioFile);
		//System.err.println("Imported date time: " + dateTime + " decimatorSr: " + decimatorSr);

		int channels = format.getChannels(); 

		int maxSamples=(int) (format.getSampleRate()*maxLen);

		int sampleRate = (int) format.getSampleRate();

		// load data
		AudioInputStream inputStream  = wavFile.getAudioInputStream(); 

		int numSamples = getNumSamples(inputStream.available(), wavFile.getAudioFileFormat());

		//first down sample
		//now down sample the data if need bed 
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

		short[] samplesShort = null;
		if (saveWave) {

			data = new byte[inputStream.available()];
			inputStream.read(data);	  
			//		}
			
			
			if (channels==1) {
				//no need to do anything else. 
			}
			else {
				//extract single channel data 
				data = WavFile.getSingleChannelByte(format, data,  channel); 
			}
			
			if (data.length/format.getFrameSize()>maxSamples) {
				//trim the data if it is too long. 
				data= WavFile.trim( format, data,  maxSamples); 
			}

			int [] samples = WavFile.getSampleAmplitudes(format, data);
			double bitSize = Math.pow(2, format.getSampleSizeInBits()); 

			//return new WavClipWave(wavFile, WavFile.getSampleAmplitudes(format, data)); 

			if (decimatorSr < format.getSampleRate()) {

				//first have to filter the data
				sampleRate = decimatorSr;
				Butterworth butterworth = new Butterworth();
				butterworth.lowPass(4,format.getSampleRate(), decimatorSr/2);

				double[] wavArray = new double[samples.length]; 

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
					//we want to always convert down to a 16-bit number
					samplesDecimated[i]=(short) (bitSize*wavArray[i]);
				}

				samples=samplesDecimated; 
			}

			/**
			 * samples are in int format and so can be 24 bit etc. Now down-sample the amplitude to 16 bit so that it can be short 
			 * and save memory
			 */

			double defaultBitSize = Math.pow(2, DEFAULT_BIT_SIZE); 

			samplesShort = new short[samples.length]; 

			for (int i=0; i<samples.length; i++) {
				samplesShort[i] = (short) (defaultBitSize*(samples[i]/bitSize)); 
			}
		}

		return new WavClipWave(wavFile.getFile(), samplesShort, sampleRate, dateTime, numSamples); 

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
		int[] duration = new int[files.size()];

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
				duration[j]  = getNumSamples(wavFile.getByteLength(), format);


				audioImporterListener.updateProgress((j/(double) files.size()), j, files.size()); 

			} 
			catch (IOException  | UnsupportedAudioFileException | NullPointerException e) {
				unopenablefiles++;; 
				channels[j] = -1; 
				sampleRate[j] = (float) -1.0; 
				duration[j] = -1;
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

		//System.out.println("Max sample duration: " +  AiPamUtils.max(duration));
		audioInfo.maxFileLength = AiPamUtils.max(duration)/audioInfo.sampleRate;
		audioInfo.medianFilelength = AiPamUtils.median(duration)/audioInfo.sampleRate;

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
