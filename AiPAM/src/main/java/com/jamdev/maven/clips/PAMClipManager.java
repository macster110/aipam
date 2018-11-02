package com.jamdev.maven.clips;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jamdev.maven.aipam.AIPamParams;
import javafx.concurrent.Task;


/**
 * Manages importing of clips and creation of PAMClips. 
 * <p>
 *  
 * @author Jamie Macaulay
 *
 */
public class PAMClipManager {

	/**
	 * The current imported audio clips. 
	 */
	private ArrayList<PAMClip> currentClips; 

	/**
	 * The current audio info file. 
	 */
	private AudioInfo currentAudioInfo; 

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
	 * @param load - true to load clips. False checks the clips. 
	 * @return the task importing clips. 
	 */
	public Task<Integer> importClipsTask(File selectedDirectory, AIPamParams params, boolean load) {
		
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {
				//progress is in intermediate mode. 
				try {
					this.updateTitle("Importing Audio Data");
					
					//first run checks. 
					System.out.println("Starting the audio import");

					ArrayList<PAMClip> pamClips = new ArrayList<PAMClip>();

					List<File> files = audioImporter.listAudioFiles(selectedDirectory); 
					for (File file:files) {
						System.out.println(file.getAbsolutePath());
					}
					
					this.updateProgress(-1, files.size());
					this.updateMessage(String.format("Running checks on %d files: ", files.size()));

					//run checks to make sure all sample rates are the same and there are no duplicate file names
					currentAudioInfo = audioImporter.getAudioInfo(selectedDirectory); 
					if (!checkAudio(audioImporter.getAudioInfo(selectedDirectory))) {
						//Send error to error reporter. 
						return -1; 
					} 
					
					if (!load) return files.size(); 

					//now import each 
					PAMClip pamClip; 
					ArrayList<ClipWave> waveData; 
					int n=0; 
					for (File file:files) {
						if (this.isCancelled()) {
							currentClips=pamClips; 
							return n; //cancel stuff
						}
						waveData = audioImporter.importAudio(file, params); 
						if (waveData!=null) {
							for (ClipWave wave:waveData) {
								if (this.isCancelled()) {
									currentClips=pamClips; 
									return n; //cancel stuff 
								}
								pamClip = new PAMClip(wave, params.fttLength, params.fttLength, n); 
								pamClips.add(pamClip); 
								n++;
							}
						}
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
	 * Check audio. 
	 * @param audioInfo - the audio information 
	 * @return audio info. 
	 */
	private boolean checkAudio(AudioInfo audioInfo) {
		if (audioInfo.isSameChannels && audioInfo.isSameSampleRate) return true; 
		return false;
	}

	/**
	 * Get the currently loaded clips. 
	 * @return the currently loaded clips
	 */
	public ArrayList<PAMClip> getCurrentClips() {
		return currentClips;
	}
	
	/**
	 * Get the current audio info.
	 * @return the audio info. 
	 */
	public AudioInfo getCurrentAudioInfo() {
		return currentAudioInfo;
	}






}
