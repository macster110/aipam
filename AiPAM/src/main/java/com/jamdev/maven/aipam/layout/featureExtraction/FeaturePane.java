package com.jamdev.maven.aipam.layout.featureExtraction;

import java.util.ArrayList;

import org.controlsfx.control.ToggleSwitch;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.featureExtraction.FeatureExtraction;
import com.jamdev.maven.aipam.featureExtraction.FeatureExtractionManager;
import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.ColourArray;
import com.jamdev.maven.aipam.layout.MasterControlPane;
import com.jamdev.maven.aipam.layout.UserPrompts.UserPrompt;
import com.jamdev.maven.aipam.layout.clips.FullClipPane;
import com.jamdev.maven.aipam.layout.clips.SpectrogramImage;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.utils.AiPamUtils;
import com.jamdev.maven.aipam.utils.ClipSpectrogram;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Pane for feature extraction settings. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class FeaturePane extends DynamicSettingsPane<AIPamParams> {

	/**
	 * The border pane. 
	 */
	private VBox mainPane;

	/**
	 * The combo box to select what type of feature manager to use. 
	 */
	private ComboBox<String> featureSelectionBox;

	/**
	 * Reference to AiPamView; 
	 */
	private AIPamView aiPamView;

	private ImageView specImage;

	private BorderPane clipPreviewHolder;

	private PAMClip currentPreviewClip;

	/**
	 * Convenient reference to the feature extraction manager. 
	 */
	private FeatureExtractionManager featureExtractionManager;

	/**
	 * The settings pane holder for different feature extraction methods. 
	 */
	private BorderPane settingsPane;

	private ToggleSwitch showFeaturesSwitch;

	private BorderPane userPromptPane;

	/**
	 * Create the feature pane
	 */
	public FeaturePane(AIPamView aiPamView) {
		this.aiPamView = aiPamView; 
		this.featureExtractionManager = aiPamView.getAIControl().getFeatureExtractionManager(); 


		mainPane = new VBox(); 
		mainPane.setSpacing(10);

		Label titleLabel = new Label("Feature Extraction");
		titleLabel.getStyleClass().add("label-title1");


		Label selectFeatures = new Label("Method");
		selectFeatures.getStyleClass().add("label-title2");


		featureSelectionBox = new ComboBox<String>(); 
		ArrayList<FeatureExtraction> featureExtractors = aiPamView.getAIControl().getFeatureExtractionManager().getFeatureExtractors(); 
		for (FeatureExtraction featureExtractor: featureExtractors) {
			featureSelectionBox.getItems().add(featureExtractor.getName()); 
		}
		featureSelectionBox.prefWidthProperty().bind(mainPane.widthProperty()); 

		featureSelectionBox.setOnAction((action)->{
			System.out.println("Notify settings listeners: " ); 
			notifySettingsListeners(); //updates the set params function
			setFeatureExtractorPane(); 
			updateSpecImage(currentPreviewClip);
		});

		featureSelectionBox.getSelectionModel().select(featureExtractionManager.getFeatureParams().currentFeatureIndex);

		//user options ot display all clips as features. 
		showFeaturesSwitch = new ToggleSwitch(); 
		showFeaturesSwitch.selectedProperty().addListener((obs, oldVal, newVal)->{
			notifySettingsListeners();
		});

		Label label = new Label("Display features in all clips");
		label.getStyleClass().add("label-title2");
		HBox hBox = new HBox(); 
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setSpacing(5);
		hBox .getChildren().addAll(showFeaturesSwitch, label); 

		mainPane.getChildren().addAll(titleLabel, selectFeatures, featureSelectionBox, 
				settingsPane = new BorderPane(), createClipPane(), hBox, userPromptPane= new BorderPane());

	}
	
	/**
	 * Create pane to allow user to recalculate 
	 * @return
	 */
	private Pane createRecalcPane() {
		HBox recalcPane = new HBox(); 
		recalcPane.setAlignment(Pos.CENTER_LEFT);
		recalcPane.setSpacing(5); 

		Button recalc = new Button("Recalculate"); 
		recalc.setOnAction((action)->{
			aiPamView.reCalcImages();
		}); 
		
		recalcPane.getChildren().addAll(new Label("Features need recalculated"), recalc); 
		
		MasterControlPane.highLightButton(recalc); 

		return recalcPane; 
	}
	
	/**
	 * Called whenever there is a new user prompt. Convenience for the user to recalc spectrograms. 
	 * @param list - the userprompt list.  
	 */
	private void newUserPrompt(ArrayList<UserPrompt> list) {
		this.userPromptPane.setCenter(null); 
		for (UserPrompt prompt : list) {
			if (prompt == UserPrompt.RECREATE_FEATURES) {
				this.userPromptPane.setCenter(createRecalcPane() );
			}
		}
	}



	/**
	 * Shows a preview of the clip
	 */
	private Pane createClipPane() {

		specImage = new ImageView(); 

		clipPreviewHolder = new BorderPane(); 

		clipPreviewHolder.setCenter(specImage);

		Label colourScale = new Label("Features Preview");
		colourScale.getStyleClass().add("label-title2");

		clipPreviewHolder.setTop(colourScale);
		clipPreviewHolder.setCenter(new Label("Select an imported clip to see a preview. \n"
				+ "Use this to test feature extraction parameters."));

		return clipPreviewHolder; 
	}

	/**
	 * Set the pane for the feature extractor. 
	 */
	private void setFeatureExtractorPane() {
		settingsPane.setCenter(null); 
		if (featureExtractionManager.getCurrentFeatureExtractor().getSettingsPane()!=null) {
			settingsPane.setCenter(featureExtractionManager.getCurrentFeatureExtractor().getSettingsPane().getPane());
		}

	}

	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public AIPamParams getParams(AIPamParams params) {
		//		System.out.println("Notify settings listeners: " ); 

		params.featureParams.currentFeatureIndex=this.featureSelectionBox.getSelectionModel().getSelectedIndex();

		for (int i=0; i<params.featureParams.featureParams.length; i++) {
			if (featureExtractionManager.getCurrentFeatureExtractor().getSettingsPane()!=null) {
				params.featureParams.featureParams[i] = featureExtractionManager.getCurrentFeatureExtractor().getSettingsPane().getParams(params.featureParams.featureParams[i] );  
			}
		}

		params.showFeatures = showFeaturesSwitch.isSelected(); 

		return params;
	}

	@Override
	public void setParams(AIPamParams params) {
		//TODO
		//params.featureParams.currentFeatureIndex=this.featureSelectionBox.getSelectionModel().getSelectedIndex();
		this.featureSelectionBox.getSelectionModel().select(params.featureParams.currentFeatureIndex);

		for (int i=0; i<params.featureParams.featureParams.length; i++) {
			if (featureExtractionManager.getCurrentFeatureExtractor().getSettingsPane()!=null) {
				featureExtractionManager.getCurrentFeatureExtractor().getSettingsPane().setParams(params.featureParams.featureParams[i]);
			}
		}
		
		showFeaturesSwitch.setSelected(params.showFeatures);

	}
	
	
	/**
	 * Get the image for feature extractions. 
	 * @param spectrogram - the spectrogram. 
	 * @param featureExtractor - the feature extractor
	 * @param colourlims - the default colour limits. These can be overiden by the feature extraction method.  
	 * @param colourArray - the default colour array. 
	 * @return the feature image. 
	 */
	public static SpectrogramImage getFeatureImage(ClipSpectrogram spectrogram, FeatureExtraction featureExtractor, double[] colourlims, ColourArray colourArray) {
		double[][] featureData = featureExtractor.getFeatures(spectrogram); 


		//		System.out.println("Old colour lims: " + " min: " + colourlims[0] + " max: " + colourlims[1]); 
		//AiPamUtils.printArray(featureData);

		double minFeatureData = AiPamUtils.min(featureData); 
		double maxFeatureData = AiPamUtils.max(featureData); 

		if (featureExtractor.logPlot()) {
			minFeatureData = 20*Math.log10(minFeatureData); 
			maxFeatureData = 20*Math.log10(maxFeatureData); 
		}

		//check whether the colour lims are maybe sensible bounds. If not use the min and max of the spectrum
		if (1.3*minFeatureData<colourlims[0] || 0.7*maxFeatureData>colourlims[1]) {
			//use different colour lims
			colourlims[0] = minFeatureData;
			colourlims[1] = maxFeatureData;
		}


		if (colourlims[0] < 0.000001) colourlims[0] = 0.000001; 

		//		System.out.println("New colour lims" + " min: " + colourlims[0] + " max: " + colourlims[1]); 

		SpectrogramImage image = new SpectrogramImage(featureData, 
				colourArray, colourlims, 
				featureExtractor.logPlot()); 
		
		return image; 
	}

	
	/**
	 * Get the feature image with correct colours. 
	 * @param spectrogram - the spectrogram
	 * @return the image of the features. 
	 */
	public 	SpectrogramImage getFeatureImage(ClipSpectrogram spectrogram) {
		
		FeatureExtraction featureExtractor = featureExtractionManager.getCurrentFeatureExtractor(); 
		ColourArray colourArray = this.aiPamView.getCurrentColourArray(); 

		//		System.out.println("Feature Extraction ColourLims: " + featureExtractionManager.getCurrentFeatureExtractor().getName() + "  " + featureData.length); 
		//		System.out.println("Min max " + " min: " + AiPamUtils.min(featureData) + " max: " + AiPamUtils.max(featureData)); 

		double[] colourlims = new double[2];
		colourlims[0] = this.aiPamView.getAIControl().getParams().spectrogramParams.colourLims[0];
		colourlims[1] = this.aiPamView.getAIControl().getParams().spectrogramParams.colourLims[1];
		
		return  getFeatureImage( spectrogram,  featureExtractor, colourlims,  colourArray); 
	}


	/**
	 * Update the image in the spectrogram display. This decides whether to plot as log and autimatically assigns colour limits. 
	 * 
	 * @param pamClip - the clips. 
	 */
	private void updateSpecImage(PAMClip pamClip) {

		if (pamClip==null) return; 

		SpectrogramImage image =  getFeatureImage(pamClip.getSpectrogram(aiPamView.getAIControl().getParams().spectrogramParams.fftLength, 
				aiPamView.getAIControl().getParams().spectrogramParams.fftHop)); 

		specImage.setImage(image.getSpecImage(200, 200));	

		clipPreviewHolder.setLeft(FullClipPane.createFreqAxis(Side.LEFT,  pamClip)); 
		clipPreviewHolder.setCenter(specImage); 
	}

	@Override
	public Node getIcon() {
		FontIcon iconView = new FontIcon("mdi-zip-box");
		iconView.setIconSize(AIPamView.iconSize);
		iconView.setFill(Color.WHITE);
		return iconView;
	}

	@Override
	public String getTitle() {
		return "Feature Extraction";
	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		switch (flag) {
		case AiPamController.NEW_CLIP_SELECTED:
			this.currentPreviewClip = (PAMClip) stuff; 
			updateSpecImage(currentPreviewClip);
			break;
		case AiPamController.FEATURES_CHANGED:
			if (currentPreviewClip!=null) 
				updateSpecImage(currentPreviewClip);
			break;
		case AiPamController.USER_PROMPT:
			ArrayList<UserPrompt> list = (ArrayList<UserPrompt> ) stuff; 
			newUserPrompt(list);
			break; 		
			}
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}


}
