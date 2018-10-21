package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;

import com.jamdev.maven.aipam.utils.SettingsPane;
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
	public void cluster(ArrayList<PAMClip> pamClips);
	
	public SettingsPane getSettingsPane();

}
