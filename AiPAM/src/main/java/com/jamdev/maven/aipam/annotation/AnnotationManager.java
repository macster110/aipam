package com.jamdev.maven.aipam.annotation;

import java.util.List;

import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;
import com.jmatio.types.MLStructure;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

/**
 * The annotation manager manages annotation tasks.
 * <p>
 * Currently the Annotation Manager expects SimpleAnnotations or 
 *	subclasses of. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class AnnotationManager {
	
	/**
	 * List of the current simple annotations. 
	 */
	private final ObservableList<SimpleAnnotation> data =
			FXCollections.observableArrayList(
					new SimpleAnnotation(1, UtilsFX.toRGBCode(Color.ORANGE)));
	
	/**
	 * Reference to the controller. 
	 */
	private AiPamController aiPamControl;
	
	public AnnotationManager(AiPamController aiPamControl) {
		this.aiPamControl=aiPamControl; 
	}
	
	/**
	 * Instatiate the correct annotation class for an annotation name. 
	 * <p>
	 * This must be updated if new annotation types are added. 
	 * @param name - the annotation name,
	 * @return a new instance of the annotation class corresponding to the name. 
	 */
	private SimpleAnnotation getAnnotation(String name) {
		SimpleAnnotation annotation = null; 
		switch (name) {
		case "simpleannotation": 
			annotation = new SimpleAnnotation(0); 
			break; 
		}
		
		return annotation; 
	}
	
	/**
	 * Converts a list of MATLAB structures to annotations. 
	 * @param mlStrcut - the MATLAB structure to convert
	 * @return a list of annotation from the MATLAB structure. 
	 */
	public List<Annotation> struct2Annotations(MLStructure mlStrcut){
				
//		MLStructure struct;
//		for (int i=0; i<struct.getM(); i++) {
//		
//						
//		}
		
		//TODO
//				
		return null; 
	} 
	
	
	
	/**
	 * Converts a list of annotations to a multi element MATLAB structure
	 * @param data2 - a lsit of annotations to convert to structures. 
	 * @return a list structure containing a list of annotation structures
	 */
	public MLStructure annotation2Struct(ObservableList<SimpleAnnotation> data2){
		
		MLStructure mlStructure = new MLStructure("annotations", new int[] {data2.size(),1}); 
		
		MLStructure struct;
		for (int i=0; i<data2.size(); i++) {
			mlStructure.setField(data2.get(i).getAnnotaionType(), data2.get(i).annotation2Struct(), i);
		}
		
		return mlStructure; 
	} 
	
	/**
	 * Re link annotations with the current set of clips. If annotations are imported 
	 * they have the clip ID but they don;t have the clip object associated with them. 
	 * This function finds the clip from the clip object list. If a clip with the right ID
	 * is not found then that clip is deleted from the annotation. 
	 * 
	 * @return true if re linking was completely successful (all clips found), otherwise false. 
	 */
	public boolean reLinkAnnotations() {
		//TODO
		return false; 
	}


	/**
	 * Get the list of simple annotations
	 * @return list of simple annotations
	 */
	public ObservableList<SimpleAnnotation> getAnnotationsList() {
		return data;
	}
	
	/**
	 * Add an annotation to the list. 
	 * @param annotation - annotaion to add
	 */
	public void add(SimpleAnnotation annotation) {
		 data.add(annotation); 
	}


	/**
	 * Get the current number of annotations
	 * @return the current number of annotations
	 */
	public int getNAnnotations() {
		return data.size();
	}

	public MLStructure annotation2Struct() {
		return annotation2Struct(this.data);
	}

}
