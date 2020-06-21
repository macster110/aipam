package com.jamdev.maven.aipam.layout.featureExtraction;

import org.controlsfx.control.ToggleSwitch;

import com.jamdev.maven.aipam.featureExtraction.SpecNoiseReduction;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpecNoiseMethod;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Settings pane for spectrogram noise reduction.
 * 
 * @author Jamie Macaulay
 *
 */
public class SpectNoisePane extends DynamicSettingsPane {
	
	/**
	 * The main pane. 
	 */
	private VBox mainPane;
	
	/**
	 * Spectrogram noise reduction feature.
	 */
	private SpecNoiseReduction specNoiseReduction;

	public SpectNoisePane(SpecNoiseReduction specNoiseReduction) {
		this.specNoiseReduction = specNoiseReduction; 
		
		this.mainPane = new VBox(); 
		mainPane.setSpacing(10);
	
		ToggleSwitch[] enabledButtons = new ToggleSwitch[specNoiseReduction.getSpecNoiseMethods().size()]; 
		
		int n=0;
		for (SpecNoiseMethod specNoiseMethod:specNoiseReduction.getSpecNoiseMethods()) {
			
			SpecNoiseNodeFX specNoisePane = specNoiseMethod.getSettingsPane();
						
			
			Label titleLabel = new Label(specNoiseMethod.getName()); 
			titleLabel.getStyleClass().add("label-title2");
			
			ToggleSwitch toggleButton = new ToggleSwitch(); 
			enabledButtons[n] = toggleButton;
			
			toggleButton.selectedProperty().addListener((obs, oldVal, newVal)->{
				titleLabel.setDisable(!toggleButton.isSelected());
				if (specNoisePane!=null) specNoisePane.getPane().setDisable(!toggleButton.isSelected());
			});
//			toggleButton.setMinWidth(100);
			
			//create the pane
			HBox headerPane = new HBox(); 
			headerPane.setSpacing(5);
			headerPane.getChildren().addAll(toggleButton, titleLabel); 
			headerPane.setAlignment(Pos.CENTER_LEFT);
			
			VBox methodPane = new VBox(); 
			methodPane.setSpacing(5);
			methodPane.getChildren().add(headerPane);
			if (specNoisePane!=null) methodPane.getChildren().add(specNoisePane.getPane());
			
			mainPane.getChildren().add(methodPane); 
			n++;
		}
		
	}

	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public Object getParams(Object paramsIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParams(Object params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node getIcon() {
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Feature Extraction";
	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// TODO Auto-generated method stub
		
	}

}
