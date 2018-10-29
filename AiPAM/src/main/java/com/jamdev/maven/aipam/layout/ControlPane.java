package com.jamdev.maven.aipam.layout;

import java.util.ArrayList;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.utils.SettingsPane;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Pane with main controls for running algorithms and changing settings. 
 * <p>
 * <div>Cluster ccon by <a href="/icon/cluster-2" target="_blank">Vaadin Icons</a></div>
 * 
 * <div>Spectrogram Icon by <a href="/icon/audio-563" target="_blank">Useiconic</a></div>
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



	/**
	 * The master control pane. 
	 */
	private MasterControlPane masterControlPane;

	/**
	 * List of control panes for changing settings.
	 *  
	 */
	private ArrayList<SettingsPane<AIPamParams>> controlPanes;

	/**
	 * The annotation pane. 
	 */
	private AnnotationPane annotationPane;


	private GeneralSettingsPane generalSettingsPane;



	public ControlPane (AIPamView aiPamView) {
		this.aiPamView=aiPamView; 
		this.setCenter(createControPane());
		//this.setPadding(new Insets(5,5,5,5));
	}

	/**
	 * Create the control pane. 
	 */
	private Pane createControPane() {
		//the holder pane. 
		VBox holderPane = new VBox(); 
		//holderPane.setSpacing(5);


		//master control pane. 
		masterControlPane = new MasterControlPane(aiPamView); 

		//pane for importing audio clips. 
		audioImportPane = new AudioImportPane(aiPamView); 

		//pane for the fft settings. 
		fftPane = new FFTSettingsPane(aiPamView); 

		//pane for data playback
		playBackPane = new PlayBackPane(aiPamView); 

		//pane for clusterring algorithm. 
		clusterPane = new ClusterPane(aiPamView); 

		//the annotaiton pane. 
		annotationPane = new AnnotationPane(aiPamView);

		//the general settings pane. 
		generalSettingsPane = new GeneralSettingsPane(aiPamView); 

		controlPanes = new ArrayList<SettingsPane<AIPamParams>>(); 
		controlPanes.add(audioImportPane);

		controlPanes.add(fftPane);
		controlPanes.add(playBackPane);
		controlPanes.add(clusterPane);
		controlPanes.add(annotationPane);
		controlPanes.add(generalSettingsPane);


		holderPane.getChildren().add(masterControlPane.getPane());

		Button settingsButton;
		for (int i=0; i<controlPanes.size(); i++) {
			settingsButton = new Button(controlPanes.get(i).getTitle());
			if (controlPanes.get(i).getIcon()!=null) {
				settingsButton.setGraphic(controlPanes.get(i).getIcon());
			}
			settingsButton.prefWidthProperty().bind(holderPane.widthProperty());
			settingsButton.getStyleClass().add("fluent-menu-button");

			final SettingsPane<AIPamParams> settingsPane = controlPanes.get(i);
			settingsButton.setOnAction((action)->{
				aiPamView.showSettingsPane(settingsPane); 
			});
			//settingsButton.setContentDisplay(ContentDisplay.LEFT);
			settingsButton.setContentDisplay(ContentDisplay.LEFT);
			settingsButton.setAlignment(Pos.BASELINE_LEFT);
			settingsButton.setGraphicTextGap(15);

			//settingsButton.setTextAlignment(TextAlignment.LEFT);

			holderPane.getChildren().add(settingsButton);

		}

		holderPane.getStyleClass().add("fluent-pane");
		holderPane.setPrefWidth(200);

		return holderPane; 
	}




	/**
	 * Set params for all the settings panes
	 * @param params - the parameters to set. 
	 */
	public void setParams(AIPamParams params) {
		for (int i=0; i<controlPanes.size(); i++) {
			controlPanes.get(i).setParams(params);
		}

	}

	/**
	 * Get parameters for all the settings panes. Each pane should sequentially alters the parameters class,
	 * changing all relevant parameters. 
	 * @param params - the parameters to change
	 */
	public AIPamParams getParams(AIPamParams params) {
		for (int i=0; i<controlPanes.size(); i++) {
			controlPanes.get(i).getPane(); //need to make sure it's intialised...bit messy
			controlPanes.get(i).getParams(params); 
		}
		return params; 
	}
	
	/**
	 * Get the audio import pane.This handles importing audio files
	 * 
	 * @return the audio import pane.
	 */
	public AudioImportPane getAudioImportPane() {
		return audioImportPane;
	}

}
