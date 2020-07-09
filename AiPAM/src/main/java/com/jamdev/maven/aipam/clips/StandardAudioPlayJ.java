package com.jamdev.maven.aipam.clips;

import java.io.File;
import com.jamdev.maven.aipam.utils.SoundPlay;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;

/**
 * AudioPlay using the Java Sound library. Will not support mp3 but
 * can deal with multi-channel wave files (No one should be using
 * mp3 for scientific sound analysis anyway).
 * 
 * @author Jamie Macaulay
 *
 */
public class StandardAudioPlayJ implements AudioPlay {
	
	/**
	 * The volume property. 
	 */
	private DoubleProperty volumeProperty; 
	
	/**
	 * The audio file. 
	 */
	private File audioFile;
	
	/**
	 * Sound play. 
	 */
	public SoundPlay soundPlay;

	/**
	 * The audioPlay task; 
	 */
	private AudioPlayTask task; 

	/**
	 * Constructor for standard audio playing. 
	 * @param file - the audio fi;le 
	 */
	public StandardAudioPlayJ(File file) {
		this.soundPlay = new SoundPlay(); 
		volumeProperty= new SimpleDoubleProperty(); 
		volumeProperty.addListener((obsVal, oldVal, newVal)->{
			//volume is between 0 and 1; 
			soundPlay.setVolume((float) ((newVal.doubleValue()-0.5)*6.0206));
		});
		this.audioFile=file; 
	}
	
	/**
	 * Get the audio file. 
	 * @return the audio file. 
	 */
	public File getAudioFile() {
		return audioFile; 
	}

	@Override
	public void playClipAudio() {
        Thread th = new Thread(task = new AudioPlayTask(audioFile));
        th.setDaemon(true);
        th.start();
	}

	
	@Override
	public void stopClipAudio() {
		if (task!=null) {
			task.cancel(); 
			while (task.isRunning()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			task = null; 
		}
	}
	
	/**
	 * Get the volume property.
	 * 
	 * @return the volume property
	 */
	@Override
	public DoubleProperty getVolumePropery() {
		return volumeProperty; 
	}
	
	@Override
	public boolean isPlaying() {
		//System.out.println("soundPlay.isPlaying();" + soundPlay.isPlaying());
		return soundPlay.isPlaying();
	}

	class AudioPlayTask extends Task<Integer> {
		
		boolean play = true; 

		public AudioPlayTask(File audioFile) {
			// TODO Auto-generated constructor stub
			this.setOnCancelled((e)->{
				soundPlay.stop();
			});
		}

		@Override
		protected Integer call() throws Exception {
			soundPlay.play(audioFile); 
			return 1;
		}
		
	}




	
}
