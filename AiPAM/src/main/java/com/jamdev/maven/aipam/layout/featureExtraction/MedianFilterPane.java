package com.jamdev.maven.aipam.layout.featureExtraction;

import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpectorgramMedianFilter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;

/**
 * Settings pane with controls to change parameters for the median filter/ 
 * @author Jamie Macaulay 
 *
 */
public class MedianFilterPane implements SpecNoiseNodeFX {

	private SpectorgramMedianFilter spectrogramMedianFilter;

	private HBox medianFilterPane;

	private Spinner<Integer> filterLength;


	public MedianFilterPane(
			SpectorgramMedianFilter spectrogramMedianFilter) {
		super();
		this.spectrogramMedianFilter = spectrogramMedianFilter;

		medianFilterPane = new HBox();
		medianFilterPane.setAlignment(Pos.CENTER_LEFT);

		medianFilterPane.setSpacing(5);
		

		medianFilterPane.getChildren().add(new Label("Filter length"));

		ObservableList<Integer> oddNumbers = FXCollections.observableArrayList(); 
		for (int i=3; i<1001; i=i+2) {
			oddNumbers.add(i); //add a list of odd numbers
		}

		filterLength = new Spinner<Integer>(oddNumbers);
		filterLength.setEditable(false);


		medianFilterPane.getChildren().add(filterLength);
	}


	@Override
	public void setSelected(boolean selected) {
		filterLength.setDisable(!selected);

	}
	@Override
	public boolean getParams() {
		try {
			int newVal = filterLength.getValue(); 
//			if (newVal < 3 || newVal%2 == 0) {
//				Alert alert = new Alert(AlertType.ERROR);
//				alert.setContentText("Filter Length Error: Filter length must be odd and >= 3");
//				alert.showAndWait(); 
//				return false;
//			}
			spectrogramMedianFilter.medianFilterParams.filterLength = newVal;
		}
		catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	public void setParams() {
		filterLength.getValueFactory().setValue(spectrogramMedianFilter.medianFilterParams.filterLength);
	}

	@Override
	public Node getPane() {
		return medianFilterPane;
	}
}

