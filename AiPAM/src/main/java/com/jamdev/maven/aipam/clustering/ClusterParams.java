package com.jamdev.maven.aipam.clustering;

/**
 * The parameters for a clustering algorithm 
 * @author Jamie Macaulay
 *
 */
public interface ClusterParams extends Cloneable {

	/**
	 * The clustering algorithm the params are associated with. 
	 */
	public String  paramsType = "tsne";

	public ClusterParams clone();
}

