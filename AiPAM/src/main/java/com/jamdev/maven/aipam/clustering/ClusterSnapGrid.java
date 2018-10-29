package com.jamdev.maven.aipam.clustering;

import javafx.concurrent.Task;

/**
 * Snaps cluster points to a defined grid. i.e. defines the cluster points and figures out 
 * 
 * 
 * @author Jamie Macaulay 
 *
 */
public class ClusterSnapGrid {
	
	LAPJV lapjv;
	
	public ClusterSnapGrid() {
		
	}
	
	
	public int[] getClusterGrid(double[][] clusterPoints, int gridx, int gridy) {
		
		SparseCostMatrix spareseCostMatrix = createCostMatrix(clusterPoints, gridx, gridy); 
		
		lapjv=new LAPJV(spareseCostMatrix); 
		lapjv.process();
		
		lapjv.getResult(); 
		
		return null; 
		
	}

	/**
	 * Generate scatter points on a grid for a cost matrix
	 * @return the cost matrix. 
	 */
	private double[][] generateGrid(){
		
		return null; 
	}
	
	/**
	 * Create the cost matrix
	 * @param clusterPoints
	 * @param gridx
	 * @param gridy
	 * @return
	 */
	private SparseCostMatrix createCostMatrix(double[][] clusterPoints, int gridx, int gridy) {
		
		
		
		
		return null;
	}


	/**
	 * Create a task to perform the 
	 * @return
	 */
	public Task<Integer> clusterGirdTask(){
		return null;
		
	}
	

}
