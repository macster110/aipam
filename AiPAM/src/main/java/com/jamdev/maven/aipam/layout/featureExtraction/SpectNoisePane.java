package com.jamdev.maven.aipam.layout.featureExtraction;

import org.controlsfx.control.ToggleSwitch;

import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpecNoiseMethod;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpecNoiseReduction;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.SpecNoiseReductionParams;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Settings pane for spectrogram noise reduction.
 * 
 * @author Jamie Macaulay
 *
 */
public class SpectNoisePane extends DynamicSettingsPane<SpecNoiseReductionParams> {

	/**
	 * The main pane. 
	 */
	private VBox mainPane;

	/**
	 * Spectrogram noise reduction feature.
	 */
	private SpecNoiseReduction specNoiseReduction;

	private ToggleSwitch[] enabledButtons;

	public SpectNoisePane(SpecNoiseReduction specNoiseReduction) {
		this.specNoiseReduction = specNoiseReduction; 

		this.mainPane = new VBox(); 
		mainPane.setSpacing(10);

		enabledButtons = new ToggleSwitch[specNoiseReduction.getSpecNoiseMethods().size()]; 

		int n=0;
		for (SpecNoiseMethod specNoiseMethod:specNoiseReduction.getSpecNoiseMethods()) {

			DynamicSettingsPane specNoisePane = specNoiseMethod.getSettingsPane();

			Label titleLabel = new Label(specNoiseMethod.getName()); 
			titleLabel.setTooltip(new Tooltip(specNoiseMethod.getDescription()));
			titleLabel.getStyleClass().add("label-title2");

			ToggleSwitch toggleButton = new ToggleSwitch(); 
			toggleButton.setPrefWidth(80);
			toggleButton.setTooltip(new Tooltip(specNoiseMethod.getDescription()));
			enabledButtons[n] = toggleButton;

			toggleButton.selectedProperty().addListener((obs, oldVal, newVal)->{
				titleLabel.setDisable(!toggleButton.isSelected());
				if (specNoisePane!=null) specNoisePane.getPane().setDisable(!toggleButton.isSelected());
				//there are new settings so may need to update the pane. 
				newSettings();

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
			
			//add a listener to check for any changes to settings in the method pane. 
			if (specNoisePane!=null) {
				methodPane.getChildren().add(specNoisePane.getPane());
				specNoisePane.addSettingsListener(()->{
					//new settings have occurred in one of noise reduction the method settings pane
					newSettings(); 
					
				});
			}

			mainPane.getChildren().add(methodPane); 
			n++;
		}

	}
	
	
	/**
	 * Called whenever a control is changes to update all settings fields 
	 */
	private void newSettings() {
		//new settings have occurred in one of noise reduction the method settings pane

		//update the parameters
		SpecNoiseReductionParams newParams = getParams(specNoiseReduction.getParams()); 
		
		if (newParams==null) {
			System.err.println("SpecNoiseReductionParams  are null"); 
		}
		else {
			//set the new params. 
			specNoiseReduction.setParams(newParams);
		}
		
		//update the fields in the noise methods. Each noise method has it's own settings pane but this is the only listener 
		//that is trigered if a setting changes. Thus the settings for each method are handled centrally here. Might not be the neatest 
		//way to do it but easiest if adding new methods. 
		updateMethodParams(newParams); 
		
		//update the preview clip
		specNoiseReduction.getFeatureExtractionManager().notifySelectedClipChange();
		
		
	}
	
	/**
	 * Update the current method params fields with those stored in SpecNoiseReductionParams 
	 * @param newParams
	 */
	private void updateMethodParams(SpecNoiseReductionParams newParams) {
		for (int i=0; i<specNoiseReduction.getSpecNoiseMethods().size(); i++) {
			specNoiseReduction.getSpecNoiseMethods().get(i).setParams(newParams.specNoiseParams[i]);
		}
	}

	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public SpecNoiseReductionParams getParams(SpecNoiseReductionParams params) {
		SpecNoiseReductionParams paramsIn =  specNoiseReduction.getParams(); //TODO for some reaoson this throws a null pointer exception. 

		for (int i=0; i<specNoiseReduction.getSpecNoiseMethods().size(); i++) {
			
			paramsIn.runMethod[i] = enabledButtons[i].isSelected();

			if (specNoiseReduction.getSpecNoiseMethods().get(i).getSettingsPane()!=null) {

				Object newParams= specNoiseReduction.getSpecNoiseMethods().get(i).getSettingsPane().getParams(paramsIn.specNoiseParams[i]); 
								
				if (newParams==null) {
					System.err.println("Parameters for: " + i + " are null"); 
					return null; 
				}

				paramsIn.specNoiseParams[i] = newParams; 
				
			}
		}
	
		return paramsIn; 
	}


	@Override
	public void setParams(SpecNoiseReductionParams params) {
		SpecNoiseReductionParams specNoiseParams =  params; 

		for (int i=0; i<enabledButtons.length; i++) {
			enabledButtons[i].setSelected(specNoiseParams.runMethod[i]);
			if (specNoiseReduction.getSpecNoiseMethods().get(i).getSettingsPane()!=null) {
				specNoiseReduction.getSpecNoiseMethods().get(i).getSettingsPane().setParams(specNoiseParams.specNoiseParams[i]);
			}
		}



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


	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
