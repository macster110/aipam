package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;

import org.nd4j.linalg.util.ArrayUtil;

import com.jamdev.maven.aipam.utils.SettingsPane;
import com.jamdev.maven.clips.PAMClip;
import com.jujutsu.tsne.TSneConfiguration;
import com.jujutsu.tsne.barneshut.BHTSne;
import com.jujutsu.tsne.barneshut.BarnesHutTSne;
import com.jujutsu.tsne.barneshut.ParallelBHTsne;
import com.jujutsu.utils.TSneUtils;

/**
 * Algorithm which clusters clips based on TSNE high dimensional clustering algorithm. 
 * 
 * Based on code form the YourKit library.
 * 
 * @author Jamie Macaulay
 *
 */
public class TSNEClipClustererYK  implements ClusteringAlgorithm {
	
	public TSNEClipClustererYK() {}

	@Override
	public void cluster(ArrayList<PAMClip> pamClips) {
		try {
			
			double[][] fingerprints = new double[pamClips.size()][]; 	
			
			for (int i=0; i<pamClips.size(); i++) {
				//fingerprints[i] =ArrayUtil.flatten(pamClips.get(i).getSpectrogram());
				fingerprints[i] = FingerPrintManager.simpleSpectrogramFingerPrint(pamClips.get(i).getSpectrogram(), 100); 
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
		int initial_dims = 30;
		double perplexity = 30.0;

		BarnesHutTSne tsne;
		boolean parallel = true;
		if(parallel) {			
			tsne = new ParallelBHTsne();
		} else {
			tsne = new BHTSne();
		}
		
		TSneConfiguration config = TSneUtils.buildConfig(X, 2, initial_dims, perplexity, 1000);
		config.setTheta(0.5);
		//config.setUsePca(true);
				
		double[][] Y = tsne.tsne(config); 

		return Y;
	}

	@Override
	public SettingsPane getSettingsPane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StandardTrainingListener getTrainingListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
