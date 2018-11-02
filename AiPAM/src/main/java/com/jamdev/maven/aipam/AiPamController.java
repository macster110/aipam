package com.jamdev.maven.aipam;

import java.io.File;
import java.util.ArrayList;

import com.jamdev.maven.aipam.clustering.PamClusterManager;
import com.jamdev.maven.clips.AudioInfo;
import com.jamdev.maven.clips.PAMClip;
import com.jamdev.maven.clips.PAMClipManager;

import javafx.concurrent.Task;

/**
 * Main controller class for AIPAM.
 * 
 * @author Jamie Macaulay
 *
 */
public class AiPamController {


	public int verbose = 1; 

	/**
	 * Start loading audio data from a file
	 */
	public static final int START_FILE_LOAD = 1;

	/**
	 * End loading audio data from a file. 
	 */
	public static final int END_FILE_LOAD =2;

	/**
	 * Start creating spectrogrma images for all the loaded clips. 
	 */
	public static final int START_IMAGE_LOAD =3;

	/**
	 * Flag indicating end of image loading. 
	 */
	public static final int END_IMAGE_LOAD =4;

	/**
	 * Cancelled image load. 
	 */
	public static final int CANCELLED_IMAGE_LOAD =5;


	/**
	 * Message for cancelled data loaded.
	 */
	public static final int CANCELLED_FILE_LOAD=6; 

	/**
	 * Start the clustering algorithm 
	 */
	public static final int START_CLUSTERING_ALGORITHM =7;

	/**
	 * End the clustering algorithm 
	 */
	public static final int END_CLUSTERING_ALGORITHM =8;

	/**
	 * The clustering algorithm has been cancelled. 
	 */
	public static final int CANCEL_CLUSTERING_ALGORITHM = 9;

	/**
	 * There is no audio directory does not exist. 
	 */
	public static final int NO_AUDIO_DIRECTORY = 10;

	/**
	 * End the file header load. 
	 */
	public static final int END_FILE_HEADER_LOAD = 11;


	/**
	 * The cluster manager
	 */
	private PamClusterManager pamClusterManager; 

	/**
	 * Manages importing audio clips. 
	 */
	private PAMClipManager pamClipManager;

	/**
	 * The current parameters for the program. 
	 */
	private AIPamParams aiPamParams;

	/**
	 * Listeners for messages form the controller. 
	 */
	ArrayList<AIMessageListener> aiMessageListeners = new ArrayList<AIMessageListener>();

	/**
	 * The last AiParams use din importing and/or clustering. Primarily used by a GUI
	 * to check whether a new settings files need clips imported again and/or clustered again. 
	 */
	private AIPamParams lastAiParams;

	/**
	 * The main controller. 
	 */
	public AiPamController() {
		this.aiPamParams = new AIPamParams();
		this.pamClipManager = new PAMClipManager(); 
		this.pamClusterManager= new PamClusterManager(); 
	}


	/**
	 * Add a sensor message listener 
	 * @param the aiMessageListener to add. 
	 */
	public void addSensorMessageListener(AIMessageListener aiMessageListener){
		aiMessageListeners.add(aiMessageListener); 
	}

	/**
	 * Update the message listeners.
	 * @param flag - the flag to update. 
	 * @param data - the data object- can be null
	 */
	public void updateMessageListeners(int flag, Object data) {
		for (AIMessageListener listener: aiMessageListeners) {
			listener.newMessage(flag, data);
		}
	}


	/**
	 * Load the audio data. 
	 * @param selectedDirectory - the directory for the clips
	 */
	public void loadAudioData(File selectedDirectory) {
		loadAudioData( selectedDirectory, true); 
	}


	/**
	 * Load the audio data. 
	 * @param selectedDirectory - the directory for the clips
	 * @param loadClips - true to load the clips. False checks the files. 
	 */
	public void loadAudioData(File selectedDirectory, boolean loadClips) {

		this.lastAiParams = aiPamParams.clone(); 
		this.lastAiParams.clusterParams = null; //indicates no clustering has taken place since last audio import. 
		if (!loadClips) lastAiParams.decimatorSR=null; //so that the algorithm know stuff has not been loaded yet. 
				
				
		Task<Integer> task = pamClipManager.importClipsTask(selectedDirectory, this.aiPamParams, loadClips);
		updateMessageListeners(START_FILE_LOAD, task); 

		task.setOnCancelled((value)->{
			//send notification when 
			updateMessageListeners(CANCELLED_FILE_LOAD, task); 
		});
		task.setOnSucceeded((value)->{
			if (loadClips)
				updateMessageListeners(END_FILE_LOAD, task); 
			else 
				updateMessageListeners(END_FILE_HEADER_LOAD, task); 

		});

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start(); 
	}

	/**
	 * Start a thread clustering the data. 
	 */
	public void clusterData() {

	}

	/**
	 * Get the currently loaded clips. 
	 * @return the currently loaded clips. 
	 */
	public ArrayList<PAMClip> getPAMClips() {
		return pamClipManager.getCurrentClips();
	}

	/**
	 * Get the audio info. This gets information. 
	 * @return the audio info.
	 */
	public AudioInfo getAudioInfo() {
		return pamClipManager.getCurrentAudioInfo();
	}


	/**
	 * Get the parameters for AIPAM
	 * @return the main parameters class. 
	 */
	public AIPamParams getParams() {
		return this.aiPamParams;
	}

	/**
	 * Cluster the clips. 
	 */
	public void clusterClips() {

		//keep a copy of the last settings
		if (lastAiParams!=null) {
			this.lastAiParams.clusterParams = aiPamParams.clusterParams.clone();
		}

		Task<Integer> task = pamClusterManager.clusterDataTask(pamClipManager.getCurrentClips(), this.aiPamParams); 
		updateMessageListeners(START_CLUSTERING_ALGORITHM, task); 

		task.setOnCancelled((value)->{
			//send notification when 
			updateMessageListeners(CANCEL_CLUSTERING_ALGORITHM, task); 
		});
		task.setOnSucceeded((value)->{
			//
			updateMessageListeners(END_CLUSTERING_ALGORITHM, task); 
		});

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start(); 
	}

	/**
	 * Get the last params during audio load or clustering. 
	 * @return the last AIPamParams used during audio load and/or clustering. 
	 */
	public AIPamParams getLastAiParams() {
		return lastAiParams;
	}


	/**
	 * Import clips. 
	 */
	public void importClips() {
		File file =new File(aiPamParams.audioFolder);
		if (file.exists()) {
			loadAudioData(new File(aiPamParams.audioFolder)); 
		}
		else {
			System.err.println("The file does not exist: " + aiPamParams.audioFolder);
			updateMessageListeners(NO_AUDIO_DIRECTORY, null); 
		}
	}






}
