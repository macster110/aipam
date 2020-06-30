package com.jamdev.maven.aipam.layout.featureExtraction;

import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpectrogramThreshold;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.ThresholdParams;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ThresholdPane extends DynamicSettingsPane<ThresholdParams> {

	private SpectrogramThreshold spectrogramThreshold;
	
	/**
	 * The main threshold pane.
	 */
	private VBox thresholdPane;
	
	/**
	 * Combo box for output type
	 */
	private ComboBox<String> outputType;

	/**
	 * Slider for thresholds
	 */
	private Slider thresholdDB;
	
	public ThresholdPane(SpectrogramThreshold spectrogramThreshold) {
		super();
		this.spectrogramThreshold = spectrogramThreshold;

		thresholdPane = new VBox();
		thresholdPane.setSpacing(5);

		thresholdPane.getChildren().add(new Label("Threshold (dB) "));
		
		thresholdPane.getChildren().add(thresholdDB = new Slider(1, 30, 8));
		thresholdDB.valueProperty().addListener((obsVal, oldVal, newVal)->{
			notifySettingsListeners(); 
		});

//		thresholdPane.getChildren().add(new Label("Below threshold -> 0. Set above threshold data to ..."));

		thresholdPane.getChildren().add(outputType = new ComboBox<String>());
		outputType.getItems().add("Binary output (0's and 1's)");
		outputType.getItems().add("Use the output of the preceeding step");
		outputType.getItems().add("Use the input from the raw FFT data");
		outputType.valueProperty().addListener((obsVal, oldVal, newVal)->{
			notifySettingsListeners(); 
		});

		//thresholdPane.getChildren().add(new Label("(Some downstream processes may want phase information)"));
		
		setParams(spectrogramThreshold.thresholdParams); 
	}

	@Override
	public Pane getPane() {
		// TODO Auto-generated method stub
		return thresholdPane;
	}

	@Override
	public ThresholdParams getParams(ThresholdParams paramsIn) {
		ThresholdParams paramsIN = paramsIn.clone(); 
		try {
			double newVal = 
				Double.valueOf(thresholdDB.getValue());
			paramsIN.thresholdDB = newVal;
		}
		catch (Exception e) {
			return null;
		}
		paramsIN.finalOutput = outputType.getSelectionModel().getSelectedIndex();
		
		return paramsIN;
	}

	@Override
	public void setParams(ThresholdParams params) {
		thresholdDB.setValue(
				params.thresholdDB);
				
				outputType.getSelectionModel().select(params.finalOutput);
	}

	@Override
	public Node getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// TODO Auto-generated method stub
		
	}

}

