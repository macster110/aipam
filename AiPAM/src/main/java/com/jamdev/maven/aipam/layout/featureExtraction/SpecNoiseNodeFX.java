package com.jamdev.maven.aipam.layout.featureExtraction;

import javafx.scene.Node;

/**
 * JavaFX control to change the settings of the noise reduction process. 
 * @author Jamie Macaulay
 *
 */
public interface SpecNoiseNodeFX {

	boolean getParams();

	void setParams();

	void setSelected(boolean selected);

	Node getPane();

}
