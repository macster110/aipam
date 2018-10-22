package com.jamdev.maven.aipam;

import java.io.File;
import java.util.ArrayList;

import com.jamdev.maven.aipam.clustering.ClusteringAlgorithm;
import com.jamdev.maven.aipam.clustering.TSNEClipClusterer;
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
	 * Message for cancelled data loaded.
	 */
	public static final int CANCELLED_FILE_LOAD=5; 
	
	/**
	 * Start the clustering algorithm 
	 */
	public static final int START_CLUSTERING_ALGORITHM =6;

	/**
	 * End the clustering algorithm 
	 */
	public static final int END_CLUSTERING_ALGORITHM =7;


	
	/**
	 * Manages importing audio clips. 
	 */
	private PAMClipManager pamClipManager;
	
	/**
	 * The current set of clips to be analysed. 
	 */
	private ArrayList<PAMClip> pamClips;

	/**
	 * The current parameters for the program. 
	 */
	private AIPamParams aiPamParams;
	
	/**
	 * The current clusterring algorithm
	 */
	private ClusteringAlgorithm clusterAlgorithm = new TSNEClipClusterer(); 
	
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
	}
	
	public void importClips() {
		
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
		
		Task<Integer> task = pamClipManager.importClipsTask(selectedDirectory);
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
	
	

}
