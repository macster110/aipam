package com.jamdev.maven.aipam.layout.featureExtraction;


import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.AverageSubtraction;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.AverageSubtractionParameters;
import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.ThresholdParams;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Pane which holds settings for the AverageSubtraction method. 
 * @author Jamie Macaulay
 *
 */
public class AverageSubtractionPane  extends DynamicSettingsPane<AverageSubtractionParameters> {

	/**
	 * Reference to average subtraction method. 
	 */
	private AverageSubtraction averageSubtraction;

	/**
	 * Holds average subtraction specific settings 
	 */
	private HBox dialogPanel;

	private Spinner<Double> updateConstant;

	public AverageSubtractionPane(
			AverageSubtraction averageSubtraction) {
		super();
		this.averageSubtraction = averageSubtraction;

		dialogPanel = new HBox();
		dialogPanel.setAlignment(Pos.CENTER_LEFT);
		dialogPanel.setSpacing(5); 

		dialogPanel.getChildren().add(new Label("Update constant"));
		dialogPanel.getChildren().add(updateConstant = new Spinner<Double>(0.00, 0.5, 0.02, 0.01));
		updateConstant.valueProperty().addListener((obsVal, oldVal, newVal)->{
			notifySettingsListeners(); 
		});

		
	}

	
	@Override
	public Pane getPane() {
		// TODO Auto-generated method stub
		return dialogPanel;
	}



	@Override
	public AverageSubtractionParameters getParams(AverageSubtractionParameters params) {
		AverageSubtractionParameters paramsIn = params.clone(); 
		try {
			double newVal = 
					Double.valueOf(updateConstant.getValue());
//			if (newVal <= 0 || newVal > 0.5) {
//				
//				PamDialogFX.showMessageDialog("Average Subtraction Error",
//						"Average Subtraction update constant must be between 0 and 0.5");
//				return false;
//			}
			paramsIn.updateConstant = newVal;
		}
		catch (Exception e) {
			return null;
		}
		return paramsIn;
	}



	@Override
	public void setParams(AverageSubtractionParameters params) {
		updateConstant.getValueFactory().setValue(params.updateConstant);		
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


