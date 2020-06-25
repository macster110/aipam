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
	public AIPamParams getParams(AIPamParams params) {
		//		System.out.println("Notify settings listeners: " ); 

		params.featureParams.currentFeatureIndex=this.featureSelectionBox.getSelectionModel().getSelectedIndex();
		
		for (int i=0; i<params.featureParams.featureParams.length; i++) {
			if (featureExtractionManager.getCurrentFeatureExtractor().getSettingsPane()!=null) {
				params.featureParams.featureParams[i] = featureExtractionManager.getCurrentFeatureExtractor().getSettingsPane().getParams(params.featureParams.featureParams[i] );  
			}
		}

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

	}


	/**
	 * Update the image in the spectrogram display. This decides whether to plot as log and autimatically assigns colour limits. 
	 * 
	 * @param pamClip - the clips. 
	 */
	private void updateSpecImage(PAMClip pamClip) {
		
		if (pamClip==null) return; 
		
		
		
		double[][] featureData = featureExtractionManager.getCurrentFeatureExtractor().getFeatures(pamClip.getSpectrogram()); 

		//		System.out.println("Feature Extraction ColourLims: " + featureExtractionManager.getCurrentFeatureExtractor().getName() + "  " + featureData.length); 
		//		System.out.println("Min max " + " min: " + AiPamUtils.min(featureData) + " max: " + AiPamUtils.max(featureData)); 

		double[] colourlims = new double[2];
		colourlims[0] = this.aiPamView.getAIControl().getParams().colourLims[0];
		colourlims[1] = this.aiPamView.getAIControl().getParams().colourLims[1];

		//		System.out.println("Old colour lims: " + " min: " + colourlims[0] + " max: " + colourlims[1]); 
		//AiPamUtils.printArray(featureData);

		double minFeatureData = AiPamUtils.min(featureData); 
		double maxFeatureData = AiPamUtils.max(featureData); 

		if (this.featureExtractionManager.getCurrentFeatureExtractor().logPlot()) {
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
				this.aiPamView.getCurrentColourArray(), colourlims, 
				this.featureExtractionManager.getCurrentFeatureExtractor().logPlot()); 

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
		case AiPamController.FEATURES_CHANGED:
			if (currentPreviewClip!=null) 
				updateSpecImage(currentPreviewClip);
			break;

		}
	}


}
