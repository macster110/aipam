package com.jamdev.maven.aipam.clustering.tsne;

import java.util.ArrayList;

import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.clustering.Params;
import com.jamdev.maven.aipam.clustering.ClusteringAlgorithm;
import com.jamdev.maven.aipam.clustering.FingerPrintManager;
import com.jamdev.maven.aipam.clustering.StandardTrainingListener;
import com.jamdev.maven.aipam.utils.AiPamUtils;
import com.jujutsu.tsne.TSneConfiguration;
import com.jujutsu.tsne.barneshut.BHTSne;
import com.jujutsu.tsne.barneshut.BarnesHutTSne;
import com.jujutsu.tsne.barneshut.ParallelBHTsne;
import com.jujutsu.utils.MatrixOps;
import com.jujutsu.utils.TSneUtils;

/**
 * Algorithm which clusters clips based on TSNE high dimensional clustering algorithm. 
 * 
 * Based on code form the YourKit library.
 * 
 * @author Jamie Macaulay
 *
 */
@SuppressWarnings("deprecation")
public class TSNEClipClustererYK  implements ClusteringAlgorithm {
	/**
	 * The perplexity 
	 */
	private int perplexity = 30 ;
	
	/**
	 * The number of initial dimensions
	 */
	private int initial_dims = 30;
	
	/**
	 * Use principle component analysis.
	 */
	private boolean usePCA = false; 
	
	/**
	 * The maximum umber of iterations. 
	 */
	private int maxIterations = 1000;

	/**
	 * The theta value. Can onlu be 0->1. 
	 */
	private double theta = 0.5;

	private AiPamController aiPamControl;


	public TSNEClipClustererYK(AiPamController aiPamControl) {
		this.aiPamControl=aiPamControl; 
	}

	@Override
	public void cluster(ArrayList<PAMClip> pamClips, Params clusterParams) {
		try {
			
			//set up the params
			this.perplexity = ((TSNEParams) clusterParams).perplexity; 
			this.initial_dims = ((TSNEParams) clusterParams).initialDim; 
			this.usePCA = ((TSNEParams) clusterParams).usePCA; 
			this.maxIterations = ((TSNEParams) clusterParams).maxIterations; 
			this.theta = ((TSNEParams) clusterParams).theta; 

			
			double[][] fingerprints = new double[pamClips.size()][]; 	
			
			for (int i=0; i<pamClips.size(); i++) {
				//fingerprints[i] =ArrayUtil.flatten(pamClips.get(i).getSpectrogram());
				//TODO - need to get features. 
				
				double[][] data = aiPamControl.getFeatureExtractionManager().getCurrentFeatureExtractor().getFeatures(pamClips.get(i).getSpectrogram());
				
//				fingerprints[i] = MatrixOps.asVector(data); 
				fingerprints[i] = FingerPrintManager.simpleSpectrogramFingerPrint(data, 100); 
//				AiPamUtils.printArray(data);

				//System.out.println("The size of fingerprint is: " + fingerprints[i].length);
			}
			
			
			double[][] clusterResults = clutserTSNE(fingerprints); 
			
			//add data to pamclips 
			for (int i=0; i<pamClips.size(); i++) {
				pamClips.get(i).setClusterPoint(clusterResults[i]);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double[][] clutserTSNE(double [][] X) {


		BarnesHutTSne tsne;
		boolean parallel = true;
		if(parallel) {			
			tsne = new ParallelBHTsne();
		} else {
			tsne = new BHTSne();
		}
		
//		System.setOut(new PrintStream(System.out) {
//			  public void println(String s) {
//			    super.println("Hello: " + s);
//			    
//			  }
//		});
		
		
		TSneConfiguration config = TSneUtils.buildConfig(X, 2, initial_dims, perplexity, maxIterations);
		config.setTheta(theta);
		config.setUsePca(usePCA);
		
		if (perplexity>X.length) {
			System.err.println("Perplexity too large for the number of data points!");
			return null; 
		}
				
		double[][] Y = tsne.tsne(config); 

		return Y;
	}

	

	@Override
	public StandardTrainingListener getTrainingListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCLusterType() {
		return "tsneYK";
	}

}
