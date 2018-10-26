package com.jamdev.maven.aipam.clustering;


/**
 * A parameters class for the TSNE algorithm 
 * @author Jamie Macaulay. 
 *
 */
public class TSNEParams implements ClusterParams {
	
	/**
	 * The learning rate. 
	 */
	public int learningRate = 500; 
	
	public int perplexity = 30; 
	
	/**
	 * The epislon value of the 
	 */
	public int epsilon = 5;

	public int iterations =100; 

}
