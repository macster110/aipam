package com.jamdev.maven.aipam.clips;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Standard listener for audio import which provides bindings 
 * for JavaFX Task. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class StandardAudioImportListener  implements AudioImporterListener {
	
	/**
	 * Progress property 
	 */
	private DoubleProperty progress = new SimpleDoubleProperty(0); 
	
	/**
	 * Progress property 
	 */
	private IntegerProperty fileN = new SimpleIntegerProperty(0); 
	
	/**
	 * Progress property 
	 */
	private IntegerProperty nFiles = new SimpleIntegerProperty(0); 
	
	/**
	 * A description of the current process. 
	 */
	private String description; 
	
	/**
	 * Get the current description. 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * The progress property. 
	 * @return the progress property. 
	 */
	public DoubleProperty progressProperty() {
		return progress;
	}

	private volatile boolean cancelled = false; 

	@Override
	public void updateProgress(double progress, int fileN, int nFiles) {
		this.progress.set(progress); 
		this.fileN.set(fileN); 
		this.nFiles.set(nFiles); 

	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Get the total number of files for audio import
	 * @return
	 */
	public int getNFiles() {
		return nFiles.get();
	}
	
	/**
	 * Get the index of the current file in the list
	 * @return the position of the current file in the list. 
	 */
	public int getFileN() {
		return fileN.get();
	}



}
