package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AiPamParams;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Pane with main controls. 
 * 
 * @author Jamie Macaulay
 *
 */
public class ControlPane extends BorderPane {
	
	/**
	 * Reference to the view. 
	 */
	private AIPamView aiPamView;
	
	/**
	 * Text field for the file. 
	 */
	private TextField textField;
	
	/**
	 * The import button 
	 */
	private Button importButton;
	


	public ControlPane (AIPamView pamSneView) {
		this.aiPamView=pamSneView; 
		this.setCenter(createControPane());
		this.setPadding(new Insets(5,5,5,5));
	}
	
	/**
	 * Create the control pane. 
	 */
	private Pane createControPane() {
		//the holder pane. 
		VBox holderPane = new VBox(); 
		holderPane.setSpacing(5);
		
		//create
		HBox fileImportHolder = new HBox();
		fileImportHolder.setSpacing(5);
		
		importButton = new Button("Import"); 
//		Text iconText = new MaterialDesignIconView(MaterialDesignIcon.FOLDER_DOWNLOAD);
//		iconText.setStyle(String.format("-fx-font-size: %dpt", AIPamView.iconSize));
//		importButton.setGraphic(iconText);
		importButton.setOnAction((action)->{
			
		});
		
		textField = new TextField(); 
		textField.setEditable(false);
		textField.prefHeightProperty().bind(importButton.heightProperty());
		textField.setText("Open an audio folder");
				
		fileImportHolder.getChildren().addAll(textField, importButton);
	
		//file vbox. 
		holderPane.getChildren().addAll(new Label("Import Wav Data"), fileImportHolder); 
		
		return holderPane; 
	}
	
	
	/**
	 * Set the params in the 
	 * @param params
	 */
	public void setParams(AiPamParams params) {
		
	}
	
	
	public AiPamParams getParams(AiPamParams params) {
		return params; 
		
	}

}
