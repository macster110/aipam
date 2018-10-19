package com.jamdev.maven.aipam.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Some basic utility classes. . 
 * @author Jamie Macaulay 
 *
 */
public class PamSneUtils {

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
		for (File file : fList) {
			if (file.isFile() &&  getExtension(directoryName).equals(type)) {
				System.out.println(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				resultList.addAll(listFiles(file.getAbsolutePath(),type));
			}
		}
		//System.out.println(fList);
		return resultList;
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

}
