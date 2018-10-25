package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;

import org.deeplearning4j.optimize.api.TrainingListener;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.clips.PAMClip;

import javafx.concurrent.Task;

public class PamClusterManager {

	/**
	 * The current clusterring algorithm
	 */
	private ClusteringAlgorithm clusterAlgorithm = new TSNEClipClusterer(); 
	
	
	public Task<Integer> clusterDatyaTask(ArrayList<PAMClip> pamClips, AIPamParams params) {
		
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {
				//progress is in intermediate mode. 
				try {
					clusterAlgorithm.setTrainingListener(new TSNETrainingListener());
					clusterAlgorithm.cluster(pamClips);
					return -1; 
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
