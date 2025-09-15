package com.jamdev.maven.aipam.layout;

import java.util.ArrayList;
import java.util.HashMap;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.layout.UserPrompts.UserPrompt;
import com.jamdev.maven.aipam.layout.clustering.ClusterPane;
import com.jamdev.maven.aipam.layout.deeplearning.DeepLearningPane;
import com.jamdev.maven.aipam.layout.featureExtraction.FeaturePane;
import com.jamdev.maven.aipam.layout.utilsFX.FluentMenuPane;
import com.jamdev.maven.aipam.layout.utilsFX.SettingsPane;

import org.kordamp.ikonli.javafx.FontIcon;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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

	public Integer AUDIO_GROUP = 1; 

	public Integer CLASSIFIER_GROUP = 2; 

	/**
	 * Reference to the view. 
	 */
	private AIPamView aiPamView;

	/**
	 * The FFT Settings Pane. 
	 */
	private FFTSettingsPane fftPane;

	/**
	 * The playback pane. 
	 */
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
	 * List of control panes for changing settings. Control panes are a 
	 * subset of the menu items
	 *  
	 */
	private ArrayList<SettingsPane<AIPamParams>> controlPanes;


	/**
	 * List of all menu nodes. 
	 */
	private ArrayList<ControlPaneMenuItem> menuItems = new ArrayList<ControlPaneMenuItem>(); 


	/**
	 * The annotation pane. 
	 */
	private AnnotationPane annotationPane;

	/**
	 * Pane for general program settings. 
	 */
	private GeneralSettingsPane generalSettingsPane;


	/**
	 * Menu pane which holds the settings buttons. 
	 */
	private FluentMenuPane menuPane;

	/**
	 * Import settings button
	 */
	private Button importSettings;

	/**
	 * Save settings button
	 */
	private Button saveSettings;

	/**
	 * Pane for feature extraction
	 */
	private FeaturePane featurePane;

	private DeepLearningPane deepLearningPane;

	private Pane menuHolderPane;


	public ControlPane (AIPamView aiPamView) {
		this.aiPamView=aiPamView; 
		this.setCenter(menuHolderPane = createControPane());
		//this.setPadding(new Insets(5,5,5,5));
		setMenuPaneCollapsed(aiPamView.getAIParams().collapseMenu); 
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
		menuItems.addAll(masterControlPane.getMenuItems());

//		//Create labels for the panes	
//		Label labelSettings = new Label("General Settings");
//		labelSettings.setPadding(new Insets(5,5,5,5));
//		labelSettings.getStyleClass().add("label-title1");
//		AIPamView.setButtonIcon(labelSettings, FontAwesomeIcon.GEAR); 
//
//		Label labelAudio = new Label("Audio");
//		labelAudio.setPadding(new Insets(5,5,5,5));
//		labelAudio.getStyleClass().add("label-title1");
//		AIPamView.setButtonIcon(labelAudio, FontAwesomeIcon.GEAR); 
//
//		Label classificationLabel  = new Label("Classification");
//		classificationLabel.setPadding(new Insets(5,5,5,5));
//		classificationLabel.getStyleClass().add("label-title1");
//		AIPamView.setButtonIcon(classificationLabel, FontAwesomeIcon.GEAR); 


		//pane for importing audio clips. 
		audioImportPane = new AudioImportPane(aiPamView); 
		audioImportPane.setParams(aiPamView.getAIParams());
		audioImportPane.addSettingsListener(()->{
			audioImportPane.getParams(aiPamView.getAIParams());
			aiPamView.checkSettings(); 
		});

		//pane for the FFT settings. 
		fftPane = new FFTSettingsPane(aiPamView); 
		fftPane.setParams(aiPamView.getAIParams());
		fftPane.addSettingsListener(()->{
			fftPane.getParams(aiPamView.getAIParams());
			aiPamView.checkSettings(); 
		});

		//pane for feature extraction
		featurePane = new FeaturePane(aiPamView); 
		featurePane.setParams(aiPamView.getAIParams());
		featurePane.addSettingsListener(()->{
			featurePane.getParams(aiPamView.getAIParams()); 
			aiPamView.checkSettings(); 
		});

		//pane for data play back
		playBackPane = new PlayBackPane(aiPamView); 
		playBackPane.setParams(aiPamView.getAIParams());
		playBackPane.addSettingsListener(()->{
			playBackPane.getParams(aiPamView.getAIParams()); 
			aiPamView.checkSettings(); 
		});

		//pane for clustering algorithm. 
		clusterPane = new ClusterPane(aiPamView); 
		clusterPane.setParams(aiPamView.getAIParams());
		clusterPane.addSettingsListener(()->{
			clusterPane.getParams(aiPamView.getAIParams()); 
			aiPamView.checkSettings(); 
		});


		deepLearningPane = new DeepLearningPane(aiPamView); 
		deepLearningPane.setParams(aiPamView.getAIParams());
		deepLearningPane.addSettingsListener(()->{
			deepLearningPane.getParams(aiPamView.getAIParams()); 
			aiPamView.checkSettings(); 
		});

		//the annotation pane. 
		annotationPane = new AnnotationPane(aiPamView);
		annotationPane.setParams(aiPamView.getAIParams());
		audioImportPane.addSettingsListener(()->{
			audioImportPane.getParams(aiPamView.getAIParams()); 
			aiPamView.checkSettings(); 
		});

		//the general settings pane. 
		generalSettingsPane = new GeneralSettingsPane(aiPamView); 
		generalSettingsPane.setParams(aiPamView.getAIParams());
		generalSettingsPane.addSettingsListener(()->{
			generalSettingsPane.getParams(aiPamView.getAIParams()); 
			aiPamView.checkSettings(); 
		});


		//layout of everything
		holderPane.getChildren().add(masterControlPane.getPane());


		//specific settings button panes. 
		saveSettings = new Button("Save Settings"); 
		saveSettings.getStyleClass().add("fluent-menu-button");
		AIPamView.setButtonIcon(saveSettings, "fa-save"); 
		saveSettings.prefWidthProperty().bind(holderPane.widthProperty());
		saveSettings.setTooltip(new Tooltip(
				"Save a settings file. This can be opened iby a new instance of the \n"
						+ "program to restore the current settings."));
		saveSettings.setOnAction((actiom)->{
			this.aiPamView.saveSettings(); 
		});

		//add the list of menu buttons
		menuItems.add(new StandardPaneMenuItem(saveSettings, saveSettings.getText()));

		importSettings = new Button("Load Settings"); 
		importSettings.getStyleClass().add("fluent-menu-button");
		AIPamView.setButtonIcon(importSettings, "fa-download"); 
		importSettings.prefWidthProperty().bind(holderPane.widthProperty());
		importSettings.setTooltip(new Tooltip(
				"Import settings from a .mat settings file."));
		importSettings.setOnAction((actiom)->{
			this.aiPamView.loadSettings(); 

		});
		
		//add the list of menu buttons. 
		menuItems.add(new StandardPaneMenuItem(importSettings, importSettings.getText()));


		//add save stuff. 
		holderPane.getChildren().add(saveSettings);
		holderPane.getChildren().add(importSettings);


		controlPanes = new ArrayList<SettingsPane<AIPamParams>>(); 

		controlPanes.add(audioImportPane);
		controlPanes.add(fftPane);
		controlPanes.add(playBackPane);

		controlPanes.add(featurePane);
		controlPanes.add(clusterPane);
		controlPanes.add(deepLearningPane);
		controlPanes.add(annotationPane);

		controlPanes.add(generalSettingsPane);

		menuPane = new FluentMenuPane(); 

		for (int i=0; i<controlPanes.size(); i++) {
			final Button settingsButton = new Button(controlPanes.get(i).getTitle());
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
//			settingsButton.setContentDisplay(ContentDisplay.LEFT);
			settingsButton.setAlignment(Pos.BASELINE_LEFT);
			settingsButton.setGraphicTextGap(15);

			
			settingsButton.setTooltip(new Tooltip(controlPanes.get(i).getDescription()));
			//settingsButton.setTextAlignment(TextAlignment.LEFT);
			menuPane.addMenuItem(settingsButton);		

			//			settingsButton.setStyle("-fx-background-color: red;");
			StandardPaneMenuItem menuItem = new StandardPaneMenuItem(settingsButton, controlPanes.get(i).getTitle());
			menuItems.add(menuItem); 

		}


		holderPane.getChildren().add(menuPane);

		holderPane.getStyleClass().add("fluent-pane");

		return holderPane; 
	}


	/**
	 * Set the menu to be collapsed. 
	 */
	public void setMenuPaneCollapsed(boolean collapse) {
		for (int i=0; i<menuItems.size(); i++) {
			menuItems.get(i).showLabel(!collapse);
		}
		if (collapse) menuHolderPane.setPrefWidth(45);
		else menuHolderPane.setPrefWidth(200);


	}


	/**
	 * Set params for all the settings panes
	 * @param params - the parameters to set. 
	 */
	public void setParams(AIPamParams params) {
		for (int i=0; i<controlPanes.size(); i++) {
			controlPanes.get(i).getPane(); //FIXME need to make sure it's intialised...bit messy
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
			controlPanes.get(i).getPane(); //FIXME need to make sure it's intialised...bit messy
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

	/**
	 * Set the settings menu to be deselected.
	 */
	public void setMenuDeselected() {
		this.menuPane.setMenuButtonsDeselected(); 

	}

	/**
	 * The volume property of the slider. 
	 * @return the volume property set by the volume control. 
	 */
	public DoubleBinding volumeProperty() {
		return playBackPane.volumeProperty();
	}

	/**
	 * Called whenever a settings pane is programmatically opened to highlight the relevant menu button. 
	 * @param settingsPane - the settings pane which is being opened.
	 */
	public void setSelectedPane(SettingsPane<AIPamParams> settingsPane) {
		menuPane.setMenuButtonsDeselected(); //clear all selections. 
		menuPane.buttonSelected(controlPanes.indexOf(settingsPane));
	}

	/**
	 * Set user prompts. 
	 * @param userPromptsA
	 */
	public void setUserPrompt(ArrayList<UserPrompt> userPromptsA) {
		masterControlPane.setUserPrompts(userPromptsA); 
		for (int i=0; i<controlPanes.size(); i++) {
			controlPanes.get(i).notifyUpdate(AiPamController.USER_PROMPT, userPromptsA);
		}
	}

	/**
	 * Disables the main control menu buttons. 
	 * @param disable - true to disable
	 */
	public void setControlButtonDisable(boolean disable) {
		masterControlPane.setControlButtonDisable(disable); 
	}

	/**
	 * Send update flag. 
	 * @param flag - the flag. 
	 * @param stuff - object containing data. 
	 */
	public void notifyUpdate(int flag, Object stuff) {
		for (int i=0; i<controlPanes.size(); i++) {
			controlPanes.get(i).notifyUpdate(flag, stuff);
		}	
		switch (flag) {
		//		case AiPamController.NEW_CLIP_SELECTED:
		//			fftPane.notifyUpdate(flag, stuff);
		//			featurePane.notifyUpdate(flag, stuff);
		//			break;
		//		case AiPamController.FEATURES_CHANGED:
		//			featurePane.notifyUpdate(flag, stuff);
		//			break;
		//		}
		case AiPamController.GENERAL_SETTINGS_CHANGED:
			setMenuPaneCollapsed(aiPamView.getAIParams().collapseMenu); 
			break;
		}
	}


}
