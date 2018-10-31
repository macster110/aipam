package com.jamdev.maven.clips;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Standard audio playing which simply stores a file path to play from. This
 * should be able to play multiple types of audio file.  
 * 
 * @author Jamie Macaulay 
 *
 */
public class StandardAudioPlay implements AudioPlay {

	/**
	 * The audio file. 
	 */
	private File audioFile;
	
	/**
	 * The Media player. 
	 */
	private MediaPlayer mediaPlayer; 
	
	/**
	 * Constructor for standard audio playing. 
	 * @param file - the audio fi;le 
	 */
	public StandardAudioPlay(File file) {
		this.audioFile=file; 
	}

	@Override
	public void playClipAudio() {
		getMediaPlay().play();		
	}
	
	private MediaPlayer getMediaPlay() {
		if (mediaPlayer==null) {
			Media hit = new Media(audioFile.toURI().toString());
			mediaPlayer = new MediaPlayer(hit);
		}
		return mediaPlayer; 
	}

	@Override
	public void stopClipAudio() {
		if (mediaPlayer!=null) mediaPlayer.stop();
	}
	
	/**
	 * Get the volume property. 
	 * @return the volume property
	 */
	public DoubleProperty getVolumePropery() {
		return getMediaPlay().volumeProperty(); 
	}
	


}
