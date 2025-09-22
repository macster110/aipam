package com.jamdev.maven.aipam.clustering.tsne;


//import org.nd4j.linalg.factory.Nd4j;
//import org.nd4j.linalg.primitives.Pair;
//import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
//import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
//import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
//import org.deeplearning4j.plot.BarnesHutTsne;
//import org.nd4j.linalg.api.buffer.DataBuffer;
//import org.nd4j.linalg.api.ndarray.INDArray;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.jamdev.maven.aipam.clustering.StandardTrainingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by agibsonccc on 9/20/14.
 *
 * Dimensionality reduction for high-dimension datasets
 */
public class TSNEStandardExample {
	
//    private static Logger log = LoggerFactory.getLogger(TSNEStandardExample.class);
//
//
//    public static void main(String[] args) throws Exception  {
//        //STEP 1: Initialization
//        int iterations = 100;
//        //create an n-dimensional array of doubles
//        Nd4j.setDataType(DataBuffer.Type.DOUBLE);
//        List<String> cacheList = new ArrayList<>(); //cacheList is a dynamic array of strings used to hold all words
//
//        //STEP 2: Turn text input into a list of words
//        log.info("Load & Vectorize data....");
//        File wordFile = new File("C:\\Users\\macst\\Desktop\\words.txt"); 
//        //Get the data of all unique word vectors
//        Pair<InMemoryLookupTable,VocabCache> vectors = WordVectorSerializer.loadTxt(wordFile);
//
//        
//        VocabCache cache = vectors.getSecond();
//        INDArray weights = vectors.getFirst().getSyn0();    //seperate weights of unique words into their own lis
//        
//
//        for(int i = 0; i < cache.numWords(); i++)   //seperate strings of words into their own list
//            cacheList.add(cache.wordAtIndex(i));
//
//        //STEP 3: build a dual-tree tsne to use later
//        log.info("Build model....");
//        BarnesHutTsne tsne = new BarnesHutTsne.Builder()
//                .setMaxIter(iterations).theta(0.5)
//                .normalize(true)
//                .learningRate(500)
//                .useAdaGrad(false)
//                .numDimension(2)
////                .usePca(false)
//                .build();
//
//        //STEP 4: establish the tsne values and save them to a file
//        log.info("Store TSNE Coordinates for Plotting....");
//        String outputFile = "C:\\Users\\macst\\Desktop\\tsne-standard-coords.csv";
//        (new File(outputFile)).getParentFile().mkdirs();
//
//        //add a listener for updates on progress 
//        StandardTrainingListener list = new StandardTrainingListener();
//        tsne.setTrainingListener(list);
//        long time0 = System.currentTimeMillis();
//        System.out.println("Begin training: ");
//        
//        //run the actual algortihm
//        tsne.fit(weights);
//        
//        //save the clusterred points to a file. 
//        tsne.saveAsFile(cacheList, outputFile);
//        
//        long time1 = System.currentTimeMillis();
//        System.out.println("End training: " + (time1-time0) + " millis");
//      
//        
//        //This tsne will use the weights of the vectors as its matrix, have two dimensions, use the words strings as
//        //labels, and be written to the outputFile created on the previous line
//        // Plot Data with gnuplot
//        //    set datafile separator ","
//        //    plot 'tsne-standard-coords.csv' using 1:2:3 with labels font "Times,8"
//        //!!! Possible error: plot was recently deprecated. Might need to re-do the last line
//        //
//        // If you use nDims=3 in the call to tsne.plot above, you can use the following gnuplot commands to
//        // generate a 3d visualization of the word vectors:
//        //    set datafile separator ","
//        //    splot 'tsne-standard-coords.csv' using 1:2:3:4 with labels font "Times,8"
//    }



}