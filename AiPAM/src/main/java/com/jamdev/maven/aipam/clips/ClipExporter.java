package com.jamdev.maven.aipam.clips;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.jamdev.maven.aipam.AIPamParams;

import javafx.concurrent.Task;

/**
 * Handles exporting clips. 
 * @author Jamie Macaulay 
 *
 */
public class ClipExporter {

	
	private static void copyFileUsingChannel(File source, File dest) throws IOException {
//		FileChannel sourceChannel = null;
//		FileChannel destChannel = null;
		OutputStream os = new FileOutputStream(dest);
		Files.copy(Paths.get(source.toURI()), os);
		os.close();
	}
	
	/**
	 * Creates a task for importing the clips from a folder. 
	 * @param load - true to load clips. False checks the clips. 
	 * @return the task importing clips. 
	 */
	public Task<Integer> exportClipTask(ArrayList<String> clipFiles, ArrayList<String> exportFiles, AIPamParams params) {
		
		Task<Integer> task = new Task<Integer>() {
			@Override protected Integer call() throws Exception {
				//progress is in intermediate mode. 
				try {
					
					File file1;
					File file2;
					for (int i=0; i<clipFiles.size(); i++) {
						
						if (this.isCancelled()) return -1; 
						
						
						
						file1= new File(clipFiles.get(i)); 
						if (!file1.exists()) {
							updateMessage("Could not find clip!!"); 
							continue; 
						}
						
						//file to create
						file2= new File(exportFiles.get(i)); 
						if (!file2.getParentFile().exists()) {
							//make directory
							file2.getParentFile().mkdirs(); 
						} 
						
						updateMessage("Copying " + file1.getName()); 
						updateProgress(i, exportFiles.size());


						copyFileUsingChannel(file1, file2); 
					}
				
				}
				catch (Exception e) {
					e.printStackTrace();
					return -1; 
				}
				
				return 1; 
			}
		
		};
		return task; 
	}
	
	
	
	
	
	

}
