package com.jamdev.maven.aipam.clustering;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.primitives.Pair;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.plot.BarnesHutTsne;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.buffer.util.DataTypeUtil;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	
	public TSNEClipClusterer() {
		
	}
	
	/**
	 * Cluster the fingerprints created from audio data using a TSNE algorithm. 
	 * @param fingerprints
	 */
	public void clusterFingerprints(ArrayList<byte[]> fingerprints) {
	
//		 //create an n-dimensional array of doubles
//        Nd4j.setDataType(DataBuffer.Type.DOUBLE);
//        List<byte[]> cacheList = new ArrayList<>(); //cacheList is a dynamic array of strings used to hold all words
//
//
//		
//        BarnesHutTsne tsne = new BarnesHutTsne.Builder()
//                .setMaxIter(iterations).theta(0.5)
//                .normalize(false)
//                .learningRate(500)
//                .useAdaGrad(false)
////                .usePca(false)
//                .build();
//
//      //STEP 4: establish the tsne values and save them to a file
//        String outputFile = "target/archive-tmp/tsne-standard-coords.csv";
//        (new File(outputFile)).getParentFile().mkdirs();
//
//        tsne.fit(weights);
//        tsne.saveAsFile(cacheList, outputFile);
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
	public void cluster(ArrayList<PAMClip> pamClips) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SettingsPane getSettingsPane() {
		// TODO Auto-generated method stub
		return null;
	}

}
