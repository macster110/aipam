package com.jamdev.maven.aipam.clustering;

import java.util.List;
import java.util.Map;

import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.nd4j.linalg.api.ndarray.INDArray;

public class TSNETrainingListener implements TrainingListener {
	
	public TSNETrainingListener() {}

	@Override
	public void iterationDone(Model model, int iteration, int epoch) {
		System.out.println("Iterations Done: " + iteration); 
			
	}

	@Override
	public void onEpochStart(Model model) {
		System.out.println("On Epoch Start: " + model.score()); 
		
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
	
}