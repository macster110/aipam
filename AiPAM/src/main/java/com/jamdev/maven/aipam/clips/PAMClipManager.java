package com.jamdev.maven.aipam.clips;

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
			
			@Override
			protected Integer call() throws Exception {
				//progress is in intermediate mode. 
				ArrayList<PAMClip> pamClips = new ArrayList<PAMClip>();
				
				StandardAudioImportListener standAudiListener = new StandardAudioImportListener(); 
							
				//add a listener to the progress files 
				standAudiListener.progressProperty().addListener((obsval, oldval, newval)->{
					updateProgress(newval.doubleValue(), 1);
					//bit of a hack but meh
					if (standAudiListener.getNFiles()>0) {
						double memoryMB = Runtime.getRuntime().totalMemory()/1000./1000.; 

						updateMessage(standAudiListener.getDescription()+ String.format(" %d of %d files | Memory usage: %.2f MB", 
								standAudiListener.getFileN(), standAudiListener.getNFiles(), memoryMB)); 
					}
				});
	
				try {
					this.updateTitle("Importing Audio Data");
					
					//first run checks. 
					System.out.println("Starting the audio import: Listing files");

					this.updateProgress(-1, 0);
					List<File> files = audioImporter.listAudioFiles(selectedDirectory); 
					
					//print the files to the console
					int n=0; 
					for (File file:files) {
						System.out.println(n + ": " +file.getAbsolutePath());
					}
					
					this.updateProgress(-1, 0);
					standAudiListener.setDescription("Running checks on " );
					//run checks to make sure all sample rates are the same and there are no duplicate file names. 
					//standAudiListener handles progress updates., 
					currentAudioInfo = audioImporter.getAudioInfo(selectedDirectory, standAudiListener); 
					
		
					if (!checkAudio(currentAudioInfo)) {
						//Send error to error reporter. 
						System.out.println("Error in files - check did not work");
						return -1; 
					} 
					
					if (!load) return files.size(); 
					
					standAudiListener.setDescription("Importing " );

					
					//now import each 
					PAMClip pamClip; 
					ArrayList<ClipWave> waveData; 
					n=0; 
					for (File file:files) {
						if (this.isCancelled()) {
							currentClips=pamClips; 
							return n; //cancel stuff
						}
						waveData = audioImporter.importAudio(file, params, standAudiListener); 
						if (waveData!=null) {
							for (ClipWave wave:waveData) {
								if (this.isCancelled()) {
									currentClips=pamClips; 
									return n; //cancel stuff 
								}
								pamClip = new PAMClip(wave, params.fftLength, params.fftLength, n); 
								pamClips.add(pamClip); 
								n++;
							}
						}
						
						//directly update progress.
						standAudiListener.updateProgress((n/(double) files.size()), n, files.size());
						
//						double memoryMB = Runtime.getRuntime().totalMemory()/1000./1000.; 
//						//System.out.println("Loaded " + n + " of " + files.size() + " wave: " + waveData.size() + " Memory usage: " + memoryMB + "MB");
//						this.updateProgress(n, files.size());
//						this.updateMessage(String.format("Importing %s | Memory usage: %.2f MB", file.getName(), memoryMB));
					}
					currentClips=pamClips; 

					return pamClips.size();
				}
				catch (Exception e) {
					System.out.println("There was an error in the pamClips: " + pamClips==null? "null":pamClips.size());
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
		if (audioInfo!=null && audioInfo.isSameChannels && audioInfo.isSameSampleRate) return true; 
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
