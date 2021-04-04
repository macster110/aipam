package com.jamdev.maven.aipam.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.rank.Median;

import javafx.scene.paint.Color;


/**
 * 
 * Some basic utility classes.
 * 
 * @author Jamie Macaulay 
 *
 */
public class AiPamUtils {

	/**
	 * The maximum number of allowed channels 
	 */
	private static final int MAX_CHANNELS = 32;

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
	 * Finds the maximum value of an array
	 * @param cc - the array to find maximum value in
	 * @return the maximum value in the array 
	 */
	public static int max(int[] numbers) {
		int maxValue = numbers[0];
		for(int i=1;i<numbers.length;i++){
			if(numbers[i] > maxValue){
				maxValue = numbers[i];
			}
		}
		return maxValue;
	}


	/**
	 * Creates an array of numbers between two values. A bit like the MATLAB syntax x = 1:4:30; 
	 * @param from - the start of the array.
	 * @param jump - the jump between each value in the array. 
	 * @param to - the end of the array. 
	 */
	public static double[] makeArr(double from, double jump, double to) {
		//create the array size. 
		double[] arr = new double[(int) Math.floor((to-from)/(double) jump)]; 

		double n=from; 
		for(int i=0;i<arr.length;i++){
			arr[i]=n; 
			n=n+jump; 
		}
		return arr;
	}

	/**
	 * Creates an array of numbers between two values. A bit like the MATLAB syntax x = 1:4:30; 
	 * @param from - the start of the array.
	 * @param jump - the jump between each value in the array. 
	 * @param to - the end of the array. 
	 */
	public static int[] makeArr(int from, int jump, int to) {
		//create the array size. 
		int[] arr = new int[(int) Math.floor((to-from)/(double) jump)]; 

		int n=from; 
		for(int i=0;i<arr.length;i++){
			arr[i]=n; 
			n=n+jump; 
		}
		return arr;
	}

	//	/**
	//	 * Create an array with ascending values between start and end with a specified jump.
	//	 * @param start - the start value.
	//	 * @param end  - the end value.
	//	 * @param jump - the jump between values. 
	//	 * @return the array. 
	//	 */
	//	public static int[] makeArray(int start, int end, int jump) {
	//		int[] newArray = new int[(int) Math.ceil((end-start)/jump)]; 
	//		int n=0; 
	//		for(int i=start;i<=end;i=i+jump){
	//			newArray[n] = i; 
	//			n++;
	//		}
	//		return newArray;
	//	}

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

	/**
	 * Calculate the median value of a double[] array. 
	 * @param duration - the array 
	 * @return the median value of the array
	 */
	public static double median(double[] duration) {
		Median median = new Median();
		double medianValue = median.evaluate(duration);
		return medianValue;
	}

	/**
	 * Calculate the median value of an int[] array. 
	 * @param duration - the array 
	 * @return the median value of the array
	 */
	public static double median(int[] duration) {
		double[] arr = new double[duration.length];
		int n=0; 
		for (int d: duration) {
			arr[n]= d; 
			n++;
		}
		return  median(arr) ;
	}


	/**
	 * Turn a bitmap into an array of channel numbers. 
	 * @param channelMap channel map
	 * @return channel list array
	 */
	public static int[] getChannelArray(int channelMap) {
		int nChan = getNumChannels(channelMap);
		//		if (nChan <= 0) { // better to return an empty array to avoid null pointer exceptions. 
		//			return null;
		//		}
		int[] channels = new int[nChan];
		for (int i = 0; i < nChan; i++) {
			channels[i] = getNthChannel(i, channelMap);
		}
		return channels;
	}


	/**
	 * Get the number of channels present in this bitmap. 
	 * (calls Integer.bitCount(...))
	 * @param channelBitmap channel map
	 * @return number of bits set.
	 */
	public static int getNumChannels(int channelBitmap) {
		return Integer.bitCount(channelBitmap);
	}

	/**
	 * Works out the index of a particular channel in a channel list - often,
	 * if channelBitmap is a set of continuous channels starting with 0, then
	 * the channel pos is the same as the single channel number. However, if there
	 * are gaps in the channelBitmap, then the channel pos will be < than the 
	 * channel Number.
	 * @param singleChannel
	 * @param channelBitmap
	 * @return the channel position in the channel list or -1 if it isn't available
	 */
	public static int getChannelPos(int singleChannel, int channelBitmap) {

		int channelPos = 0;

		if ((1<<singleChannel & channelBitmap) == 0) return -1;

		for (int i = 0; i < singleChannel; i++) {
			if ((1<<i & channelBitmap) != 0) {
				channelPos++;
			}
		}

		return channelPos;
	}

	/**
	 * 		
	 * Get's the number of the nth used channel in a bitmap. e.g. if the 
	 * channelBitmap were 0xc (1100), then the 0th channel would be 2 and the 
	 * 1st channel would be 3.
	 * @param singleChannel nth channel in a list 
	 * @param channelBitmap bitmap of all used channels. 
	 * @return true channel number or -1 if the channel is not in the map. 
	 */
	public static int getNthChannel(int singleChannel, int channelBitmap) {
		/*
		 * get's the number of the nth used channel in a bitmap. e.g. if the 
		 * channelBitmap were 0xc (1100), then the 0th channel would be 2 and the 
		 * 1st channel would be 3.
		 */
		int countedChannels = 0;
		for (int i = 0; i < MAX_CHANNELS; i++) {
			if ((channelBitmap & (1<<i)) != 0) {
				if (++countedChannels > singleChannel) return i;
			}
		}
		return -1;
	}

	/**
	 * Convert a short array to a double array assuming 16 bit scaling i..e the maximum short is 1.0 when converted to double.
	 * @param arr - the short array
	 */
	public static double[] short2double(short[] arr) {

		double[] arrd = new double[arr.length]; 

		double scaleFactor= Math.pow(2, 16);  

		for (int i=0; i<arr.length; i++) {
			arrd[i]=arr[i]/scaleFactor; 
		}

		return arrd; 

	}


	/**
	 * Convert a short array to a double array assuming 16 bit scaling i..e the maximum short is 1.0 when converted to double.
	 * @param arr - the short array
	 */
	public static double[][] short2double(short[][] arr) {

		double scaleFactor= Math.pow(2, 16);  


		double[][] arrdd = new double[arr.length][];  

		double[] arrd;
		for (int j=0; j<arr.length; j++) {

			arrd = new double[arr[j].length]; 

			for (int i=0; i<arr[j].length; i++) {
				arrd[i]=arr[j][i]/scaleFactor; 
			}

			arrdd[j] = arrd; 

		}

		return arrdd; 

	}








}
