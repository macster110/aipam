package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;

import org.deeplearning4j.optimize.api.TrainingListener;

import com.jamdev.maven.aipam.layout.utilsFX.SettingsPane;
import com.jamdev.maven.clips.PAMClip;

/**
 * Interface for any clustering algorithm 
 * @author Jamie Macaulay
 *
 */
public interface ClusteringAlgorithm {
	
	/**
	 * Cluster audio clips. 
	 * @param pamClips - list of clips to cluster. 
	 */
	public void cluster(ArrayList<PAMClip> pamClips, ClusterParams clusterParams);
	
	/**
	 * Get the settings pane for the clustering algorithm 
	 * @return the settings pane for the clustering algorithm. 
	 */
	public SettingsPane getSettingsPane();

	/**
	 * Get the training listener. 
	 * @return listener - the training listener; 
	 */
	public StandardTrainingListener getTrainingListener();


}
