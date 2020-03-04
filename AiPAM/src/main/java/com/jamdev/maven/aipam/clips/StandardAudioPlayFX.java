package com.jamdev.maven.aipam.clips;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 * Standard audio playing which simply stores a file path to play from. This
 * is a JavaFGX implementation for audio playing and should be able to play 
 * multiple types of audio file.  
 * 
 * However, this appears to have a problem playing multi-channel .wav files if 
 * the output soundcard does not support that number of channels. For some reason
 * this seems to only hvae been an issue after jre8_131. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class StandardAudioPlayFX implements AudioPlay {

	/**
	 * The audio file. 
	 */
	private File audioFile;
	
	/**
	 * The Media player. 
	 */
	private MediaPlayer  mediaPlayer; 
	
	/**
	 * Constructor for standard audio playing. 
	 * @param file - the audio fi;le 
	 */
	public StandardAudioPlayFX(File file) {
		this.audioFile=file; 
	}

	@Override
	public void playClipAudio() {
		System.out.println("Play: " + getMediaPlay().getMedia().getSource());

		getMediaPlay().play();		
		
		if (getMediaPlay().getError()!=null) {
			System.out.println(getMediaPlay().getError().getMessage());
			System.out.println(getMediaPlay().getError().getType());
		}
		else {
			System.out.println("No error: ");
		}
	}
	
	private MediaPlayer getMediaPlay() {
		if (mediaPlayer==null) {
			URL resource;
			try {
				resource = audioFile.toURI().toURL();
				Media hit = new Media(resource.toExternalForm().toString());
				mediaPlayer = new MediaPlayer(hit);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
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

		File audioFile = new File("E:\\Google Drive\\SMRU_research\\Tanzania 2019\\Blast Data - 2018-2019\\Blast WAV Files\\Maziwe\\Bombs_retrieval_1\\Ish_Bomb20180708_125236_672.wav");
		
		
		 try {
             AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile.getAbsoluteFile());
             Clip clip = AudioSystem.getClip();
             clip.open(audioInputStream);
             clip.start();

			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finished audio: "); 
	}

	@Override
	public boolean isPlaying() {
		return getMediaPlay().getStatus().equals(Status.PLAYING);
//		return getMediaPlay().PL
	}
	


}
