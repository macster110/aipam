package com.jamdev.maven.aipam.clustering.tsne;

import com.jamdev.maven.aipam.clustering.ClusterParams;

/**
 * A parameters class for the TSNE algorithm 
 * @author Jamie Macaulay. 
 *
 */
public class TSNEParams implements ClusterParams {

	/**
	 * The learning rate. 
	 */
	public int initialDim = 30; 

	/**
	 * Effective number of local neighbors of each point, specified as a positive scalar.
	 * Larger perplexity causes tsne to use more points as nearest neighbors. Use a larger value of Perplexity for a 
	 * large dataset. Typical Perplexity values are from 5 to 50. In the Barnes-Hut algorithm, tsne uses min(3*Perplexity,N-1) 
	 * as the number of nearest neighbors.
	 */
	public int perplexity = 30; 

	/**
	 * The theta value for the TSNE algorithm
	 * Barnes-Hut tradeoff parameter, specified as a scalar from 0 through 1. 
	 * Higher values give a faster but less accurate optimization. 
	 * Applies only when Algorithm is  Barnes-Hut.
	 */
	public double theta = 0.5;

	/**
	 * The maximum iterations
	 */
	public int maxIterations = 1000;

	/**
	 * Learning rate (for DeepLearning4j) TSNE
	 */
	@Deprecated
	public int learningRate = 100; 

	/**
	 * Iterations rate (for DeepLearning4j) TSNE
	 */
	@Deprecated
	public int iterations = 100;

	public boolean usePCA = false;

	@Override
	public ClusterParams clone() {

		try {
			return (ClusterParams) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null; 
		}

	}

	@Override
	public boolean compare(ClusterParams clusterParams) {
			
		TSNEParams tsneParams = (TSNEParams) clusterParams;
		
		boolean changed =false; 
		if (this.initialDim!=tsneParams.initialDim) 			changed=true; 
		if (this.perplexity!=tsneParams.perplexity) 			changed=true; 
		if (this.theta!=tsneParams.theta)						changed=true; 
		if (this.maxIterations!=tsneParams.maxIterations) 		changed=true; 
		if (this.usePCA!=tsneParams.usePCA) 					changed=true; 

		return !changed;
	}

}
