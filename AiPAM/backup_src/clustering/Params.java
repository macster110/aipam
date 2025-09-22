package com.jamdev.maven.aipam.clustering;

import com.jmatio.types.MLStructure;

/**
 * 
 * The parameters for a clustering algorithm 
 * 
 * @author Jamie Macaulay
 *
 */
public interface Params extends Cloneable {

	/**
	 * The clustering algorithm the params are associated with. 
	 */
	public String  paramsType = "tsne";

	/**
	 * Clone these parameters.
	 * @return the cloned cluster parameters. 
	 */
	public Params clone();
	
//	/**
//	 * Get the settings pane for the clustering algorithm 
//	 * @return the settings pane for the clustering algorithm. 
//	 */
//	public SettingsPane<?> getSettingsPane();
	
	/**
	 * Set the parameters of the cluster algorithm using a MATLAB structure
	 * @param mlStruct - the strucutre containing the new params. 
	 */
	public void struct2Params(MLStructure mlStruct); 
	
	/**
	 * Create a MATLAB structure containing the cluster parameters.
	 * @return a structure containing relevant parameters for the structure. 
	 */
	public MLStructure params2Struct(); 

	/**
	 * Compare this cluster to another cluster parameters. 
	 * @param clusterParams - the cluster parameters to compare.
	 * @return true of the parameters in clusterParams is exactly the same as these parameters.
	 */
	public boolean compare(Params clusterParams);
	
}

