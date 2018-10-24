package com.jamdev.maven.aipam.layout.utilsFX;

import com.jamdev.maven.aipam.layout.ColourArray;
import com.jamdev.maven.aipam.layout.ColourArray.ColourArrayType;
import com.jamdev.maven.aipam.utils.AiPamUtils;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * The colour range slider shows a linear colour gradient between two thumbs
 * with the rest of the slider coloured by the min and max of the colour
 * gradient. The slider is generally used to allow users to change settings for
 * anything requiring a colour scale, e.g. a spectrogram.
 * 
 * @author Jamie Macaulay
 *
 */
public class ColourRangeSlider extends PamRangeSlider {

	/**
	 * The colour of the whole track i.e. the low limit colour. 
	 */
	private Color trackCol;

	/**
	 * The colour of the top bar. i.e. the high limit colour
	 */
	private Color topBarCol;

	/** 
	 * Additonal node for top bar/ 
	 */
	private Pane topBar;

	private double trackWidth=15;

	private ColourArrayType colourMap = ColourArrayType.RED;


	/**
	 * Create the colour range slider. 
	 */
	public ColourRangeSlider() {
		super();
	}
	
	public ColourRangeSlider(Orientation orientaiton) {
		super();
		this.setOrientation(orientaiton);
	}


	/**
	 * Create the top bar. 
	 */
	public void initTopBar(){
		topBar=new StackPane();
		//TODO-need to sort for horizontal 
		if (getOrientation()==Orientation.VERTICAL){
			topBar.layoutXProperty().bind(getTrack().layoutXProperty());
		}
		else {
			topBar.layoutYProperty().bind(getTrack().layoutYProperty());
		}
		//		topBar.setStyle("-fx-background-color: red;");
		getChildren().add(topBar);
	}

	/**
	 * Extract the tracks from CSS. 
	 */
	@Override
	protected void initColorTracks() {
		super.initColorTracks();

		initTopBar(); 
		getHighThumb().toFront(); //need to bring to front.
		getLowThumb().toFront(); //need to bring to front. 

		if (getOrientation()==Orientation.VERTICAL){
			getRangeBar().setPrefWidth(trackWidth);
			getTrack().setPrefWidth(trackWidth);
			getTopBar().setPrefWidth(trackWidth);
		}
		else{
			getRangeBar().setPrefHeight(trackWidth);
			getTrack().setPrefHeight(trackWidth);
			getTopBar().setPrefHeight(trackWidth);
		}

		setColourArrayType(colourMap); 
	}


	@Override
	public void layoutChildren() {
		super.layoutChildren();
		double rangeStart;
		//now resize the top bar. 
		if (getOrientation()==Orientation.VERTICAL){
			rangeStart=this.getHighThumb().getLayoutY()+getHighThumb().getHeight(); 
			topBar.layoutYProperty().setValue(0);
			topBar.resize(trackWidth, rangeStart+1);
		}
		else {
			rangeStart=this.getLowThumb().getLayoutX()+getLowThumb().getWidth(); 
			topBar.layoutXProperty().setValue(0);
			topBar.resize(rangeStart+1, trackWidth);
		}

	}


	protected Region getTopBar() {
		return topBar;
	}

	@Override
	public void setTrackColor(Color trackColour) {
		//FIXME- this is getting called somewhere and messing up the colours. 
	}
	
	
	/**
	 * Set the colour array type. 
	 * @param colourArrayType - the ColourArrayType to set. 
	 */
	public void setColourArrayType(ColourArrayType colourMap) {
		//set the colour gradient
		this.colourMap=colourMap; 
		Color[] colorList=ColourArray.getColorList(colourMap);
		trackCol=colorList[0]; 
		topBarCol=colorList[colorList.length-1]; 

		if (getTrack()!=null) {

			//set the solid colours for the track and top bar. 
			//		getTrack().setBackground(new Background(new BackgroundFill(trackCol, CornerRadii.EMPTY, Insets.EMPTY)));
			//28/03/2017 - had to change to css as adding to a scroll pane seemed ot override background. 
			getTrack().setStyle("-fx-background-color: " + AiPamUtils.color2Hex(trackCol));

			getTopBar().setBackground(new Background(new BackgroundFill(topBarCol, CornerRadii.EMPTY, Insets.EMPTY)));

			//set the colour gradient
			Stop[] stops =new Stop[colorList.length];
			for (int j=0; j<colorList.length; j++){
				stops[j]=new Stop((double) j/(colorList.length-1),colorList[j]);
			};

			LinearGradient linearGradient;
			if (getOrientation()==Orientation.VERTICAL) {
				linearGradient=new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stops);
			}
			else {
				linearGradient=new LinearGradient(1,0, 0, 0, true, CycleMethod.NO_CYCLE, stops);

			}
			getRangeBar().setBackground(new Background(new BackgroundFill(linearGradient, CornerRadii.EMPTY, Insets.EMPTY)));		
		}
	}


}