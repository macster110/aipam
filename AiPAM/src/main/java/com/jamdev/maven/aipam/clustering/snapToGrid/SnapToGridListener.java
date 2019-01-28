package com.jamdev.maven.aipam.clustering.snapToGrid;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * Listener for LAPJV algorithm  
 * @author Jamie Macaulay 
 *
 */
public class SnapToGridListener implements AssignmentListener {

	/**
	 * 
	 * LAPJV progress
	 */
	public SimpleDoubleProperty progressProperty = new SimpleDoubleProperty(); 

	/**
	 * Simple string property for messages
	 */
	public StringProperty messageProperty = new SimpleStringProperty(); 
	
//	/**
//	 * Cancelled property
//	 */
//	public BooleanProperty cancelledProperty = new SimpleBooleanProperty(); 
	
	/**
	 * Cancel flag so algorithm knows to stop. Note: the boolean property does not work
	 * for this, even if set to volatile. Primitive volatile boolean used instead. 
	 */
	private volatile boolean cancelled = false; 


	@Override
	public void columnReduction() {
		progressProperty.setValue(-1);
		messageProperty.setValue("Column reduction");

	}

	@Override
	public void reductionTransfer() {
		progressProperty.setValue(-1);
		messageProperty.setValue("Reduction Transfer");
	}

	@Override
	public void augmentingRowReduction(int f0, int k, int count) {
		progressProperty.setValue(k/(double) f0);
		messageProperty.setValue(String.format("k: %d of %d count: %d of 2", k, f0, count));
	}

	@Override
	public void augmenting() {
		// TODO Auto-generated method stub
		messageProperty.setValue("Augmenting");
	}


	/**
	 * The LAPJV algorithm progress property.
	 * @return the progress property 0 to 1. 
	 */
	public SimpleDoubleProperty assigmentProgressProperty() {
		return progressProperty;
	}
	

	/**
	 * The message property for updates. 
	 * @return
	 */
	public StringProperty assignmentMessageProperty() {
		return messageProperty;
	}
	

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Set a cancel flag to cancel the listener. 
	 * @param  cancelled - true to cancel the algorithm. 
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled=cancelled;
	}
	
	



}
