package com.jamdev.maven.aipam.clustering;

import java.util.ArrayList;
import java.util.List;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.util.ArrayUtil;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.deeplearning4j.plot.BarnesHutTsne;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;
import com.jamdev.maven.aipam.utils.DownSampleImpl;
import com.jamdev.maven.aipam.utils.SettingsPane;
import com.jamdev.maven.clips.PAMClip;

/**
 * Algorithm which clusters clips based on TSNE high dimesnionla clusterring algortihm. 
 * 
 * 
 * @author Jamie Macaulay
 *
 */
public class TSNEClipClusterer implements ClusteringAlgorithm{
	
	private int iterations = 500; 
	
    private TrainingListener listener; 
    
    
	public TSNEClipClusterer() {
		
	}
	

	@Override
	public void cluster(ArrayList<PAMClip> pamClips) {
		try {
		clusterFingerprints(pamClips); 
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
	public void clusterFingerprints(ArrayList<PAMClip> pamClips) {
	

        //create an n-dimensional array of doubles
        Nd4j.setDataType(DataBuffer.Type.DOUBLE);
        
        
        List<String> cacheList = new ArrayList<>(); //cacheList is a dynamic array of ID's used to hold all words
        
        System.out.println("Creating the   INDArray: ");

        INDArray weights = createDataArray(pamClips);    //seperate weights of unique words into their own list
        

        if (weights==null) {
        	System.err.println("The weights is null: " + weights);
        	return; 
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
                .setMaxIter(iterations).theta(0.5)
                .normalize(false)
                .learningRate(500)
                .useAdaGrad(false)
                .numDimension(2)
//                .usePca(false)
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
        
       System.out.println("Rows is " + tsnResult.rows());
       INDArray rows; 
        for (int i=0; i<tsnResult.rows(); i++) {
        	rows = tsnResult.getRow(i); 
        	double[] clusterPoint = rows.toDoubleVector(); 
        	System.out.print(i + ":  ");  
        	for (int j=0; j<clusterPoint.length; j++) {
        		System.out.print("  " +clusterPoint[j]);
        	}
        	System.out.println(); 
        }
        
        long time1 = System.currentTimeMillis();
        System.out.println("End training: " + (time1-time0) + " millis");
      
        
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


	public TrainingListener getTrainingListener() {
		return listener;
	}

	/**
	 * Set the training listener
	 * @param listener - the training listener. 
	 */
	@Override
	public void setTrainingListener(TrainingListener listener) {
		this.listener = listener;
	}


	@Override
	public SettingsPane getSettingsPane() {
		// TODO Auto-generated method stub
		return null;
	}

}
