package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;

import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.clustering.snapToGrid.SnapToGridListener;

/**
 * Interface for any clustering algorithm.
 * 
 * @author Jamie Macaulay
 *
 */
public interface ClusteringAlgorithm {
	
	/*
	 * The type of clustering algorithm. Used primarily for identifying settings.
	 */
	public String getCLusterType(); 
	
	
	/**
	 * Cluster audio clips. 
	 * @param pamClips - list of clips to cluster. 
	 */
	public void cluster(ArrayList<PAMClip> pamClips, Params clusterParams);


	/**
	 * Get the training listener. 
	 * @return listener - the training listener; 
	 */
	@SuppressWarnings("deprecation")
	public StandardTrainingListener getTrainingListener();


}
