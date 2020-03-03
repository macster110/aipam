package com.jamdev.maven.aipam.clustering.snapToGrid;
/**
 * The assignment problem for matching cluster points to a grid. 
 * 
 * @author Jamie Macaulay 
 *
 */
public interface AssignmentProblemAlgorithm {
	
	/**
	 * Start the algorithm 
	 */
	public boolean process(); 
	
	/**
	 * Get the results from the assignment algorithm. 
	 * @return the results. 
 	 */
	public int[] getResult();

	/**
	 * Set the listener for algorithm updates.
	 * @param listener - the listener
	 */
	public void setListener(SnapToGridListener listener);
	
	/**
	 * Get the processing time.
	 * @return the processing time. 
	 */
	public long getProcessingTime(); 
	
}
