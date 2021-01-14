package com.jamdev.maven.aipam.layout;

import org.controlsfx.control.ToggleSwitch;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


/**
 * Create a date and time parser pane which allows the user to change date time settings. 
 * @author Jamie Macaulay 
 *
 */
public class DateTimeParserPane extends BorderPane {

	/**
	 * The toggle switch for selecting autoformat. 
	 */
	private ToggleSwitch autoFormatSwitch;

	public DateTimeParserPane() {
		this.setCenter(createPane());
	}
	
	/**
	 * Create the datetime pane. 
	 * @return the date time pane. 
	 */
	private Pane createPane() {
		
		autoFormatSwitch = new ToggleSwitch(); 
		
		HBox hBox = new HBox(); 
		
		return new VBox(new Label("DateTimeParserPane  - TODO")); 
	}
}
