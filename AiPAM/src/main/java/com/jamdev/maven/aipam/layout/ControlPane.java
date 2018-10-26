package com.jamdev.maven.aipam.layout;

import java.io.File;

import com.jamdev.maven.aipam.AIPamParams;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;

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
	 * The FFT Settings Pane. 
	 */
	private FFTSettingsPane fftPane;


	private PlayBackPane playBackPane;

	/**
	 * Pane with clustering algorithm controls 
	 */
	private ClusterPane clusterPane;

	/**
	 * The audio import pane. 
	 */
	private AudioImportPane audioImportPane;
	


	public ControlPane (AIPamView aiPamView) {
		this.aiPamView=aiPamView; 
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
		
		//pane for importing audio clips. 
		audioImportPane = new AudioImportPane(aiPamView); 
		
		//pane for the fft settings. 
		fftPane = new FFTSettingsPane(); 
		
		//pane for data playback
		playBackPane = new PlayBackPane(aiPamView); 
		
		//pane for clusterring algorithm. 
		clusterPane = new ClusterPane(aiPamView); 

		//file vbox. 
		holderPane.getChildren().addAll(audioImportPane.getPane(), 
				fftPane.getPane(), playBackPane, clusterPane.getPane()); 
		
		
		return holderPane; 
	}
	



	/**
	 * Set the params in the 
	 * @param params
	 */
	public void setParams(AIPamParams params) {
		
	}
	
	
	public AIPamParams getParams(AIPamParams params) {
		return params; 
		
	}

}
