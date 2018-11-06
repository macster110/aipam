package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;

import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.layout.utilsFX.SettingsPane;
import com.jmatio.types.MLStructure;

/**
 * Interface for any clustering algorithm 
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
	public void cluster(ArrayList<PAMClip> pamClips, ClusterParams clusterParams);
	
	/**
	 * Get the settings pane for the clustering algorithm 
	 * @return the settings pane for the clustering algorithm. 
	 */
	public SettingsPane getSettingsPane();
	
	/**
	 * Set the parameters of the cluster algorithm using a MATLAB structure
	 * @param mlStruct - the strucutre containing the new params. 
	 */
	public void struct2ClusterParams(MLStructure mlStruct); 
	
	/**
	 * Create a MATLAB strucutre containing the cluster parameters.
	 * @return a strucutre containing relevent parameters for the structure. 
	 */
	public MLStructure clusterParams2Struct(); 


	/**
	 * Get the training listener. 
	 * @return listener - the training listener; 
	 */
	public StandardTrainingListener getTrainingListener();


}
