package com.jamdev.maven.aipam.layout.utilsFX;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Interface for settings panes. 
 * @author Jamie Macaulay
 *
 * @param <T> - the parameter class to alter.
 */
public interface SettingsPane<T> {
	
	/**
	 * Get the pane with controls 
	 * @return
	 */
	public Pane getPane(); 
	
	/**
	 * Get the parameters from the control states.
	 * @return the parameters input- relevant fields of this will be altered to reflect control settings. 
	 */
	public T getParams(T paramsIn); 
	
	/**
	 * Set the controls in the pane to parameter settings
	 */
	public void setParams(T params); 
	
	/**
	 * Get the icon
	 * @return - the icon
	 */
	public Node getIcon();
	
	/**
	 * Get the title of the pane. 
	 * @return the title of the pane. 
	 */
	public String getTitle();
	
	/**
	 * Notify an update from the controller. 
	 * @param flag - the update type
	 * @param stuff - a potential object, can be null.
	 */
	public void notifyUpdate(int flag, Object stuff);

	/**
	 * Get a description of the pane. 
	 * @return a description of the pane. 
	 */
	public String getDescription();

}
