package com.jamdev.maven.clips;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.datavec.audio.Wave;

import com.jamdev.maven.aipam.utils.AiPamUtils;

import javafx.concurrent.Task;


/**
 * Manages importing of clips and creation of PAMClips. 
 * <p>
 *  
 * @author Jamie Macaulay
 *
 */
public class PAMClipManager {

	private ArrayList<PAMClip> currentClips; 

	/**
	 * Audio importer. 
	 */
	private AudioImporter audioImporter; 

	/**
	 * Create the clip manager.
	 */
	public PAMClipManager() {
		audioImporter = new StandardAudioImporter(); 
	}


	/**
	 * Creates a task for importing the clips from a folder. 
	 * @return the task importing clips. 
	 */
	public Task<Integer> importClipsTask(File selectedDirectory) {

		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {
				//progress is in intermediate mode. 
				try {
					this.updateTitle("Importing Audio Data");

					System.out.println("Starting the audio import");

					ArrayList<PAMClip> pamClips = new ArrayList<PAMClip>();

					List<File> files  = AiPamUtils.listFiles(selectedDirectory.getAbsolutePath(), "wav");
					for (File file:files) {
						System.out.println(file.getAbsolutePath());
					}

					
					//now import each 
					PAMClip pamClip; 
					ArrayList<Wave> waveData; 
					int n=0; 
					for (File file:files) {
						if (this.isCancelled()) return n; //cancel stuff
						waveData = audioImporter.importAudio(file); 
						if (waveData!=null) {
							for (Wave wave:waveData) {
								if (this.isCancelled()) return n; //cancel stuff 
								pamClip = new PAMClip(wave); 
								pamClips.add(pamClip); 
							}
						}
						n++;
						System.out.println("Loaded " + n + " of " + files.size() + " wave: " + waveData.size());
						this.updateProgress(n, files.size());
						this.updateMessage(String.format("Importing %s", file.getName()));
					}
					currentClips=pamClips; 

					return pamClips.size();
				}
				catch (Exception e) {
					e.printStackTrace();
					return -1; 
				}
			}
		};
		return task; 
	}







}
