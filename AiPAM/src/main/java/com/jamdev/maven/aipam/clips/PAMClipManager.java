package com.jamdev.maven.aipam.clips;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.utils.AiPamUtils;

import javafx.concurrent.Task;


/**
 * Manages importing of clips and creation of PAMClips.
 * <p>
 * How to load pages of clips. A file can contain one or more clips. Each clip
 * comes from a file... Need to load in all clips without spectrogram first,
 * keeping a master list of all clips. Then each clip needs a loaddata function
 * which will load the clip wave data etc.
 * <p>
 * Next question is how to minimize data usage?
 * 
 * Option 1 - Keep a complex array of spectrogram data in memory. This is as
 * many data points as raw data but in 32 bit numbers... (assuming a hop size of
 * zero)! 
 * Option 2 Could keep a short matrix in each PAMClip representing the
 * spectrogram - but that would mean any feature extraction methods requiring
 * the complex array would be difficult...
 *  Option 3 - So why not just keep the raw sound data as a short? Then can r
 * recalculate the spectrograms if we need to? That is processor intensive though... 
 * Option 4 - we keep nothing and reload from file anytime there needs to be a change...
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
	 * The current clip IDs included in the current page. 
	 */
	private int[] currentPageClips = null; 

	/**
	 * Create the clip manager.
	 */
	public PAMClipManager() {
		audioImporter = new StandardAudioImporter(); 		
	}


	/**
	 * Creates a task for importing the clips from a folder. This is the initial clip import that loads all the clips but 
	 * does not save the spectrogram to memory. This leave a list of all available clips with metadata etc. nextPageTask then 
	 * loads the actual spectrogram in the current page.  
	 * 
	 * @param load - true to load clips. False checks the clips. 
	 * @return the task importing clips. 
	 */
	public Task<Integer> importClipsTask(File selectedDirectory, AIPamParams params) {

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
					//					System.out.println("Starting the audio import: Listing files");

					this.updateProgress(-1, 0);

					List<File> files = audioImporter.listAudioFiles(selectedDirectory); 

					//print the files to the console
					int n=0; 
					//					for (File file:files) {
					//						System.out.println(n + ": " +file.getAbsolutePath());
					//					}

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

					//now we need to actually need to load the wav data 
					standAudiListener.setDescription("Importing " );

					//now import each 
					PAMClip pamClip; 
					ArrayList<ClipWave> waveClips; 
					n=0; 
					for (File file:files) {
						if (this.isCancelled()) {
							currentClips=pamClips; 
							return n; //cancel stuff
						}
						waveClips = audioImporter.importAudio(file, params, standAudiListener, false);
						//wave data is designed to be very flexible but the PAMClip class is simple and
						//the same for all detections. Thus use that. 
						if (waveClips!=null) {
							for (ClipWave waveClip:waveClips) {
								if (this.isCancelled()) {
									currentClips=pamClips; 
									return n; //cancel stuff 
								}
								pamClip = new PAMClip(waveClip, params.fftLength, params.fftHop, n); 
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

					//now need to sort the clips by time

					//now need to sort the clips by time and move to the next page. 

					///SORT CLIPS by time - clips implement collection
					Collections.sort(pamClips);

					currentClips=pamClips; 
					
					//make sure this is reset. 
					currentPageClips=null;

					return currentClips.size();
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
	 * Create a new set of clip IDs for the next page. These are the IDs of the clips that will shown in the pane. 
	 * @param params - the current AIParams. 
	 * @param forward - true of the page is moving forward. 
	 */
	private void newClipsIDs(AIPamParams params, boolean forward) {

		//find the clip IDs that we want, remember that the clips are in order of time. 
		if (currentPageClips == null) {
			this.currentPageClips = AiPamUtils.makeArr(0, 1, params.maxPageClips);
			return;
		}

		if (currentClips.size()<=params.maxPageClips) {
			//the order of these is not an issue. 
			this.currentPageClips = AiPamUtils.makeArr(0, 1, currentClips.size()-1);
			return;
		}


		int[] newClipsIds = currentPageClips;
		//so move forward from the last clip and save the IDs - remember the clips are in cronological order. 
		int i = 0;
		int ii = 0;
		if (forward) {
			ii = currentPageClips[currentPageClips.length-1];
			//so move forward from the last clip and save the IDs - remember the clips are in cronological order. 
			while (i<params.maxPageClips && ii<currentClips.size()) {
				newClipsIds[i] = currentClips.get(ii).getGridID();
				i++;
				ii++;
			}
		}
		else {
			ii = currentPageClips[0];
			//so move backward from the last clip and save the IDs - remember the clips are in cronological order. 
			while (i<params.maxPageClips && ii>0) {
				newClipsIds[i] = currentClips.get(ii).getGridID();
				i++;
				ii--;
			}
		}
		
		System.out.println("New page IDs: i " + i + " ii: " + ii); 
		AiPamUtils.printArray(new int[][] {newClipsIds});
		this.currentPageClips = newClipsIds;

		Arrays.sort(currentPageClips);

	}


	/**
	 * Get the task which moves the clips to the next page. Note that this assumes a list of clips already exists and has
	 * been checked. 
	 * @param params - the current AIParams. 
	 * @param forward - true of the page is moving forward. 
	 * @return the task to move to the next page. 
	 */
	public Task<Integer> nextClipPageTask(AIPamParams params, boolean forward) {

		if (currentClips==null) return null; 

		newClipsIDs( params, forward); 

		Task<Integer> task = new Task<Integer>() {

			@Override
			protected Integer call() throws Exception {
				try {

					System.out.println("nextClipPageTask: Start next page task: " + currentPageClips.length); 

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

					
					//must wipe audio data from all clips before loading a new page or the whole page thing is pointless. 
					for (PAMClip pamClip : currentClips) {
						pamClip.setAudioData(null);
					}

					
					ArrayList<String> filesToLoad = new ArrayList<String>(); 

					//first iterate through all the clips and figure out a list of all the files we need to load.
					//(Remember that a file can contain multiple clips...)
					for (int i=0; i<currentClips.size() ; i++) {
						//need to load the clips to 
						if (checkClipInPage(currentClips.get(i), currentPageClips)) {
							//this clip will need new data. 
							if (!filesToLoad.contains(currentClips.get(i).getFileName())) {
								filesToLoad.add(currentClips.get(i).getFileName());
							}
						}
					}

					System.out.println("nextClipPageTask: Start next page task: files to load: " + filesToLoad.size()); 

					ArrayList<ClipWave> waveClips; 
					int count = 0;
					for (String file: filesToLoad) {
						waveClips = audioImporter.importAudio(new File(file), params, standAudiListener, true);

						//TODO - ISSUE here - think it's to do with the TIMEMILLIS COMPARISON
						System.out.println("nextClipPageTask: New wav data: " + waveClips.get(0).getSampleAmplitudes().length + 
								"  First sample: " +  waveClips.get(0).getSampleAmplitudes()[0] + 
								"  TimeMillis: " + waveClips.get(0).getTimeMillis()); 
						
						//now need to update the information in the PAMClip. 
						for (ClipWave waveClip: waveClips) {
							for (PAMClip pamClip : currentClips) {
								if (pamClip.getTimeMillis() == waveClip.getTimeMillis()) {

									pamClip.setAudioData(waveClip.getSpectrogram(params.fftLength, params.fftHop));

									System.out.println("nextClipPageTask: Spectrogram data: " + pamClip.getSpectrogram().getAbsoluteSpectrogram()[3][3]); 
									count++; 
									break; 
								}
							}
						}
					}

					System.out.println("nextClipPageTask: Start next page task: no. spec added: " + count); 

					return 1; 
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
	 * Check whether a clip is on the page. 
	 */
	private boolean checkClipInPage(PAMClip pamClip, int [] newClipIds) {
		int index = Arrays.binarySearch(newClipIds, pamClip.getGridID());
		return index>=0; 
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
