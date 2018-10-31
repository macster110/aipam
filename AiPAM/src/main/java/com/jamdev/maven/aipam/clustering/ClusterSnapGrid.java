package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;
import java.util.Arrays;

import com.jamdev.maven.clips.PAMClip;

/**
 * Snaps cluster points to a defined grid. i.e. defines the cluster points and figures out the 
 * best way to snap those points using a Jonker-Volgenant algorithm. 
 * 
 * 
 * @author Jamie Macaulay 
 *
 */
public class ClusterSnapGrid {

	/**
	 * The Jonker-Volgenant algorithm. 
	 */
	LAPJV lapjv;

	public ClusterSnapGrid() {

	}

	/**
	 * Run the algorithm whihc snapps cluster points to a grid
	 * @param clusterPoints - the raw cluster points
	 * @param gridx - the size of the grid in x.
	 * @param gridy - the size of the grid in y. 
	 * @return a list of points 
	 */
	public int[] getClusterGrid(double[][] clusterPoints, int gridx, int gridy) {


		SparseCostMatrix spareseCostMatrix = createCostMatrix(clusterPoints, gridx, gridy); 

		lapjv=new LAPJV(spareseCostMatrix); 
		lapjv.process();

		int[] result =  lapjv.getResult();

		System.out.println("LAPJV complete. The cost is: " + result.length);


		return lapjv.getResult(); 		
	}

	/**
	 * Generate scatter points on a grid for a cost matrix
	 * @return the cost matrix. 
	 */
	private double[][] generateGrid(int gridx, int gridy, int clusterPoints){

		double[][] gridPoints = new double[gridx*gridy][]; 
		double x;
		double y; 
		int n=0; 
		for (int i=0; i<gridx; i++) {
			x = i/(double) gridx;
			for (int j=0; j<gridy; j++) {
				y = j/(double) gridy;
					gridPoints[n]= new double[] {x,y};
					n++;
			}
		}
		
		gridPoints= Arrays.copyOf(gridPoints, n);
		
		System.out.println("N grid points: " + gridPoints.length + " N clustwer points: " + clusterPoints ); 
		
		return gridPoints; 
	}

	/**
	 * Create the cost matrix
	 * @param clusterPoints
	 * @param gridx
	 * @param gridy
	 * @return
	 */
	private SparseCostMatrix createCostMatrix(double[][] clusterPoints, int gridx, int gridy) {	

		double[][] gridPoints = generateGrid(gridx, gridy, clusterPoints.length);

		//calculate the costs 
		double[] cost = new double[gridPoints.length*clusterPoints.length];
		int[] column = new int[gridPoints.length*clusterPoints.length];
		int[] row = new int[gridPoints.length];
		int n=0; 
		int rowk=0; 

		for (int i=0; i<gridPoints.length; i++) {
			rowk=0;
			System.out.println("Calculating cost for: gridPoints: " + i);
			for (int j=0; j<clusterPoints.length; j++) {
				cost[n] = calculateCost(gridPoints[i], clusterPoints[j]); 
				column[n]=i;
				rowk++;
				n++; 
			}
			row[i] = rowk;
		}

		System.out.println("The cost matrix is: " + cost.length + " elements in size: " );

		//now have a very large set of costs. 
		SparseCostMatrix sparse = new SparseCostMatrix(cost, column, row, gridPoints.length); 

		return sparse;
	}

	/**
	 * Calculate the cost between a grid point and cluster 
	 * @param ds - grid point
	 * @param ds2 - cluster point
	 * @return the cost
	 */
	private double calculateCost(double[] ds, double[] ds2) {
		return distance(ds, ds2);
	}

	private double distance(double[] a, double[] b) {
		double diff_square_sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
		}
		return Math.sqrt(diff_square_sum);
	}

	/**
	 * Runs a snap to grid algorithm on the cluster points and sets a gird position for each pamClip.
	 * @param pamClips - the current pamclips. These should contain cluster positions 
	 */
	public void snapToGrid(ArrayList<PAMClip> pamClips) {
		double[][] clusterPoints = new double[pamClips.size()][]; 
		for (int i=0; i<pamClips.size(); i++) {
			clusterPoints[i]=pamClips.get(i).getClusterPoint();
		}

		int[] gridSize = calcGridSize(pamClips.size());

		//run the algorithm; 
		int[] results = getClusterGrid(clusterPoints, gridSize[0], gridSize[1]); 

		//now apply the results to the pamclips. 


	}	



	/**
	 * Calculate the grid size. 
	 * @param nClips - the number of clips. 
	 * @return the grid size. 
	 */
	public static int[] calcGridSize(int nClips) {
		int grid = (int) Math.ceil(Math.sqrt(nClips)); 
		return new int[] {grid, grid};
	}

}
