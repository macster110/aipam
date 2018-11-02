package com.jamdev.maven.aipam.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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


}
