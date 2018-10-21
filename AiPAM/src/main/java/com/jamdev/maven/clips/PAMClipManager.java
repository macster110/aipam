package com.jamdev.maven.clips;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.datavec.audio.Wave;

import com.jamdev.maven.aipam.utils.PamSneUtils;


/**
 * Manages importing of clips and creation of PAMClips. 
 * <p>
 *  
 * @author Jamie Macaulay
 *
 */
public class PAMClipManager {
	
	private ArrayList<PAMClip> currentClips; 
	
	private AudioImporter audioImporter; 
	
	/**
	 * Create the clip manager.
	 */
	public PAMClipManager() {
		
		audioImporter = new StandardAudioImporter(); 
	}
	
	/**
	 * Import the clips from a folders
	 * @return the clips. 
	 */
	public ArrayList<PAMClip> importClips(String filename) {
		ArrayList<PAMClip> pamClips = new ArrayList<PAMClip>();
		
		List<File> files  = PamSneUtils.listFiles(filename, "wav");
		
		//now import each 
		PAMClip pamClip; 
		ArrayList<Wave> waveData; 
		for (File file:files) {
			waveData = audioImporter.importAudio(file); 
			if (waveData!=null) {
				for (Wave wave:waveData) {
					pamClip = new PAMClip(wave); 
					pamClips.add(pamClip); 
				}
			}
		}
		
		currentClips=pamClips; 
		return pamClips; 
	}
	
	
	

	

}
