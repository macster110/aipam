package com.jamdev.maven.aipam.layout.clips;


import com.jamdev.maven.aipam.layout.ColourArray;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Draws the image of a spectrogram on a canvas. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class SpectrogramImage {
	
	/**
	 * The spectrogram. 
	 */
	private double[][] spectrogram;
	
	/**
	 * The colour array type
	 */
	private ColourArray colourArray;
	
	/**
	 * The colour limits 
	 */
	private double[] clims;
	
	/**
	 * Constructor for a spectogram image. 
	 * @param spectrogram - the spectrogram to make an image of 
	 * @param colourArrayType - the colour map to use for the spectrogram 
	 * @param clims - the colour limits for the spectrogram.
	 */
	public SpectrogramImage(double[][] spectrogram, ColourArray colourArray, double[] clims) {
		this.spectrogram = spectrogram;
		this.colourArray=colourArray;
		this.clims=clims; 
	}
	
	/**
	 * Create a an image of the spectrogram by applying a colour gradient to the surface data.
	 * @param spectrogram - the spectrogram to draw
	 * @param colourArray - the colour array to use for the colour gradient. 
	 * @param clims - the colour limits to apply. 
	 */
	public WritableImage writeImageData(double[][] data, ColourArray colourArray, double[] clims) {
		
//		double[][] data = spectrogram.getAbsoluteSpectrogramData(); 
		WritableImage specImage = new WritableImage(data.length, data[0].length); 
		
		PixelWriter writer = specImage.getPixelWriter(); 

		for (int i=0; i<data.length; i++) {
			for (int j=0; j<data[0].length; j++) {
				writer.setColor(i, data[0].length-1-j, calcColour(20*Math.log10(data[i][j])));
			}
		}
		
		return specImage; 
	}
	
	public Image scale(Image source, int targetWidth, int targetHeight, boolean preserveRatio) {
	    ImageView imageView = new ImageView(source);
	    imageView.setPreserveRatio(preserveRatio);
	    imageView.setFitWidth(targetWidth);
	    imageView.setFitHeight(targetHeight);
	    return imageView.snapshot(null, null);
	}
	
	/**
	 * Calculate the colour which corresponds to a point on the spectrogram. 
	 * @param d - the spectrogram surface point to calculate the colour for. 
	 * @return the colour of the spectrogram at d. 
	 */
	private Color calcColour(double d) {
		double colPerc = (d-clims[0])/(clims[1]-clims[0]); 
		//System.out.println("ColPerc: " + colPerc);
		return colourArray.getColour(colPerc);
	}


	/**
	 * Get the writable image 
	 * @return
	 */
	public Image getSpecImage(int width, int height) {
		return scale(writeImageData(spectrogram, colourArray, clims) , width, height, false);
	}

}
