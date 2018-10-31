package com.jamdev.maven.aipam.layout;

import org.controlsfx.control.ToggleSwitch;

import com.jamdev.maven.aipam.clustering.tsne.TSNEParams;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;


public class TSNEClusterPane extends DynamicSettingsPane<TSNEParams> {
	
	Pane mainPane;
	
	/**
	 * The toggle button for PCA
	 */
	private ToggleSwitch pcaToggle; 
	
	/**
	 * The intial dimesniosn box. 
	 */
	private ComboBox<Integer> initialDimensionsBox;

	/**
	 * The perplexity box
	 */
	private ComboBox<Integer> perplexityBox;

	/*
	 * The theta value box. 
	 */
	private ComboBox<Double> thetaBox;

	/**
	 * The maximum number of iterations. 
	 */
	private ComboBoxBase<Integer> maxIterationsBox;  
	
	public TSNEClusterPane() {
		mainPane = createMainPane(); 
	}

	private VBox createMainPane() {

		//initial dims
		Label initialDimsLabel = new Label("Initial Dimensions"); 
		initialDimsLabel.getStyleClass().add("label-title2");
		
		ObservableList<Integer> initalDimensions = FXCollections.observableArrayList();
		initalDimensions.addAll(5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60); 
		
		initialDimensionsBox = new ComboBox<Integer>(initalDimensions);
		initialDimensionsBox.setTooltip(
				new Tooltip("The tsne optimization algorithm uses these points as initial values."));
		initialDimensionsBox.setOnAction((action)->{
			notifySettingsListeners();
		});
		
		//perplexity
		Label perplexityLabel = new Label("Perplexity"); 
		perplexityLabel.getStyleClass().add("label-title2");

		ObservableList<Integer> perplexityValues = FXCollections.observableArrayList();
		perplexityValues.addAll(1, 2, 3,4, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 70, 80, 90, 100); 
		
		perplexityBox = new ComboBox<Integer>(perplexityValues); 
		perplexityBox.setTooltip(
				new Tooltip("Effective number of local neighbors of each point, specified as a positive scalar.\n" + 
						"Larger perplexity causes tsne to use more points as nearest neighbors. Use a larger value \n"
						+ " of Perplexity for a large dataset. Typical Perplexity values are from 5 to 50."));
		perplexityBox.setOnAction((action)->{
			notifySettingsListeners();
		});
		
		
		//theta
		Label thetaLabel = new Label("Theta"); 
		thetaLabel.getStyleClass().add("label-title2");
		
		ObservableList<Double> thetaValues = FXCollections.observableArrayList();
		double value = 0.0;
		while (value <=1.0) {
			thetaValues.add(value);
			value=value+0.05;
		}
		thetaBox = new ComboBox<Double>(thetaValues); 
		thetaBox.setTooltip(
				new Tooltip("Barnes-Hut tradeoff parameter, specified as a scalar from 0 \n"
									+ "through 1. Higher values give a faster but less accurate \n"
									+ "optimization."));
		thetaBox.setConverter(new DoubleDecimalConverter());
		thetaBox.setOnAction((action)->{
			notifySettingsListeners();
		});
		
		
		//max iterations.
		Label maxIterationsLabel = new Label("Max. Iterations"); 
		perplexityLabel.getStyleClass().add("label-title2");

		ObservableList<Integer> maxIterationsValues = FXCollections.observableArrayList();
		maxIterationsValues.addAll(50, 100, 200, 500, 750, 1000, 1250, 1500, 2000, 3500, 3000, 4000, 5000); 
		maxIterationsBox = new ComboBox<Integer>(maxIterationsValues); 
		maxIterationsBox.setOnAction((action)->{
			notifySettingsListeners();
		});
		
		//use PCA
		Label usePCA = new Label("Use PCA"); 
		usePCA.getStyleClass().add("label-title2");
		
		pcaToggle = new ToggleSwitch();
		pcaToggle.setTooltip(
				new Tooltip("Use principle component analysis to extract features from spectrgram fingerprints"));
		pcaToggle.selectedProperty().addListener((obsVal, oldVal, newVal)->{
			notifySettingsListeners();
		});
		
		VBox vBox = new VBox();
		vBox.setSpacing(5);

		vBox.getChildren().addAll(initialDimsLabel, initialDimensionsBox, perplexityLabel, perplexityBox,
				thetaLabel, thetaBox, maxIterationsLabel, maxIterationsBox, usePCA, pcaToggle);
	
		return vBox;
	}
	
	/**
	 * Class for showing seconds value on clip size combo box. 
	 * @author Jamie Macaulay 
	 *
	 */
	public class DoubleDecimalConverter extends StringConverter<Double>
	{

		@Override
		public String toString(Double object) {
			//add a seconds value
			return String.format("%.2f", object);
		}

		@Override
		public Double fromString(String string) {
			//remove all letters from value and try to parse the double
			//String str = string.replaceAll("[^\\d.]", "");
			return Double.parseDouble(string); 
		}

	}

	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public TSNEParams getParams(TSNEParams paramsIn) {

		System.out.println("Get the cluster params");
		
		paramsIn.initialDim = initialDimensionsBox.getValue(); 
		paramsIn.perplexity = perplexityBox.getValue(); 

		paramsIn.maxIterations = maxIterationsBox.getValue(); 
		
		paramsIn.theta = this.thetaBox.getValue(); 
		paramsIn.usePCA = this.pcaToggle.isSelected();

		return paramsIn;
	}

	@Override
	public void setParams(TSNEParams params) {
		
		initialDimensionsBox.setValue(params.initialDim);
		perplexityBox.setValue(params.perplexity);
		
		maxIterationsBox.setValue(params.maxIterations);
		
		thetaBox.setValue(params.theta);
		pcaToggle.setSelected(params.usePCA);
	}

	@Override
	public Node getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "TSNE Cluster Settings";
	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// TODO Auto-generated method stub
		
	}

}
