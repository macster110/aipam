package com.jamdev.maven.aipam.layout.clips;

import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.ColourArray;

import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * Pane which shows the full spectorgram of a clip. 
 * @author Jamie Macaulyay 
 *
 */
public class FullClipPane extends BorderPane {
	
	ColourArray colourArray = ColourArray.createFireArray(200); 
	
	double[] colourLims = new double[] {120,160};
	
	/**
	 * Full clip pane 
	 * @param pamClip - a pam clip 
	 */
	public FullClipPane(PAMClip pamClip) {
		
		SpectrogramImage image = new SpectrogramImage(pamClip.getSpectrogram(), 
				colourArray, colourLims); 
		
		ImageView imageView = new ImageView(image.getSpecImage(800, 400));
		
		NumberAxis yAxis = new NumberAxis();
		//yAxis.setTickUnit(tickUnit);
		yAxis.setLabel("Frequency");
		
		this.setTop(new Label(pamClip.getFileName()));
		this.setCenter(imageView);
	}
	
	/**
	 * Create a number axis to display in the pane. 
	 * @return the number axis. 
	 */
	private NumberAxis createAxis(Side side){
		
		final NumberAxis axis = new NumberAxis(); 

		//set the axis side
		axis.setSide(side);
		//disable aurto ranging 
		axis.setAutoRanging(false);


		//don;t want silly animations. 
		axis.setAnimated(false);
		
		return axis;
	}

}
