package com.jamdev.maven.aipam.annotation;

import java.util.List;

import com.jamdev.maven.aipam.clips.PAMClip;
import com.jmatio.types.MLStructure;

import javafx.scene.Node;

/**
 * An annotation for a clip. 
 * @author Jamie Macaulay 
 *
 */
public interface Annotation {
	

	
	/**
	 * Get a list of clips associated with this annotation. 
	 * @return
	 */
	public List<PAMClip> getPamClips(); 
	

	/**
	 * Get the annotaion for a clip. This is exported as a list beside each clip. 
	 * @param pamClip - the pamclip to get the data for.  
	 * @return the clip annotation data e.g. basic measurements, the clip name etc. 
	 */
	public String clipAnnotaitonString(PAMClip pamClip);
	
	/**
	 * Get the name of the annotation group. This is used in menus etc.
	 */
	public String getAnnotationGroupName(); 
	
	/**
	 * Get the symbol of the annotaton group. Used in menus etc. 
	 */
	public Node getAnnotationGroupSymbol(); 
	
	/**
	 * A unique ID for the annotation group. This cannot be the same as any other annotation groups. 
	 * @return the unique ID for a group. 
	 */
	public int uniqueID();
	
	
	/**
	 * Get the annotation type. This is used to identify the annotation for 
	 * import and export. 
	 * @return the annotation type. 
	 */
	public String getAnnotaionType(); 
	
	/**
	 * Convert the annotation to a structure. 
	 * @return the annotation structure. 
	 */
	public MLStructure annotation2Struct(); 
	
	/**
	 * Convert a MLStruct to an annotation i.e. takes the data from the MLStructure and 
	 * fills out this annotation with it 
	 * @param mlstruct - the ml structure containing annotation data
	 */
	public void struct2Annotion(MLStructure mlstruct); 
}
