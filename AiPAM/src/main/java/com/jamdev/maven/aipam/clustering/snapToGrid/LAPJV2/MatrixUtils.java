package com.jamdev.maven.aipam.clustering.snapToGrid.LAPJV2;

import java.util.Arrays;

public class MatrixUtils
{
	public static double[][] makeSquare(double[][] matrix, double fillValue)
	{
		int rdim = matrix.length;
		int cdim = matrix[0].length;
		
		if (rdim==cdim)
		{
			return transpose(matrix);
		}
		else if (rdim>cdim)
		{
			matrix = transpose(matrix);
		    double[][] temp = new double[rdim-cdim][rdim];
		    for (double[] row: temp) Arrays.fill(row,fillValue);
		    matrix = append(matrix,temp);
		    return transpose(matrix);
		}
		else
		{
			double[][] temp = new double[cdim-rdim][cdim];
			for (double[] row: temp) Arrays.fill(row,fillValue);
		    return append(matrix,temp);
		}
	}
	
	public static double[][] transpose(double[][] matrix)
	{
		int m = matrix.length;
		int n = matrix[0].length;
		
		double[][] transposed = new double[n][m];
		
		for(int i = 0; i < m; i++) 
		{
		  for(int j = 0; j < n; j++) 
		  {
		    transposed[j][i] = matrix[i][j];
		  }
		}
		return transposed;
	}
	
	public static ValueLocationStruct min(double[][] matrix)
	{
		ValueLocationStruct vl = new ValueLocationStruct();
		vl.value=Double.MAX_VALUE;
		
		int m = matrix.length;
		int n = matrix[0].length;
		
		for(int i = 0; i < m; i++) 
		{
		  for(int j = 0; j < n; j++) 
		  {
		    if(matrix[i][j]<vl.value)
		    {
		    	vl.value = matrix[i][j];
		    	vl.row = i;
		    	vl.col = j;
		    }
		  }
		}
		return vl;
	}
	
	public ValueLocationStruct max(double[][] matrix)
	{
		ValueLocationStruct vl = new ValueLocationStruct();
		vl.value=Double.MIN_VALUE;
		
		int m = matrix.length;
		int n = matrix[0].length;
		
		for(int i = 0; i < m; i++) 
		{
		  for(int j = 0; j < n; j++) 
		  {
		    if(matrix[i][j]>vl.value)
		    {
		    	vl.value = matrix[i][j];
		    	vl.row = i;
		    	vl.col = j;
		    }
		  }
		}
		return vl;
	}
	
	public static double sum(double[][] matrix)
	{
		double sum=0;
		
		int m = matrix.length;
		int n = matrix[0].length;
		
		for(int i = 0; i < m; i++) 
		{
		  for(int j = 0; j < n; j++) 
		  {
		    sum+=matrix[i][j];
		  }
		}
		return sum;
	}
	
	public static double[][] append(double[][] a, double[][] b)
	{
        double[][] result = new double[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
	
}