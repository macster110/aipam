package com.jamdev.maven.aipam.layout.featureExtraction;


import com.jamdev.maven.aipam.featureExtraction.specNoiseReduction.AverageSubtraction;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Pane which holds settings for the AverageSubtraction method. 
 * @author Jamie Macaulay
 *
 */
public class AverageSubtractionNodeFX implements SpecNoiseNodeFX {

	/**
	 * Reference to average subtraction method. 
	 */
	private AverageSubtraction averageSubtraction;

	/**
	 * Holds average subtraction specific settings 
	 */
	private HBox dialogPanel;

	private TextField updateConstant;

	public AverageSubtractionNodeFX(
			AverageSubtraction averageSubtraction) {
		super();
		this.averageSubtraction = averageSubtraction;

		dialogPanel = new HBox();
		dialogPanel.setSpacing(5); 

		dialogPanel.getChildren().add(new Label("Update constant (e.g. .02) "));
		dialogPanel.getChildren().add(updateConstant = new TextField());
		updateConstant.setPrefColumnCount(6);
	}

	@Override
	public void setSelected(boolean selected) {
		updateConstant.setDisable(!selected);

	}



	@Override
	public boolean getParams() {
		try {
			double newVal = 
					Double.valueOf(updateConstant.getText());
//			if (newVal <= 0 || newVal > 0.5) {
//				
//				PamDialogFX.showMessageDialog("Average Subtraction Error",
//						"Average Subtraction update constant must be between 0 and 0.5");
//				return false;
//			}
			averageSubtraction.averageSubtractionParameters.updateConstant = newVal;
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}


	@Override
	public void setParams() {
		updateConstant.setText(String.format("%.3f", 
				averageSubtraction.averageSubtractionParameters.updateConstant));

	}


	@Override
	public Node getPane() {
		return dialogPanel;
	}


}


