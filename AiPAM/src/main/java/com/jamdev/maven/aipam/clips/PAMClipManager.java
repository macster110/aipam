package com.jamdev.maven.aipam.clips;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.utils.AiPamUtils;

import ai.djl.util.Utils;
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
					standAudiListener.setDescription("Running checks on");

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
						//System.out.println("Filename: " + FilenameUtils.getBaseName(file.getAbsolutePath())); 
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
								
//								System.out.println("New PAMClip duration: 1: " +  waveClip.getLengthInSeconds() + " samples: " + waveClip.getSampleAmplitudes().length);
								pamClip = new PAMClip(waveClip, n); 
								pamClip.clearAudioData(); // don't want to be storing any audio data here - just in case. 
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


					///sort the clips by time - clips implement collection
					Collections.sort(pamClips);
					for (int i=0; i<pamClips.size(); i++) {
						System.out.println("Filename: " + pamClips.get(i).getClipName() + " ID: " +  pamClips.get(i).getGridID()); 
					}
					
					//now set the clip IDs so they are in order of time
					for (int i=0; i<pamClips.size(); i++) {
						pamClips.get(i).setGridID(i);
					}
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
	private synchronized boolean newClipsIDs(AIPamParams params, boolean forward) {
		
		/**
		 * NOTE: be extremely careful here - any changes to array length and signs e.g. <= can result in lots 
		 * of weird errors. A nasty bit of code but it deals with a bunch of scenarios such as clustered clips 
		 * being out of time order etc. 
		 */
		int[] newClipsIds;
		if (currentPageClips == null || currentClips.size()<=params.maxPageClips) {

			int size = Math.min(currentClips.size(), params.maxPageClips);
			
			newClipsIds = new int[size];
			//the clips are in chronological order. 
			for (int i=0; i<size; i++) {
				newClipsIds[i] = currentClips.get(i).getGridID();
			}
			this.currentPageClips = newClipsIds;
			AiPamUtils.printArray(new int[][] {currentPageClips});
		}

		//TODO - need to search for the min and max time index 0f the clips because they may have been moved out of order by
		//a clustering algorithm? - safer thing to do anyways. 
		else {
			newClipsIds = new int[params.maxPageClips];
			//so move forward from the last clip and save the IDs - remember the clips are in chronological order. 
			int i = 0;
			int ii = 0;
			if (forward) {

				ii = clipIndex(currentClips, clipMinMaxIndex(currentPageClips)[1])+1;
				System.out.println("Clip start index A: " + ii + " " + "  " + clipMinMaxIndex(currentPageClips)[1]); 
				if (ii>=currentClips.size()-1) return false; //keep same clip IDs
				//so move forward from the last clip and save the IDs - remember the clips are in chronological order. 
				while (i<params.maxPageClips && ii<currentClips.size()) {
					newClipsIds[i] = currentClips.get(ii).getGridID();
					i++;
					ii++;
				}
			}
			else {
				ii = clipIndex(currentClips, clipMinMaxIndex(currentPageClips)[0])-1;
				System.out.println("Clip start index B: " + ii + " " + currentClips.size() + "  " +  currentPageClips[currentPageClips.length-1]); 
				if (ii<0) return false; //keep same clip IDs
				//if (ii-params.maximumClipLength<0) return; //keep same clip IDs
				//so move backward from the last clip and save the IDs - remember the clips are in chronological order. 
				while (i<params.maxPageClips && ii>=0) {
					newClipsIds[i] = currentClips.get(ii).getGridID();
					i++;
					ii--;
				}
			}
			if (i>0) {
				//is not i-1 because i is an index and for this is a length. Because i++ at end of loop 
				//it's equal to the length of array, already. 
				newClipsIds = Arrays.copyOf(newClipsIds, i);
			}
			System.out.println("PamClipManager: New page IDs: i " + i + " ii: " + ii + " len: " + newClipsIds.length); 
		}
		this.currentPageClips = newClipsIds;
		//very import as search function assume a sorted array
		//Arrays.sort(currentPageClips);

		//AiPamUtils.printArray(new int[][] {currentPageClips});
		return true;
	}


	//	/**
	//	 * Get the clip start and end index corresponding to the ID of the clips with the min and max time. 
	//	 * @param newClipIds - the clips to find the min and max for. 
	//	 * @return the clip IDs with the min and max time respectively. 
	//	 */
	//	private int[] minMaxTimeIndex(int[] newClipIds) {
	//		for(int i = 0 ; i<newClipIds.length; i++){
	//			if(newClipIds[i] == clipID)
	//				return i;
	//		}
	//		return -1;
	//	}

	/**
	 * Check what position the clipsID is in a list of clips. 
	 */
	private int clipIndex(ArrayList<PAMClip> currentClips, int clipID) {
		for(int i = 0 ; i<currentClips.size(); i++){
			if(currentClips.get(i).getGridID() == clipID)
				return i;
		}
		return -1;
		//cannot have sorted clips because they need to to be in order of time.
		//		int index = Arrays.binarySearch(newClipIds, pamClip.getGridID());
		//		return index>=0; 
	}
	//
	//	/**
	//	 * Get the page clips. 
	//	 * @return the page clips. 
	//	 */
	//	private ArrayList<PAMClip> getClips(int[] clipIDs){
	//		ArrayList<PAMClip> pageClips = new  ArrayList<PAMClip> (); 
	//		
	//		int ind;
	//		for (int i=0; i<clipIDs.length ; i++) {
	//			//need to load the clips to 
	//			ind = clipIndex(currentClips, clipIDs[i])
	//			if (checkClipInPage(currentClips.get(i), clipIDs)) {
	//				pageClips.add(currentClips.get(i)); 
	//			}
	//		}
	//		return pageClips;
	//	}

	/**
	 * Get the index of the clips specified by clipIDs which have the 
	 * lowest and highest time value. 
	 */
	private int[] clipMinMaxIndex(int[] clipIDs) {

		long maximum = Long.MIN_VALUE;
		long minimum = Long.MAX_VALUE;
		int iMax = -1; 
		int iMin = -1; 

		PAMClip clip; 
		int ind; 
		for(int i = 0 ; i<clipIDs.length; i++){

			ind = clipIndex(currentClips, clipIDs[i]);
			if (ind<0) continue;
			clip = currentClips.get(ind); 

			if (clip.getTimeMillis()<minimum) {
				minimum =clip.getTimeMillis();
				iMin = i; 
			}	
			if (clip.getTimeMillis()>maximum) {
				maximum = clip.getTimeMillis();
				iMax = i; 
			}
		}

		return new int[] {clipIDs[iMin], clipIDs[iMax]};
		//cannot have sorted clips because they need to to be in order of time.
		//		int index = Arrays.binarySearch(newClipIds, pamClip.getGridID());
		//		return index>=0; 
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

		if (!newClipsIDs( params, forward)) return null;

		Task<Integer> task = new Task<Integer>() {

			@Override
			protected Integer call() throws Exception {
				try {

					//System.out.println("nextClipPageTask: Start next page task: " + currentPageClips.length); 

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

//					System.out.println("PamClipManager: nextClipPageTask: Start next page task: WIPE CLIPS: " + currentClips.size() + "  page clips: " + currentPageClips.length); ;
//					for (int i=0; i<currentPageClips.length ; i++) {
//						System.out.print(currentPageClips[i] + " ");
//					}
					
					System.out.println("PamClipManager: nextClipPageTask: Start next page task: WIPE CLIPS: " + currentClips.size() + "  page clips: " + currentPageClips.length); ;
					for (int i=0; i<currentPageClips.length ; i++) {
						System.out.print(currentClips.get(i).getGridID() + " ");
					}
					
					
					//must wipe audio data from all clips before loading a new page or the whole page thing is pointless. 
					for (PAMClip pamClip : currentClips) {
						pamClip.clearAudioData();
					}
					
					System.out.println("PamClipManager: no. clips with spectrgram: " + getNClipsWithImage() + " with audio " + getNClipsWithAudio()); ;



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

					System.out.println("PAMClipManager: nextClipPageTask: Start next page task: files to load: " + filesToLoad.size()); 
					ArrayList<ClipWave> waveClips; 
					int count = 0;
					String file; 
					for (int i=0; i<filesToLoad.size(); i++) {
						file = filesToLoad.get(i);
						waveClips = audioImporter.importAudio(new File(file), params, standAudiListener, true);
////						//TODO - ISSUE here - think it's to do with the TIMEMILLIS COMPARISON
//						System.out.println("-----------------------"); 
//						System.out.println("PAMClipManager: nextClipPageTask: New wav data: " + i + "  " + waveClips.get(0).getSampleAmplitudes().length + 
//								"  First sample: " +  waveClips.get(0).getSampleAmplitudes()[0] + 
//								"  TimeMillis: " + waveClips.get(0).getTimeMillis() + " " + filesToLoad.get(i)); 
//						
						
						//now need to update the information in the PAMClip. Note that there may be multiple clips in a file so need to match by time.
						boolean found = false;
						for (ClipWave waveClip: waveClips) {
							found=false;
							for (PAMClip pamClip : currentClips) {
								//it's important to compare the filename here too just incase there are clips with the same time in different files
								if (pamClip.getTimeMillis() == waveClip.getTimeMillis() && pamClip.getFileName().equals(waveClip.getFileName())) {
									pamClip.setAudioData(waveClip);
									//System.out.println("nextClipPageTask: Spectrogram data: " + pamClip.getGridID()); 
									count++; 
									found= true;
									break; 
								}
							}
							if (!found) System.out.println("PAMClipManager: nextClipPageTask: unable to find clips for time: " + i + " " + waveClip.getTimeMillis() + " file: " + waveClip.getFileName());
						}
					}
					System.out.println("PAMClipManager: nextClipPageTask: Start next page task: no. audio added: " + getNClipsWithAudio() + "  count: " + count); 

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
		for(int s: newClipIds){
			if(s == pamClip.getGridID())
				return true;
		}
		return false;
		//cannot have sorted clips because they need to to be in order of time.
		//		int index = Arrays.binarySearch(newClipIds, pamClip.getGridID());
		//		return index>=0; 
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

	/**
	 * Check whether there clips which are not shown currently in the page to be loaded. 
	 * @return true of there are clips beyond the current page to be loaded. 
	 */
	public boolean hasNextClips() {

		if (currentClips == null) return false; 
		else if (currentPageClips==null) return true; //something weird going on

		int ii = clipIndex(currentClips, clipMinMaxIndex(currentPageClips)[1])+1;

		//System.out.println("Next clips: " + ii + "  of " +  currentClips.size()); 
		if (ii>=currentClips.size()-1) return false; 

		return true; 

	}

	/**
	 * Check whether there are clips which are not shown currently in the page to be loaded. 
	 * @return true of there are clips before the current page to be loaded. 
	 */
	public boolean hasPrevClips() {
		if (currentClips == null) return false; 
		else if (currentPageClips==null) return true; //something weird going on

		int ii = clipIndex(currentClips, clipMinMaxIndex(currentPageClips)[0]);
		//System.out.println("Prev clips: " + ii + "  of " +  currentClips.size() +  "  min grid ID: " + clipMinMaxIndex(currentPageClips)[0]); 
		if (ii<=0) return false;

		return true; 
	}
	
	public int getNClipsWithImage() {
		int count=0;
		for (int i=0; i<currentClips.size(); i++) {
			if (currentClips.get(i).getSpectrogram()!=null) {
				count++;
			}
		}
		return count; 
	}
	
	public int getNClipsWithAudio() {
		int count=0;
		for (int i=0; i<currentClips.size(); i++) {
			if (currentClips.get(i).getClipWave().getSampleAmplitudes()!=null) {
				count++;
			}
		}
		return count; 
	}
	


}
