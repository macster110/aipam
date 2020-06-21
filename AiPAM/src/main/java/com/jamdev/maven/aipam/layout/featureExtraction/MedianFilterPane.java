package com.jamdev.maven.aipam.layout.featureExtraction;

import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpectorgramMedianFilter;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Settings pane with controls to change parameters for the median filter/ 
 * @author Jamie Macaulay 
 *
 */
public class MedianFilterPane implements SpecNoiseNodeFX {

	private SpectorgramMedianFilter spectrogramMedianFilter;

	private HBox medianFilterPane;

	private TextField filterLength;


	public MedianFilterPane(
			SpectorgramMedianFilter spectrogramMedianFilter) {
		super();
		this.spectrogramMedianFilter = spectrogramMedianFilter;

		medianFilterPane = new HBox();
		medianFilterPane.setSpacing(5);

		medianFilterPane.getChildren().add(new Label("Filter length (should be odd) "));
		medianFilterPane.getChildren().add(filterLength = new TextField());
		filterLength.setPrefColumnCount(6);
	}


	@Override
	public void setSelected(boolean selected) {
		filterLength.setDisable(!selected);

	}
	@Override
	public boolean getParams() {
		try {
			int newVal = 
					Integer.valueOf(filterLength.getText());
			if (newVal < 3 || newVal%2 == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Filter Length Error: Filter length must be odd and >= 3");
				alert.showAndWait(); 
				return false;
			}
			spectrogramMedianFilter.medianFilterParams.filterLength = newVal;
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	public void setParams() {
		filterLength.setText(String.format("%d", spectrogramMedianFilter.medianFilterParams.filterLength));
	}

	@Override
	public Node getPane() {
		return medianFilterPane;
	}
}

