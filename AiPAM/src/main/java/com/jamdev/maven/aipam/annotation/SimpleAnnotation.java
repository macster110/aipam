package com.jamdev.maven.aipam.annotation;

import java.util.List;

import com.jamdev.maven.aipam.SettingsImportExport;
import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLInt32;
import com.jmatio.types.MLStructure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
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
	 * List of pamClips which belong to this annotation
	 */
	private ObservableList<PAMClip> pamClips; 
	
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
    private final SimpleIntegerProperty numPamClips; 
    
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
    	colorProperty.addListener((obsValue, oldValue, newValue)->{
        	symbol.setFill(Color.web(colorProperty.get()));
    	});
    	
    	numPamClips = new SimpleIntegerProperty(); 
    	pamClips= FXCollections.observableArrayList(); 
    	pamClips.addListener((Change<? extends PAMClip> c) -> {
    		numPamClips.set(pamClips.size());
    	});
    	
    	symbol= new Circle(10);
    	symbol.setFill(Color.web(colorProperty.get()));
    }

	@Override
	public List<PAMClip> getPamClips() {
		return pamClips;
	}

	@Override
	public String clipAnnotaitonString(PAMClip pamClip) {
		return ""; // no extra information for pamclips 
	}

	@Override
	public String getAnnotationGroupName() {
		return annotationName.get();
	}

	@Override
	public Node getAnnotationGroupSymbol() {
		return symbol;
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
		
		MLStructure annotationStruct = new MLStructure("simpleannotation", new int[] {1,1});
		
		MLInt32 uniqueId= SettingsImportExport.mlInt(this.uniqueID);
		MLChar name= new MLChar(null, getAnnotationGroupName()); 
		
		annotationStruct.setField("uniqueid", uniqueId);
		annotationStruct.setField("name", name);
		
		MLStructure clips = new MLStructure("clips", new int[] { getPamClips().size(),1});
		for (int i=0; i< getPamClips().size(); i++) {
			clips.setField("clipID", new MLChar(null, getPamClips().get(i).getID()), i);
			clips.setField("clipgridID", SettingsImportExport.mlInt(getPamClips().get(i).getGridID()), i); 
		}
		
		annotationStruct.setField("clips", clips);

		return annotationStruct;
	}

	@Override
	public void struct2Annotation(MLStructure mlstruct) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Add a clip to the annotation group. 
	 * @param clip
	 */
	public void addClip(PAMClip clip) {
		this.pamClips.add(clip); 	
		clip.setAnnotation(this); //se the clips parent annotation. 
	}

}
