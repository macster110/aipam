package com.jamdev.maven.aipam.clustering;

import com.jamdev.maven.aipam.layout.utilsFX.SettingsPane;
import com.jmatio.types.MLStructure;

/**
 * 
 * The parameters for a clustering algorithm 
 * 
 * @author Jamie Macaulay
 *
 */
public interface ClusterParams extends Cloneable {

	/**
	 * The clustering algorithm the params are associated with. 
	 */
	public String  paramsType = "tsne";

	/**
	 * Clone these parameters.
	 * @return the cloned cluster parameters. 
	 */
	public ClusterParams clone();
	
//	/**
//	 * Get the settings pane for the clustering algorithm 
//	 * @return the settings pane for the clustering algorithm. 
//	 */
//	public SettingsPane<?> getSettingsPane();
	
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
	 * Compare this cluster to another cluster parameters. 
	 * @param clusterParams - the cluster parameters to compare.
	 * @return true of the parameters in clusterParams is exactly the same as these parameters.
	 */
	public boolean compare(ClusterParams clusterParams);
	
}

