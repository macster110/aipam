package com.jamdev.maven.aipam.clustering.snapToGrid;

import com.jamdev.maven.aipam.clustering.snapToGrid.LAPJV1.SparseCostMatrix;
import com.jamdev.maven.aipam.clustering.snapToGrid.LAPJV2.LAPJV2;

/**
 * Testing how to use the sparse cost matrix. 
 * @author Jamie Macaulay 
 *
 */
public class SparseCostMatrixTests {
	
	 public static void print1D(int mat[]) {
		  for (int j = 0; j < mat.length; j++) 
              System.out.print(String.format("%d ",mat[j])); 
	 }

	
	 public static void print2D(double mat[][]) 
	    { 
	        // Loop through all rows 
	        for (int i = 0; i < mat.length; i++) {
	        	 System.out.println(""); 
	            // Loop through all elements of current row 
	            for (int j = 0; j < mat[i].length; j++) 
	                System.out.print(String.format("%.2f ",mat[i][j])); 
	        }
	    } 

	public static void main(String[] args) {

		int gridsize=40;

		//generate a grid points we want to snap stuff to 
		double[][] gridPoints = ClusterSnapGrid.generateGrid(gridsize,gridsize, 1.); 

		//generate the data points; 
		double[][] dataPoints = new double[gridsize*gridsize][]; 
		
		
		for (int i=0; i<gridsize*gridsize; i++) {
			dataPoints[i]=new double[] {Math.random(), Math.random()};
		}

		//generate the cost
		double[] cost = new double[gridPoints.length*dataPoints.length]; 
		int[] column = new int[gridPoints.length*dataPoints.length];
		int[] row = new int[gridPoints.length]; 

		int n=0; 
		int rowkk=0;
		for (int i=0; i<gridPoints.length; i++) {
			//System.out.println("Calculating cost for: gridPoints: " + i);
			rowkk=0;
			for (int j=0; j<dataPoints.length; j++) {
				cost[n] = ClusterSnapGrid.distance(gridPoints[i], dataPoints[j]); //cost doesn't mean anything. Just for testing. 
				//System.out.println("Cost: n " + cost[n]);
				column[n]=j;
				rowkk++;
				n++; 
			}
		
			row[i] = rowkk;
		}
		
		System.out.println("Creating the cost matrix");

		//create thr sprae cost matrix
		SparseCostMatrix sparse = new SparseCostMatrix(cost, column, row, gridPoints.length); 
		
		System.out.println("SparseCostMatrix.nCols " + sparse.getNCols());
		System.out.println("SparseCostMatrix.nRows " + sparse.getNRows());
		System.out.println("SparseCostMatrix.examplePoint " + sparse.get(4, 2, -1));
		
		//double[][] fullCostMatrix  = sparse.toFullMatrix(); 
		//print2D(fullCostMatrix); 
		
		
		/**** First LAPJV algorithm to try ****/

		
		System.out.println("Start process: LAPJV");
		long time1= System.currentTimeMillis();
		
//		LAPJV lapjv = new LAPJV(sparse); 
//		lapjv.setListener(new SimpleLAPJVListener());
//		lapjv.process();
//		int[] result =  lapjv.getResult();
//		
//		System.out.println("LAPJV complete in " + ((time2-time1)/1000.) +  " seconds: The number of costs is: " + result.length);
//		
//		System.out.println("Example point: " + result[3]); 
		
		
		long time2= System.currentTimeMillis();


		
		/**** Second LAPJV algorithm to try ****/

		System.out.println("Start process: LAPJV2");

		time1= System.currentTimeMillis();
		
		LAPJV2 lapjv2 = new LAPJV2(); 
		lapjv2.setListener(new SimpleLAPJVListener());
		int[] result2 = lapjv2.execute(sparse.toFullMatrix()); 

		time2= System.currentTimeMillis();

		System.out.println("LAPJV complete in " + ((time2-time1)/1000.) +  " seconds: The number of costs is: " + result2.length);
	
		System.out.println("Example point: " + result2[3]); 



	}

}
