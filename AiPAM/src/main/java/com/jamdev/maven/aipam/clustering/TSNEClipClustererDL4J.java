package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;
import org.nd4j.linalg.factory.Nd4j;
import org.deeplearning4j.plot.BarnesHutTsne;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;
import com.jamdev.maven.aipam.utils.SettingsPane;
import com.jamdev.maven.clips.PAMClip;

/**
 * Algorithm which clusters clips based on TSNE high dimensional clustering algorithm. 
 * 
 * Based on code form the DeepLearning4j library.
 * 
 * @author Jamie Macaulay
 *
 */
public class TSNEClipClustererDL4J implements ClusteringAlgorithm{

	
	/**
	 * The training listener
	 */
    private StandardTrainingListener listener; 
    
    /**
     * The TSNE params
     */
    public TSNEParams params = new TSNEParams(); 
    
    
	public TSNEClipClustererDL4J() {
		listener = new StandardTrainingListener(this); 
	}
	

	@Override
	public void cluster(ArrayList<PAMClip> pamClips) {
		try {
			double[][] clusterResults = clusterFingerprints(pamClips); 
			for (int i=0; i<pamClips.size(); i++) {
				pamClips.get(i).setClusterPoint(clusterResults[i]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Create input arrtay for the TSNE algorithm.
	 * @param pamClips - the pam clips to use in the algorithm.
	 * @return the input algorithm. 
	 */
	private INDArray createDataArray(ArrayList<PAMClip> pamClips) {
		  
		double[][] fingerprints = new double[pamClips.size()][]; 	
	
		for (int i=0; i<pamClips.size(); i++) {
			fingerprints[i] = FingerPrintManager.simpleSpectrogramFingerPrint(pamClips.get(i).getSpectrogram(), 50); 
			//System.out.println("The size of fingerprint is: " + fingerprints[i].length);
		}
		
//		double[] flat = ArrayUtil.flattenDoubleArray(fingerprints);
//		int[] shape = new int[] {pamClips.size(), fingerprints[0].length,fingerprints[0][0].length };	//Array shape here
//		INDArray myArr = Nd4j.create(flat,shape,'c'); 
			
		return Nd4j.create(fingerprints); 
	}
	
	
	/**
	 * Cluster the fingerprints created from audio data using a TSNE algorithm. 
	 * @param fingerprints
	 */
	public double[][] clusterFingerprints(ArrayList<PAMClip> pamClips) {
	
    	System.err.println("Cluster fingerprints: ");

        //create an n-dimensional array of doubles
        Nd4j.setDataType(DataBuffer.Type.DOUBLE);
                
        System.out.println("Creating the   INDArray: ");

        INDArray weights = createDataArray(pamClips);    //seperate weights of unique words into their own list
        

        if (weights==null) {
        	System.err.println("The weights is null: " + weights);
        	return null; 
        }
        
        System.out.println("Creating the  cache list: ");
//
//        for(int i = 0; i < pamClips.size(); i++) {   //seperate strings of words into their own list
//        	System.out.println(pamClips.get(i).getID().toString());
//            cacheList.add(pamClips.get(i).getID().toString());
//        }
//        
        System.out.println("Build the TSNE algorithm: ");


        //STEP 3: build a dual-tree tsne to use later
        BarnesHutTsne tsne = new BarnesHutTsne.Builder()
                .setMaxIter(params.iterations).theta(0.5)
                .normalize(false)
                .learningRate(params.learningRate)
                .perplexity(30)
                .build();

        //add a listener for updates on progress 
        if (listener!=null) {
        	tsne.setTrainingListener(listener);
        }
        long time0 = System.currentTimeMillis();
        System.out.println("Begin TSNE fitting: ");
        
        //run the actual algortihm
        tsne.fit(weights);
        
        //save the clusterred points to a file. 
        INDArray tsnResult  = tsne.getData(); 
        
        long time1 = System.currentTimeMillis();
        System.out.println("End training: " + (time1-time0) + " millis");
        
       //now output data.  
       double[][] clusterPoints = new double[tsnResult.rows()][2]; 
       
       System.out.println("Rows is " + tsnResult.rows());
       INDArray rows; 
        for (int i=0; i<tsnResult.rows(); i++) {
        	rows = tsnResult.getRow(i); 
          	clusterPoints[i] = rows.toDoubleVector(); 
        	System.out.print(i + ":  ");  
        	for (int j=0; j<clusterPoints[i].length; j++) {
        		System.out.print("  " +clusterPoints[i][j]);
        	}
      
        	System.out.println(); 
        }

        
        return clusterPoints; 
      
        
        //This tsne will use the weights of the vectors as its matrix, have two dimensions, use the words strings as
        //labels, and be written to the outputFile created on the previous line
        // Plot Data with gnuplot
        //    set datafile separator ","
        //    plot 'tsne-standard-coords.csv' using 1:2:3 with labels font "Times,8"
        //!!! Possible error: plot was recently deprecated. Might need to re-do the last line
        //
        // If you use nDims=3 in the call to tsne.plot above, you can use the following gnuplot commands to
        // generate a 3d visualization of the word vectors:
        //    set datafile separator ","
        //    splot 'tsne-standard-coords.csv' using 1:2:3:4 with labels font "Times,8"

	}

	@Override
	public StandardTrainingListener getTrainingListener() {
		return listener;
	}

	/**
	 * Set the training listener
	 * @param listener - the training listener. 
	 */
	public void setTrainingListener(StandardTrainingListener listener) {
		this.listener = listener;
	}


	@Override
	public SettingsPane getSettingsPane() {
		// TODO Auto-generated method stub
		return null;
	}


	public double getMaxIterations() {
		return 100;
	}

}
