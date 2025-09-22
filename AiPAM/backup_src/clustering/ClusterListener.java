package com.jamdev.maven.aipam.clustering;

import javafx.beans.property.DoubleProperty;

/**
 * Listener afor cluster algorithm progress. 
 * 
 * @author Jamie Macaulay 
 *
 */
public interface ClusterListener {
	
	public DoubleProperty progressProperty(); 

}
