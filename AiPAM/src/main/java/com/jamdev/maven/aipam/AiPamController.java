package com.jamdev.maven.aipam;

import java.io.File;
import java.util.ArrayList;

import com.jamdev.maven.aipam.clustering.PamClusterManager;
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
	 * The clusterring algorithm has been cancelled. 
	 */
	private static final int CANCEL_CLUSTERING_ALGORITHM = 9;

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
	 * @param selectedDirectory
	 */
	public void loadAudioData(File selectedDirectory) {
		
		Task<Integer> task = pamClipManager.importClipsTask(selectedDirectory, this.aiPamParams);
		updateMessageListeners(START_FILE_LOAD, task); 
		
		task.setOnCancelled((value)->{
			//send notification when 
			updateMessageListeners(CANCELLED_FILE_LOAD, task); 
		});
		task.setOnSucceeded((value)->{
			updateMessageListeners(END_FILE_LOAD, task); 
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
		// TODO Auto-generated method stub
		return pamClipManager.getCurrentClips();
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

	
	

}
