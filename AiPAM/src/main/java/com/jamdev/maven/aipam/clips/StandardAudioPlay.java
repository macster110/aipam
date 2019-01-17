package com.jamdev.maven.aipam.clips;

import java.io.File;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.AudioClip;

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
	private AudioClip  mediaPlayer; 
	
	/**
	 * Constructor for standard audio playing. 
	 * @param file - the audio fi;le 
	 */
	public StandardAudioPlay(File file) {
		this.audioFile=file; 
	}

	@Override
	public void playClipAudio() {
		System.out.println("Play: " + getMediaPlay().getSource()); 
		getMediaPlay().play();		
	}
	
	private AudioClip getMediaPlay() {
		if (mediaPlayer==null) {
//			Media hit = new Media(audioFile.toURI().toString());
			mediaPlayer = new AudioClip (audioFile.toURI().toString());
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
	
	/**
	 * Tests audio playing 
	 * @param args
	 */
	public static void main(String[] args) {
		File audioFile = new File("E:\\Google Drive\\SMRU_research\\Tanzania 2019\\Blast Data - 2018-2019\\Blast WAV Files\\Maziwe\\Ish_Bomb20180616_092302_870.wav");
		StandardAudioPlay standardAudioPlay = new StandardAudioPlay(audioFile); 
		standardAudioPlay.getMediaPlay().volumeProperty().set(1);
		System.out.println("Play audio: "); 
		standardAudioPlay.playClipAudio();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished audio: "); 
	}
	


}
