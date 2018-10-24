package com.jamdev.maven.clips;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jamdev.maven.aipam.AIPamParams;
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
	public Task<Integer> importClipsTask(File selectedDirectory, AIPamParams params) {

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
					ArrayList<ClipWave> waveData; 
					int n=0; 
					for (File file:files) {
						if (this.isCancelled()) {
							currentClips=pamClips; 
							return n; //cancel stuff
						}
						waveData = audioImporter.importAudio(file, params.channel, params.maximumClipLength); 
						if (waveData!=null) {
							for (ClipWave wave:waveData) {
								if (this.isCancelled()) {
									currentClips=pamClips; 
									return n; //cancel stuff 
								}
								pamClip = new PAMClip(wave, params.fttLength, params.fttLength); 
								pamClips.add(pamClip); 
							}
						}
						n++;
						double memoryMB = Runtime.getRuntime().totalMemory()/1000./1000.; 
						//System.out.println("Loaded " + n + " of " + files.size() + " wave: " + waveData.size() + " Memory usage: " + memoryMB + "MB");
						this.updateProgress(n, files.size());
						this.updateMessage(String.format("Importing %s | Memory usage: %.2f MB", file.getName(), memoryMB));
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

	/**
	 * Get the currently loaded clips. 
	 * @return the currently loaded clips
	 */
	public ArrayList<PAMClip> getCurrentClips() {
		return currentClips;
	}





}
