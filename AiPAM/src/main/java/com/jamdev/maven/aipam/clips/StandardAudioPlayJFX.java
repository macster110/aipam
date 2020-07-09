package com.jamdev.maven.aipam.clips;

import java.io.File;

import com.jamdev.maven.aipam.utils.SoundPlay;

import javafx.beans.property.DoubleProperty;

/**
 * A mish mash of audio play. StandardAudioPlayJ works well with multi channel files but seems 
 * like 24 bit is an issue. JavaFX can deal with 24 bit but multi channel (>2 channesl) files 
 * are an issue. This tries both....it's a bit of a HACK but will do until JavaFX fixes itself. 
 * <p>
 * Not pretty but will have to do for now. 
 * @author Jamie Macaulay
 *
 */
public class StandardAudioPlayJFX implements AudioPlay  {


	private AudioPlay usedAudioPlay;

	private File audioFile; 

	public StandardAudioPlayJFX(File file) {
		this.audioFile = file; 
	}

	@Override
	public void playClipAudio() {
		checkAudioPlay(); 
		usedAudioPlay.playClipAudio();
	}

	/**
	 * Check to see which audio player should be used and set that as the AudioPlay. 
	 */
	private void checkAudioPlay() {
		if (usedAudioPlay==null) {
			SoundPlay soundPlay = new SoundPlay();
			//try java native system first. 
			boolean audioJOK = soundPlay.prepFile(audioFile); 
			if (audioJOK) {
				usedAudioPlay =  new StandardAudioPlayJ(audioFile);; 
			}
			else {
				//need to resort ot JavaFX. Will not work with more than two channels. 
				usedAudioPlay =  new StandardAudioPlayFX(audioFile);
			}
		}
	}

	@Override
	public void stopClipAudio() {
		checkAudioPlay(); 
		usedAudioPlay.stopClipAudio();

	}

	@Override
	public DoubleProperty getVolumePropery() {
		checkAudioPlay(); 
		return usedAudioPlay.getVolumePropery(); 
	}

	@Override
	public boolean isPlaying() {
		checkAudioPlay(); 
		return  usedAudioPlay.isPlaying(); 
	}


}
