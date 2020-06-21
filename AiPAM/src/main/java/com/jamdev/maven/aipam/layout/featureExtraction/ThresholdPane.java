package com.jamdev.maven.aipam.layout.featureExtraction;

import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpectrogramThreshold;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class ThresholdPane implements SpecNoiseNodeFX {

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

		thresholdPane.getChildren().add(new Label("Below threshold -> 0. Set above threshold data to ..."));

		thresholdPane.getChildren().add(outputType = new ComboBox<String>());
		outputType.getItems().add("Binary output (0's and 1's)");
		outputType.getItems().add("Use the output of the preceeding step");
		outputType.getItems().add("Use the input from the raw FFT data");

		//thresholdPane.getChildren().add(new Label("(Some downstream processes may want phase information)"));
	}

	@Override
	public boolean getParams() {
		try {
			double newVal = 
				Double.valueOf(thresholdDB.getValue());
			spectrogramThreshold.thresholdParams.thresholdDB = newVal;
		}
		catch (Exception e) {
			return false;
		}
		spectrogramThreshold.thresholdParams.finalOutput = outputType.getSelectionModel().getSelectedIndex();
		return true;
	}


	@Override
	public void setParams() {

		thresholdDB.setValue(
				spectrogramThreshold.thresholdParams.thresholdDB);
		
		outputType.getSelectionModel().select(spectrogramThreshold.thresholdParams.finalOutput);

	}

	@Override
	public void setSelected(boolean selected) {
		thresholdDB.setDisable(!selected);
	}

	@Override
	public Node getPane() {
		return thresholdPane;
	}

}

