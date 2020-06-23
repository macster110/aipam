package com.jamdev.maven.aipam.layout.featureExtraction;

import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.MedianFilterParams;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpectorgramMedianFilter;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Settings pane with controls to change parameters for the median filter/ 
 * @author Jamie Macaulay 
 *
 */
public class MedianFilterPane extends DynamicSettingsPane<MedianFilterParams> {

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
		
		filterLength.valueProperty().addListener((obsVal, oldVal, newVal)->{
			notifySettingsListeners(); 
		});


		medianFilterPane.getChildren().add(filterLength);
	}


	@Override
	public Pane getPane() {
		return medianFilterPane;
	}


	@Override
	public MedianFilterParams getParams(MedianFilterParams paramsIn) {
		MedianFilterParams paramsIN =  paramsIn.clone(); 
		try {
			int newVal = filterLength.getValue(); 
//			if (newVal < 3 || newVal%2 == 0) {
//				Alert alert = new Alert(AlertType.ERROR);
//				alert.setContentText("Filter Length Error: Filter length must be odd and >= 3");
//				alert.showAndWait(); 
//				return false;
//			}
			paramsIN.filterLength = newVal;
		}
		catch (NumberFormatException e) {
			return null;
		}
		return paramsIN;
	}


	@Override
	public void setParams(MedianFilterParams params) {
		filterLength.getValueFactory().setValue(params.filterLength);		
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

