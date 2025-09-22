package com.jamdev.maven.aipam.clustering;

import java.io.Serializable;

/**
 * Base class for parameters
 */
public class Params implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public Params() {
        // Default constructor
    }
    
    public Params clone() {
        return new Params();
    }
}