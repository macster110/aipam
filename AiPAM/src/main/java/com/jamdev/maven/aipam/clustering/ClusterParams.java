package com.jamdev.maven.aipam.clustering;

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

	/**
	 * Compare this cluster to another cluster parameters. 
	 * @param clusterParams - the cluster parameters to compare.
	 * @return true of the parameters in clusterParams is exactly the same as these parameters.
	 */
	public boolean compare(ClusterParams clusterParams);
	
}

