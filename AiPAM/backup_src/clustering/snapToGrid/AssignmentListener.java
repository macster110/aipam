package com.jamdev.maven.aipam.clustering.snapToGrid;

/**
 * Listener for the LAPJV algorithm 
 *
 * @author Jamie Macaulay
 *
 */
public interface AssignmentListener {
		
	public void columnReduction(); 
	
	public void reductionTransfer(); 

	public void augmentingRowReduction(int f0, int k, int count);

	public void augmenting();
	
	public boolean isCancelled(); 


}
