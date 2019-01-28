package com.jamdev.maven.aipam.clustering.snapToGrid;

import java.util.ArrayList;
import java.util.Arrays;

import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.clustering.snapToGrid.LAPJV1.SparseCostMatrix;
import com.jamdev.maven.aipam.clustering.snapToGrid.shortestPath.ShortestPath;
import com.jamdev.maven.aipam.utils.AiPamUtils;

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
	 * The assignment algorithm 
	 */
	private AssignmentProblemAlgorithm lapjv;

	/**
	 * Listener for the cluster snap to grid algorithm 
	 */
	private SnapToGridListener listener;



	/**
	 * Get the snap to grid listener. This is called on updates and used to pass cancel messages to thread. 
	 * @return the snap to grid listener
	 */
	public SnapToGridListener getListener() {
		return listener;
	}
	
	


	/**
	 * Run the algorithm which snaps cluster points to a grid
	 * @param clusterPoints - the raw cluster points
	 * @param gridx - the size of the grid in x.
	 * @param gridy - the size of the grid in y. 
	 * @return a list of points 
	 */
	public int[] getClusterGrid(double[][] clusterPoints, int gridx, int gridy) {
	
		SparseCostMatrix spareseCostMatrix = createCostMatrix(clusterPoints, gridx, gridy); 

		//set the algorithm 
		lapjv=new ShortestPath(spareseCostMatrix.toFullMatrix(), clusterPoints.length); 
		//		//an LAPJV algorithm
		//		lapjv=new LAPJV(spareseCostMatrix); 
		//		//the LAPJV2 algorithm<- a carbon copy of the C code. 
		//		lapjv=new LAPJV2(spareseCostMatrix.toFullMatrix()); 

		lapjv.setListener(listener);
		lapjv.process();

		int[] result =  lapjv.getResult();

		if (result!=null) {
			System.out.println("LAPJV complete. The cost is: " + result.length +
					" and it took: " +  (lapjv.getProcessingTime()/1000.) + " seconds");
		}
		else {
			System.out.println("LAPJV cancelled");
		}


		return lapjv.getResult(); 		
	}

	/**
	 * Generate scatter points on a grid for a cost matrix
	 * @return the grid to help calc cost matrix
	 */
	public static double[][] generateGrid(int gridx, int gridy, double size){

		double[][] gridPoints = new double[gridx*gridy][]; 
		double x;
		double y; 
		int n=0; 
		for (double i=0; i<gridx; i++) {
			x = i/(double) gridx;
			for (double j=0; j<gridy; j++) {
				y = j/(double) gridy;
				gridPoints[n]= new double[] {size*x,size*y};
				n++;
			}
		}
		//System.out.println("N grid points: " + gridPoints.length + " N clustwer points: " + clusterPoints ); 
		return gridPoints; 
	}

	/**
	 * Create the cost matrix
	 * @param clusterPoints - the cluster points
	 * @param gridx - the size of the grid in x
	 * @param gridy - the size of the grid of y
	 * @return the sparse cosrt matrix for the grid and the points. 
	 */
	private SparseCostMatrix createCostMatrix(double[][] clusterPoints, int gridx, int gridy) {	

		//normalise the cluster points. 
		//AiPamUtils.normalise(clusterPoints, 1);

		double[][] gridPoints = generateGrid(gridx, gridy, AiPamUtils.max(clusterPoints));
		gridPoints= Arrays.copyOf(gridPoints, clusterPoints.length);

		//calculate the costs 
		double[] cost = new double[gridPoints.length*clusterPoints.length];
		int[] column = new int[gridPoints.length*clusterPoints.length];
		int[] row = new int[gridPoints.length];
		int n=0; 
		int rowk=0; 

		for (int i=0; i<clusterPoints.length; i++) {
			rowk=0;
			//System.out.println("Calculating cost for: gridPoints: " + i);
			for (int j=0; j<gridPoints.length; j++) {
				cost[n] = calculateCost(gridPoints[j], clusterPoints[i]); 
				column[n]=j;
				rowk++;
				n++; 
			}
			row[i] = rowk;
		}

		//normalise the costs to 10000 to stop the LAPJV algorithm from potential looping...FOREVER/
		//until it reaches floating point precision...
		AiPamUtils.normalise(cost, 10000.);
		System.out.println("The cost matrix is: " + cost.length + " elements in size: " );
		System.out.println("The column matrix is: " + column.length + " elements in size: " );
		System.out.println("The row matrix is: " + row.length + " elements in size: " );

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

	/**
	 * Calculate the distance between two points. 
	 * @param a - point a
	 * @param b - point b
	 * @return the distance. 
	 */
	public static double distance(double[] a, double[] b) {
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

		if (results!=null) {
			for (int i=0; i<pamClips.size(); i++) {
				pamClips.get(i).setGridID(results[i]); 
			}
		}
		//now apply the results to the pam clips. 
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

	
	/**
	 * Set the listener. 
	 * @param snapToGridListener - the snap to grid listener. 
	 */
	public void setListener(SnapToGridListener snapToGridListener) {
		this.listener=snapToGridListener;
	}




}
