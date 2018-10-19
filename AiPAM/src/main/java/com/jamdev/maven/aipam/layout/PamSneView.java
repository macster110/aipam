package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AiPamController;

import javafx.scene.layout.BorderPane;
import jfxtras.styles.jmetro8.JMetro;

/**
 * The main view for PamSne
 * @author Jamie Macaulay
 *
 */
public class PamSneView extends BorderPane {
	
	/**
	 * The control pane. 
	 */
	private ControlPane controlPane; 
	
	/**
	 * Holds the clips. 
	 */
	private ClipPane clipPane; 


	
	public PamSneView(AiPamController pamSneControl) {
		
		//apply the JMetro them 
		new JMetro(JMetro.Style.DARK).applyTheme(this);
		
		controlPane= new ControlPane(this); 
		
		clipPane= new ClipPane(this); 
		
		this.setLeft(controlPane);
		this.setCenter(clipPane);
	}

}
