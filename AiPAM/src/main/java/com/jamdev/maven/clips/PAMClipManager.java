package com.jamdev.maven.clips;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jamdev.maven.aipam.PamSneUtils;


/**
 * Manages importing of clips and creation of PAMClips. 
 * <p>
 *  
 * @author Jamie Macaulay
 *
 */
public class PAMClipManager extends PAMClip {
	
	private ArrayList<PAMClip> currentClips; 
	
	/**
	 * Create the clip manager.
	 */
	private PAMClipManager() {
		
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
		for (File file:files) {
			pamClip = new PAMClip(); 
			if (pamClip.importWav(file)==PAMClipError.NO_ERROR) {
				pamClips.add(pamClip); 
			} 
		}
		
		currentClips=pamClips; 
		return pamClips; 
	}
	

	

}
