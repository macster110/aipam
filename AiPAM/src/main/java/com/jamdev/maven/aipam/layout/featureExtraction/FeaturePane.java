package com.jamdev.maven.aipam.layout.featureExtraction;

import java.util.ArrayList;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.featureExtraction.FeatureExtraction;
import com.jamdev.maven.aipam.featureExtraction.FeatureExtractionManager;
import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.clips.FullClipPane;
import com.jamdev.maven.aipam.layout.clips.SpectrogramImage;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.utils.AiPamUtils;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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


		mainPane.getChildren().addAll(titleLabel, selectFeatures, featureSelectionBox, settingsPane = new BorderPane(), createClipPane());

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
		clipPreviewHolder.setCenter(new Label("Select an imported clip to see a preview\nThis can be used to test feature extraction parameters"));

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
	public AIPamParams getParams(AIPamParams paramsIn) {
		System.out.println("Notify settings listeners: " ); 

		paramsIn.featureParams.currentFeatureIndex=this.featureSelectionBox.getSelectionModel().getSelectedIndex();
		return paramsIn;
	}

	@Override
	public void setParams(AIPamParams params) {
		//TODO
		//params.featureParams.currentFeatureIndex=this.featureSelectionBox.getSelectionModel().getSelectedIndex();
	}


	/**
	 * Update the image in the spectrogram display
	 * @param pamClip - the clips. 
	 */
	private void updateSpecImage(PAMClip pamClip) {
		if (pamClip==null) return; 
		double[][] featureData = featureExtractionManager.getCurrentFeatureExtractor().getFeatures(pamClip.getSpectrogram()); 

		System.out.println("Feature Extraction ColourLims: " + featureExtractionManager.getCurrentFeatureExtractor().getName() + "  " + featureData.length); 
		
		SpectrogramImage image = new SpectrogramImage(featureData, 
				this.aiPamView.getCurrentColourArray(), new double[] {this.aiPamView.getAIControl().getParams().colourLims[0]
						, this.aiPamView.getAIControl().getParams().colourLims[1]}); 

		specImage.setImage(image.getSpecImage(200, 200));	

		clipPreviewHolder.setLeft(FullClipPane.createFreqAxis(Side.LEFT,  pamClip)); 
		clipPreviewHolder.setCenter(specImage); 
	}

	@Override
	public Node getIcon() {
		MaterialDesignIconView iconView = new MaterialDesignIconView(MaterialDesignIcon.ZIP_BOX); 
		iconView.setGlyphSize(AIPamView.iconSize);
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
		}
	}


}
