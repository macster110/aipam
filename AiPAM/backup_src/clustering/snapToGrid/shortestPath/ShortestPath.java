package com.jamdev.maven.aipam.clustering.snapToGrid.shortestPath;

import com.jamdev.maven.aipam.clustering.snapToGrid.AssignmentProblemAlgorithm;
import com.jamdev.maven.aipam.clustering.snapToGrid.SnapToGridListener;


/**
 * Shortest path algorithm to group snap the cluster points to a grid. 
 * 
 * 
 * @author Jamie Macaulay 
 *
 */
public class ShortestPath implements  AssignmentProblemAlgorithm {
	
	//TODO - add AssignmentProblem dense

	/**
	 * The assignment problem solver. 
	 */
	private AssignmentProblemDense assignMentProblem; 

	/**
	 * The weights
	 */
	private double[][] weights;

	/**
	 * The number of points. 
	 */
	private int nPoints;

	/**
	 * The processing time
	 */
	private long processingTime = 0;

	/**
	 * The listener for updates.
	 */
	private SnapToGridListener listener = null;

	/**
	 * Create the shortest path algorithm
	 * @param weight
	 */
	public ShortestPath(double[][] weight, int nPoints) {
		this.nPoints=nPoints; 
		this.weights=weight; 
	}

	@Override
	public boolean process() {
		long time1 = System.currentTimeMillis();
		System.out.println("Start the shortest path algorithm");
		assignMentProblem = new AssignmentProblemDense(weights); 
		assignMentProblem.setAssignMentListener(listener); 
		assignMentProblem.process();
		System.out.println("End of the shortest path algorithm");
		long time2 = System.currentTimeMillis();
		processingTime  = time2-time1; 
		return true;
	}

	@Override
	public int[] getResult() {
		int[] results = new int[nPoints];
		for (int i=0; i<nPoints; i++) {
			results[i] = assignMentProblem.sol(i);
		}
		return results; 
	}

	@Override
	public void setListener(SnapToGridListener listener) {
		this.listener=listener; 
		
	}

	@Override
	public long getProcessingTime() {
		return processingTime;
	}





}
