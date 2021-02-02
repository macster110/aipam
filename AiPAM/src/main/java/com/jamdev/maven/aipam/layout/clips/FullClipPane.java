package com.jamdev.maven.aipam.layout.clips;

import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.clips.SpecParams;
import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.ColourArray;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * Pane which shows the full spectrogram of a clip and has some
 * additional controls. 
 * @author Jamie Macaulyay 
 *
 */
public class FullClipPane extends BorderPane {
	
	ColourArray colourArray = ColourArray.createFireArray(200); 
	
	double[] colourLims = new double[] {20, 100};  
	
	SpecParams specParams = new SpecParams(); 
	
	/**
	 * Full clip pane shows a full szied clip of the spectrogram data.
	 * @param pamClip - a pam clip 
	 */
	public FullClipPane() {
		
	}
	
	/**
	 * Set the clip to view. 
	 * @param pamClip
	 */
	public void setPamClip(PAMClip pamClip) {
		SpectrogramImage image = new SpectrogramImage(pamClip.getSpectrogram(specParams.fftLength, specParams.fftHop).getAbsoluteSpectrogram(), 
				colourArray, colourLims); 
		
		ImageView imageView = new ImageView(image.getSpecImage(300, 300));
		
		NumberAxis yAxis = createFreqAxis(Side.LEFT, pamClip); 

		
		this.setTop(new Label(pamClip.getFileName()));
		BorderPane.setAlignment(imageView, Pos.CENTER_LEFT);
		this.setCenter(imageView);
		this.setLeft(yAxis);
	}
	
	/**
	 * Create the frequency axis for a PAMClip.
	 * @param side - the side for the axis.
	 * @param pamClip - the clip ot create the axis for 
	 * @return the number axis for frequency. 
	 */
	public static NumberAxis createFreqAxis(Side side, PAMClip pamClip) {
		NumberAxis yAxis = createAxis(side); 
		
		double upperBound;
		double lowerBound; 
		if (pamClip.getFreqLims()[1]>=1000) {
			yAxis.setLabel("Frequency (kHz)");
			lowerBound= pamClip.getFreqLims()[0]/1000.;
			upperBound = pamClip.getFreqLims()[1]/1000.;
		}
		else {
			yAxis.setLabel("Frequency (Hz)");
			lowerBound= pamClip.getFreqLims()[0];
			upperBound = pamClip.getFreqLims()[1];
		} 
		
		yAxis.setLowerBound(lowerBound);
		yAxis.setUpperBound(upperBound);
		yAxis.setTickUnit((upperBound-lowerBound)/10);
		
		return yAxis; 
	}
	
	/**
	 * Create a number axis to display in the pane. 
	 * @return the number axis. 
	 */
	private static NumberAxis createAxis(Side side){
		
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
