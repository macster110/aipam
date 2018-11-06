package com.jamdev.maven.aipam.annotation;

import java.util.List;

import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;
import com.jmatio.types.MLStructure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Simple annotation which simply allows group together of clips under a default 
 * name. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class SimpleAnnotation implements Annotation {
	
	/**
	 * The annotation name 
	 */
    private final SimpleStringProperty annotationName;
    
	/**
	 * The annotation name 
	 */
   // private final ColorProperty colorProperty;
    
    /**
     * The number of clips. 
     */
    private final IntegerProperty numPamClips; 
    
    /**
     * The unique ID for the annotation
     */
	private int uniqueID;
	
	/**
	 * The circle symbol
	 */
	private Circle symbol;

	/**
	 * Hash string of the colour. 
	 */
	public SimpleStringProperty colorProperty; 
    
    /**
     * Create a simple annotation
     * @param annotationN - the annotation number
     */
    public SimpleAnnotation(int uniqueID) {
    	this.uniqueID=uniqueID;
    	annotationName= new SimpleStringProperty("Group: " + uniqueID); 
    	colorProperty= new SimpleStringProperty(UtilsFX.toRGBCode(Color.color(Math.random(), Math.random(), Math.random()))); 
    	
    	numPamClips = new SimpleIntegerProperty(); 
    	symbol= new Circle();
    	symbol.setFill(Color.web(colorProperty.get()));
    }

	@Override
	public List<PAMClip> getPamClips() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String clipAnnotaitonString(PAMClip pamClip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAnnotationGroupName() {
		return annotationName.get();
	}

	@Override
	public Node getAnnotationGroupSymbol() {
		return null;
	}
	
	/**
	 * The number of clips property. 
	 * @return the number of clips property. 
	 */
	public IntegerProperty numClipsProperty() {
		return this.numPamClips; 
	}

	@Override
	public int uniqueID() {
		return uniqueID;
	}

	/**
	 * Colour property. 
	 * @return the colour property. 
	 */
	public StringProperty colorProperty() {
		return colorProperty;
	}

	/**
	 * The annotation name property
	 * @return the annotation name property
	 */
	public StringProperty nameProperty() {
		return this.annotationName;
	}

	@Override
	public String getAnnotaionType() {
		return "simple_annotation";
	}

	@Override
	public MLStructure annotation2Struct() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void struct2Annotion(MLStructure mlstruct) {
		// TODO Auto-generated method stub
		
	}

}
