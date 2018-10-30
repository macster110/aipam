package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;

import org.deeplearning4j.optimize.api.TrainingListener;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.clips.PAMClip;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

/**
 * Manages clusterring on different threads. 
 * @author Jamie Macaulay
 *
 */
public class PamClusterManager {

	/**
	 * The current clustering algorithm
	 */
	private ClusteringAlgorithm clusterAlgorithm = new TSNEClipClustererYK(); 

	/**
	 * Construct the cluster data task. 
	 * @param pamClips  - the pamClips
	 * @param params 	- the aiPamParams
	 * @return the clusterring task.
	 */
	public Task<Integer> clusterDataTask(ArrayList<PAMClip> pamClips, AIPamParams params) {
		return new ClusterTask(pamClips, params); 
	}


	public class ClusterTask extends Task<Integer> {

		private ArrayList<PAMClip> pamClips;

		public ClusterTask(ArrayList<PAMClip> pamClips, AIPamParams params) {
			this.pamClips=pamClips; 
		}

		@Override
		protected Integer call() throws Exception {
			//progress is in intermediate mode. 
			//System.out.println("Hello: Start clustering");
			try {

				updateTitle("Clustering");
				updateMessage("Initialising..."); 
				updateProgress(0, 1);
				updateProgress(ProgressBar.INDETERMINATE_PROGRESS, 1);

				if (clusterAlgorithm.getTrainingListener()!=null) {
					clusterAlgorithm.getTrainingListener().progressProperty().addListener((obsval, oldval, newval)->{
						updateProgress(newval.doubleValue(), 1);
						updateMessage("Processing..."); 
					});
				}

				//start the algorithm 
				updateMessage("Clustering Clips..."); 

				if (pamClips==null) return -1; 
				clusterAlgorithm.cluster(pamClips);

				//System.out.println("Hello: Finished!!!!");
				return -1; 
			}
			catch (Exception e) {
				e.printStackTrace();
				return -1; 
			}
		}


	}


}
