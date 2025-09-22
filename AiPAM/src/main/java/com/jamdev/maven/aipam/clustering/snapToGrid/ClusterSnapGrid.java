package com.jamdev.maven.aipam.clustering.snapToGrid;

/**
 * Stub class for ClusterSnapGrid to allow compilation
 */
public class ClusterSnapGrid {
    public static final int CLUSTERTOOLGAUSSIANNEG = 1;
    public static final int CLUSTERTOOLDENSITYBASED = 2;
    
    public ClusterSnapGrid() {
        // Default constructor
    }
    
    public static int calcGridSize(int numItems) {
        return (int) Math.ceil(Math.sqrt(numItems));
    }
}