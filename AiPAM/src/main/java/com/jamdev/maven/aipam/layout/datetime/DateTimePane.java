package com.jamdev.maven.aipam.layout.datetime;

import org.controlsfx.control.textfield.CustomTextField;

import com.jamdev.maven.aipam.clips.datetime.StandardDateTimeParser;
import com.jamdev.maven.aipam.layout.utilsFX.PamToggleSwitch;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Pane for setting up the date- time parser. 
 * @author Jamie
 *
 */
public class DateTimePane extends BorderPane {
	
	/**
	 * The auto switch. 
	 */
	private PamToggleSwitch autoSwitch;
	
	/**
	 * Create the custom text field. 
	 */
	private CustomTextField textField;

	/**
	 * Preview text field for checkign if date times are correct. 
	 */
	private TextField previewDateField;

	/**
	 * The field preview box. 
	 */
	private ComboBox<String> filePreviewSelBox;

	/**
	 * constructor for the data time pane 
	 * @param dateTimeParser - the date time parser. 
	 */
	public DateTimePane(StandardDateTimeParser dateTimeParser) {
		
		this.setCenter(createPane()); 

	}
	
	private Pane createPane() {
		
		VBox vBox = new VBox(); 
		vBox .setSpacing(5);
		
		
		autoSwitch  = new PamToggleSwitch("Auto date-time"); 
		autoSwitch.selectedProperty().addListener((obsVal, oldVal, newVal)->{
			
		});
		
		textField  = new CustomTextField(); 

		HBox testDateHolder = new HBox(); 
		
		
		previewDateField  = new TextField(); 
		
		filePreviewSelBox  = new ComboBox<String>(); 
		
		vBox.getChildren().addAll(autoSwitch, textField, testDateHolder); 

		
		return vBox; 
	}

}
