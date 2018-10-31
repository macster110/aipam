package com.jamdev.maven.aipam.clustering;

import java.util.List;
import java.util.Map;

import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.nd4j.linalg.api.ndarray.INDArray;

import com.jamdev.maven.aipam.clustering.PamClusterManager.ClusterTask;
import com.jamdev.maven.aipam.clustering.tsne.TSNEClipClustererDL4J;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Standard listener for clustering progress. 
 * @author Jamie Macaulay
 *
 */
public class StandardTrainingListener implements TrainingListener {

	/**
	 * Reference to the task running the algorithm  
	 */
	private ClusterTask task;

	/**
	 * The maximum iterations. 
	 */
	private double maxIterations;
	
	/**
	 * The progress property betwen 0 and 1; 
	 */
	private DoubleProperty progressProperty; 

	public void setProgressProperty(DoubleProperty progressProperty) {
		this.progressProperty = progressProperty;
	}

	public StandardTrainingListener() {}

	public StandardTrainingListener(TSNEClipClustererDL4J tsneClipClusterer) {
		this.maxIterations = tsneClipClusterer.getMaxIterations(); 
		progressProperty= new SimpleDoubleProperty(); 
	}

	@Override
	public void iterationDone(Model model, int iteration, int epoch) {
		System.out.println("Iterations Done: " + iteration); 
		progressProperty.setValue(new Double((double) iteration/ (double) maxIterations));
	}

	@Override
	public void onEpochStart(Model model) {
		System.out.println("On Epoch Start: " + model.score()); 
//		if (task!=null) {
//			task.updateMessage("Preparing the algorithm...");
//			task.updateProgress(-1, 1);
//		}
		progressProperty.setValue(new Double(-1.0));
	}

	@Override
	public void onEpochEnd(Model model) {
		System.out.println("On Epoch End " + model.score()); 

	}

	@Override
	public void onForwardPass(Model model, List<INDArray> activations) {
		System.out.println("On Forward Pass: " + activations.size()); 

	}

	@Override
	public void onForwardPass(Model model, Map<String, INDArray> activations) {
		System.out.println("Iterations Done: " + activations.size() ); 
	}

	@Override
	public void onGradientCalculation(Model model) {
		System.out.println("On Gradient Calculation: " + model.score()); 
	}

	@Override
	public void onBackwardPass(Model model) {
		System.out.println("On Backward Pass: " + model.score()); 
	}
	

	/**
	 * The progress the property. 
	 * @return the progress property.
	 */
	public DoubleProperty progressProperty() {
		return progressProperty;
	}
	


}