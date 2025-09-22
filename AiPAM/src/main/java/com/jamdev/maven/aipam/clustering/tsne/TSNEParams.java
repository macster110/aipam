package com.jamdev.maven.aipam.clustering.tsne;

/**
 * Stub class for TSNEParams to allow compilation without full T-SNE implementation
 */
public class TSNEParams {
    public int initialDim = 50;
    public double perplexity = 20.0;
    public int maxIterations = 1000;
    public double theta = 0.5;
    public boolean usePCA = true;
    
    public TSNEParams() {
        // Default constructor
    }
}