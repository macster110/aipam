package com.jamdev.maven.aipam.clips.datetime;

import java.io.File;

/**
 * Interface for working out file dates from files. 
 * 
 * @author Jamie Macaulay
 *
 */
public interface DateTimeParser {
	
	/**
	 * Get the time from a file. This could be from the filename or metadata. 
	 * @param file - the file. 
	 * @return the time millis datenumber. 
	 */
	public long getTimeFromFile(File file); 

}
