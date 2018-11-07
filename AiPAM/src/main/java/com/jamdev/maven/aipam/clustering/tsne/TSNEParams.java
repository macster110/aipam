package com.jamdev.maven.aipam.clustering.tsne;

import com.jamdev.maven.aipam.SettingsImportExport;
import com.jamdev.maven.aipam.clustering.ClusterParams;
import com.jamdev.maven.aipam.layout.utilsFX.SettingsPane;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLInt32;
import com.jmatio.types.MLStructure;

/**
 * 
 * A parameters class for the TSNE algorithm 
 * 
 * @author Jamie Macaulay. 
 */
public class TSNEParams implements ClusterParams {

	/**
	 * The learning rate. 
	 */
	public int initialDim = 30; 

	/**
	 * Effective number of local neighbours of each point, specified as a positive
	 * scalar. Larger perplexity causes t-SNE to use more points as nearest
	 * neighbours. Use a larger value of Perplexity for a large dataset. Typical
	 * Perplexity values are from 5 to 50. In the Barnes-Hut algorithm, tsne uses
	 * min(3*Perplexity,N-1) as the number of nearest neighbours.
	 */
	public int perplexity = 30; 

	/**
	 * The theta value for the t-SNE algorithm Barnes-Hut tradeoff parameter,
	 * specified as a scalar from 0 through 1. Higher values give a faster but less
	 * accurate optimisation. Applies only when Algorithm is Barnes-Hut.
	 */
	public double theta = 0.5;

	/**
	 * The maximum iterations
	 */
	public int maxIterations = 1000;

	/**
	 * Learning rate (for DeepLearning4j) TSNE
	 */
	@Deprecated
	public int learningRate = 100; 

	/**
	 * Iterations rate (for DeepLearning4j) TSNE
	 */
	@Deprecated
	public int iterations = 100;

	public boolean usePCA = false;

	@Override
	public ClusterParams clone() {
		try {
			return (ClusterParams) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null; 
		}
	}

	@Override
	public boolean compare(ClusterParams clusterParams) {

		TSNEParams tsneParams = (TSNEParams) clusterParams;

		boolean changed =false; 
		
		if (this.initialDim!=tsneParams.initialDim) 			changed=true; 
		if (this.perplexity!=tsneParams.perplexity) 			changed=true; 
		if (this.theta!=tsneParams.theta)						changed=true; 
		if (this.maxIterations!=tsneParams.maxIterations) 		changed=true; 
		if (this.usePCA!=tsneParams.usePCA) 					changed=true; 

		return !changed;
	}

	@Override
	public SettingsPane<?> getSettingsPane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void struct2ClusterParams(MLStructure mlStruct) {
		
		MLInt32 initialDim=(MLInt32) mlStruct.getField("initialdim", 0);
		MLInt32 perplexity=(MLInt32) mlStruct.getField("perplexity", 0);
		MLDouble theta=(MLDouble) mlStruct.getField("theta", 0);
		MLInt32 maxiterations=(MLInt32) mlStruct.getField("maxiterations", 0);
		MLInt32 usePCA=(MLInt32) mlStruct.getField("uspca", 0);

		this.initialDim=initialDim.get(0).intValue(); 
		this.perplexity=perplexity.get(0).intValue();
		this.theta=theta.get(0).doubleValue();
		this.maxIterations=maxiterations.get(0).intValue();
		this.usePCA=usePCA.get(0).intValue() == 1? true : false;
		
	}

	@Override
	public MLStructure clusterParams2Struct() {
		
		MLInt32 initialDim= SettingsImportExport.mlInt(this.initialDim);
		MLInt32 perplexity=SettingsImportExport.mlInt(this.perplexity); 
		MLDouble theta=SettingsImportExport.mlDouble(this.theta); 
		MLDouble maxiterations=SettingsImportExport.mlDouble(this.maxIterations); 
		MLInt32 usePCA=SettingsImportExport.mlInt(this.usePCA? 1:0); 

		MLStructure struct =  new MLStructure("clustersettings", new int[] {1,1});
		struct.setField("initialdim", initialDim);
		struct.setField("perplexity", perplexity);
		struct.setField("theta", theta);
		struct.setField("maxiterations", maxiterations);
		struct.setField("uspca", usePCA);

		return struct;
	}

}
