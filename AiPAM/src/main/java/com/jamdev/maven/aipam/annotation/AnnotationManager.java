package com.jamdev.maven.aipam.annotation;

import java.util.List;

import com.jamdev.maven.aipam.AiPamController;
import com.jmatio.types.MLStructure;

/**
 * The annotation manager manages annotation tasks 
 * 
 * @author Jamie Macaulay 
 *
 */
public class AnnotationManager {
	
	/**
	 * Reference to the controller. 
	 */
	private AiPamController aiPamControl;
	
	public AnnotationManager(AiPamController aiPamControl) {
		this.aiPamControl=aiPamControl; 
	}
	
	
	/**
	 * Converts a list of MATLAB structures to annotations. 
	 * @param mlStrcut - the MATLAB structure to convert
	 * @return a list of annotation from the MATLAB structure. 
	 */
	public List<Annotation> struct2Annotations(MLStructure mlStrcut){
		
		return null; 
	} 
	
	
	
	/**
	 * Converts a list of annotations to a multi element MATLAB structure
	 * @param annotations - a lsit of annotations to convert to structures. 
	 * @return a list structure containing a list of annotation structures
	 */
	public MLStructure annotation2Struct(List<Annotation> annotations){
		
		return null; 
	} 
	
	/**
	 * Re link annotations with the current set of clips. 
	 * @return true if re linking was successful, otherwise false. 
	 */
	public boolean reLinkAnnotations() {
		//TODO
		return false; 
	}

}
