package com.jamdev.maven.aipam.featureExtraction;

import com.jamdev.maven.aipam.clustering.Params;
import com.jmatio.types.MLStructure;

public class FeatureParams implements Params {
	
		/**
	 * The currently selected index of the feature extractor. 
	 */
	public int currentFeatureIndex = 0 ; 
	
	/**
	 * Holds the parameter objects for each feature extraction method. 
	 */
	public Object[] featureParams; 


	@Override
	public void struct2Params(MLStructure mlStruct) {
		// TODO Auto-generated method stub
	}

	@Override
	public MLStructure params2Struct() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean compare(Params featureParams) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public Params clone() {
		try {
			return (Params) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null; 
		}
	}
	

}
