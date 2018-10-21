package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AiPamController;

import javafx.scene.layout.BorderPane;

/**
 * The main view for PamSne
 * @author Jamie Macaulay
 *
 */
public class AIPamView extends BorderPane {
	
	/**
	 * The standard icon size. 
	 */
	public static int iconSize = 20;

	/**
	 * The control pane. 
	 */
	private ControlPane controlPane; 
	
	/**
	 * Holds the clips. 
	 */
	private ClipPane clipPane; 

	
	public AIPamView(AiPamController pamSneControl) {

				
		controlPane= new ControlPane(this); 
		
		clipPane= new ClipPane(this); 
		
		this.setLeft(controlPane);
		this.setCenter(clipPane);
	}

}
