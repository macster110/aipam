package com.jamdev.maven.aipam.layout;

import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * A standard menu item.
 * @author Jamie Macaulay
 *
 */
public class StandardPaneMenuItem implements ControlPaneMenuItem {
	
	
	/**
	 * The button . 
	 */
	private Button button;
	
	/**
	 * The text to show in the button
	 */
	private String text;

	StandardPaneMenuItem(Button button, String text){
		this.button = button; 
		this.text = text; 
	}

	@Override
	public Node getMenuItem() {
		return button;
	}

	@Override
	public void showLabel(boolean show) {
		if (show) {
			button.setText(text);
		}
		else {
			button.setText("");
		}
		
	}

}
