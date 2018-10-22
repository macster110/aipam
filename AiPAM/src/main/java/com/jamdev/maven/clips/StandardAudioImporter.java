package com.jamdev.maven.clips;

import java.io.File;
import java.util.ArrayList;

import org.datavec.audio.Wave;

/**
 * Standard audio importer for single .wav clips
 * @author Jamie Macaulay 
 *
 */
public class StandardAudioImporter implements AudioImporter {


	@Override
	public ArrayList<Wave> importAudio(File audioFile) {
		//a wave 
		Wave wave = new Wave(audioFile.getAbsolutePath()); 
		
		//waves 
		ArrayList<Wave> waves = new ArrayList<Wave>();
		waves.add(wave); 
		
		return waves;
	}

}
