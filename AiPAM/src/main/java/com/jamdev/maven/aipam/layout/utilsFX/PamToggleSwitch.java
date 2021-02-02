package com.jamdev.maven.aipam.layout.utilsFX;


import org.controlsfx.control.ToggleSwitch;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


/**
 * Toggle switch with a label to the right of the switch, not the default left.
 *  
 * @author Jamie Macaulay
 *
 */
public class PamToggleSwitch extends HBox{
	
	public static double MAX_TOGGLE_SWITCH_WIDTH = 20.0; 

	private ToggleSwitch toggleSwitch;

	private Label label;

	public PamToggleSwitch(String text) {
		toggleSwitch = new ToggleSwitch(); 
		toggleSwitch.setMaxWidth(20); //bug fix
		label = new Label(text);
		this.setSpacing(5);
			
		this.getChildren().addAll(toggleSwitch, label ); 
	}
	
	public BooleanProperty selectedProperty() {
		return toggleSwitch.selectedProperty(); 
	}
	
	public boolean isSelected() {
		return toggleSwitch.isSelected(); 
	}
	
	public void setSelected(boolean select) {
		 toggleSwitch.setSelected(select);
	}
	
	public ToggleSwitch getToggleSwitch() {
		return toggleSwitch;
	}

	public void setToggleSwitch(ToggleSwitch toggleSwitch) {
		this.toggleSwitch = toggleSwitch;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

}
