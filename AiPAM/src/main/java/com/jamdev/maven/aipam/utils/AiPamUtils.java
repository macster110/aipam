package com.jamdev.maven.aipam.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.paint.Color;


/**
 * Some basic utility classes. . 
 * @author Jamie Macaulay 
 *
 */
public class AiPamUtils {

	/**
	 * Get a list of files within a directory and sub directories.
	 * @param directoryName - the name of the directory. 
	 * @param type - the file extension (null for all file types)
	 * @return list of files with extension 
	 */
	public static List<File> listFiles(String directoryName, String type) {
		File directory = new File(directoryName);

		List<File> resultList = new ArrayList<File>();

		// get all the files from a directory
		File[] fList = directory.listFiles();
		resultList.addAll(Arrays.asList(fList));

		List<File> wavList = new ArrayList<File>(); 
		for (File file : fList) {
			//System.out.println("Wav: " + getExtension(file.getName()));
			if (file.isFile() &&  getExtension(file.getName()).equals(type)) {
				wavList.add(file); 
				//System.out.println(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				wavList.addAll(listFiles(file.getAbsolutePath(),type));
			}
		}


		//System.out.println(fList);
		return wavList;
	} 


	/**
	 * Get the file extension for a path. 
	 * @param fileName - the filename
	 * @return the file extension e.g. wave file is "wav"; 
	 */
	public static String getExtension(String fileName) {
		char ch;
		int len;
		if(fileName==null || 
				(len = fileName.length())==0 || 
				(ch = fileName.charAt(len-1))=='/' || ch=='\\' || //in the case of a directory
				ch=='.' ) //in the case of . or ..
			return "";
		int dotInd = fileName.lastIndexOf('.'),
				sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if( dotInd<=sepInd )
			return "";
		else
			return fileName.substring(dotInd+1).toLowerCase();
	}

	/** 
	 * Convert an arbitrary-dimensional rectangular double array to flat vector.<br>
	 * Can pass double[], double[][], double[][][], etc.
	 */
	public static double[] flattenDoubleArray(Object doubleArray) {
		if (doubleArray instanceof double[])
			return (double[]) doubleArray;

		LinkedList<Object> stack = new LinkedList<>();
		stack.push(doubleArray);

		int[] shape = arrayShape(doubleArray);
		int length = prod(shape);
		double[] flat = new double[length];
		int count = 0;

		while (!stack.isEmpty()) {
			Object current = stack.pop();
			if (current instanceof double[]) {
				double[] arr = (double[]) current;
				for (int i = 0; i < arr.length; i++)
					flat[count++] = arr[i];
			} else if (current instanceof Object[]) {
				Object[] o = (Object[]) current;
				for (int i = o.length - 1; i >= 0; i--)
					stack.push(o[i]);
			} else
				throw new IllegalArgumentException("Base array is not double[]");
		}

		if (count != flat.length)
			throw new IllegalArgumentException("Fewer elements than expected. Array is ragged?");
		return flat;
	}


	/** Calculate the shape of an arbitrary multi-dimensional array. Assumes:<br>
	 * (a) array is rectangular (not ragged) and first elements (i.e., array[0][0][0]...) are non-null <br>
	 * (b) First elements have > 0 length. So array[0].length > 0, array[0][0].length > 0, etc.<br>
	 * Can pass any Java array opType: double[], Object[][][], float[][], etc.<br>
	 * Length of returned array is number of dimensions; returned[i] is size of ith dimension.
	 */
	public static int[] arrayShape(Object array) {
		int nDimensions = 0;
		Class<?> c = array.getClass().getComponentType();
		while (c != null) {
			nDimensions++;
			c = c.getComponentType();
		}


		int[] shape = new int[nDimensions];
		Object current = array;
		for (int i = 0; i < shape.length - 1; i++) {
			shape[i] = ((Object[]) current).length;
			current = ((Object[]) current)[0];
		}

		if (current instanceof Object[]) {
			shape[shape.length - 1] = ((Object[]) current).length;
		} else if (current instanceof double[]) {
			shape[shape.length - 1] = ((double[]) current).length;
		} else if (current instanceof float[]) {
			shape[shape.length - 1] = ((float[]) current).length;
		} else if (current instanceof long[]) {
			shape[shape.length - 1] = ((long[]) current).length;
		} else if (current instanceof int[]) {
			shape[shape.length - 1] = ((int[]) current).length;
		} else if (current instanceof byte[]) {
			shape[shape.length - 1] = ((byte[]) current).length;
		} else if (current instanceof char[]) {
			shape[shape.length - 1] = ((char[]) current).length;
		} else if (current instanceof boolean[]) {
			shape[shape.length - 1] = ((boolean[]) current).length;
		} else if (current instanceof short[]) {
			shape[shape.length - 1] = ((short[]) current).length;
		} else
			throw new IllegalStateException("Unknown array opType"); //Should never happen
		return shape;
	}

	/**
	 * Product of an int array
	 * @param mult the elements
	 *            to calculate the sum for
	 * @return the product of this array
	 */
	public static int prod(List<Integer> mult) {
		if (mult.size() < 1)
			return 0;
		int ret = 1;
		for (int i = 0; i < mult.size(); i++)
			ret *= mult.get(i);
		return ret;
	}

	/**
	 * Product of an int array
	 * @param mult the elements
	 *            to calculate the sum for
	 * @return the product of this array
	 */
	public static long prodLong(List<? extends Number> mult) {
		if (mult.size() < 1)
			return 0;
		long ret = 1;
		for (int i = 0; i < mult.size(); i++)
			ret *= mult.get(i).longValue();
		return ret;
	}
	
	/**
     * Product of an int array
     * @param mult the elements
     *            to calculate the sum for
     * @return the product of this array
     */
    public static int prod(int... mult) {
        if (mult.length < 1)
            return 0;
        int ret = 1;
        for (int i = 0; i < mult.length; i++)
            ret *= mult[i];
        return ret;
}



	/**
	 * Convert a colour to a hex value. 
	 * @param color - the colour to convert
	 * @return hex string of the colour. 
	 */
	public static String color2Hex(Color color){
		int r =  (int) (color.getRed() * 255);
		int g =  (int) (color.getGreen() * 255);
		int b =  (int) (color.getBlue() * 255);
		String str = String.format("#%02X%02X%02X;", r, g, b);
		return str; 
	}

	/**
	 * Finds the minimum value of a matrix
	 * @param cc - the array to find minimum value in
	 * @return the minimum value in the array 
	 */
	public static double min(double[] numbers) {
		double minValue = numbers[0];
		for(int i=1;i<numbers.length;i++){
			if(numbers[i] < minValue){
				minValue = numbers[i];
			}
		}
		return minValue;
	}

	/**
	 * Finds the maximum value of an array
	 * @param cc - the array to find maximum value in
	 * @return the maximum value in the array 
	 */
	public static double max(double[] numbers) {
		double maxValue = numbers[0];
		for(int i=1;i<numbers.length;i++){
			if(numbers[i] > maxValue){
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}
	
	

	/**
	 * Finds the minimum value of a matrix
	 * @param numbers - the array to find the minimum in
	 * @return the minimu  number in the array.
	 */
	public static double min(double[][] numbers) {
		double minValue  = numbers[0][0];
		for(int i=0;i<numbers.length;i++){
			for(int j=1;j<numbers[i].length;j++){
				if(numbers[i][j] < minValue){
					minValue = numbers[i][j];
				}
			}
		}
		return minValue;
	}

	/**
	 * Finds the maximum value of a matrix
	 * @param cc - the array to find maximum value in
	 * @return the maximum value in the array 
	 */
	public static double max(double[][] numbers) {
		double maxValue = numbers[0][0];
		for(int i=0;i<numbers.length;i++){
			for(int j=1;j<numbers[i].length;j++){
				if(numbers[i][j] > maxValue){
					maxValue = numbers[i][j];
				}
			}
		}
		return maxValue;
	}

	/**
	 * Normalise an array of points. Rewrite the input array
	 * @param normalise constant to normalise to. 
	 * @return the normalised array. 
	 */
	public static void normalise(double[] normalise, double normaliseTo) {
		double max = max(normalise); 
		for (int i=0; i<normalise.length; i++) {
			normalise[i]=normaliseTo*(normalise[i]/max); 
		}
	}


	/**
	 * Normalise a 2D matrix of points. Rewrite the input array
	 * @param normalise constant to normalise to. 
	 * @return the normalised array. 
	 */
	public static void normalise(double[][] normalise, double normaliseTo) {
		double max = max(normalise); 
		for (int i=0; i<normalise.length; i++) {
			for(int j=1;j<normalise[i].length;j++){
				normalise[i][j]=normaliseTo*(normalise[i][j]/max); 
			}
		}
	}
	
	/**
	 * Print a 2D double array
	 * @param array - the array 
	 */
	public static void printArray(double[][] array) {
		for (int j=0; j<array.length; j++) {
			System.out.println("");
			for (int i=0; i<array[j].length; i++) {
				System.out.print(array[j][i] + " : ");
			}
		}
		System.out.println("");
	}
	
	/**
	 * Print a 2D int array
	 * @param array - the array 
	 */
	public static void printArray(int[][] array) {
		for (int j=0; j<array.length; j++) {
			System.out.println("");
			for (int i=0; i<array[j].length; i++) {
				System.out.print(array[j][i] + " : ");
			}
		}
		System.out.println("");
	}




}
