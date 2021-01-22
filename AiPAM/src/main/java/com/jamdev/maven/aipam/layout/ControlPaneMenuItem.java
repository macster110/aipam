package com.jamdev.maven.aipam.layout;

import javafx.scene.Node;

/**
 * A menu item for the control pane. Every menu item or label must implement this
 * @author Jamie Macaulay
 *
 */
public interface ControlPaneMenuItem {

	public Node getMenuItem();
	
	public void showLabel(boolean show); 
}
