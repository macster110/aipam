package com.jamdev.maven.aipam.layout.clips;

import java.io.File;

import com.jamdev.maven.aipam.annotation.SimpleAnnotation;
import com.jamdev.maven.aipam.clips.AudioPlay;
import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.clips.SpecParams;
import com.jamdev.maven.aipam.featureExtraction.FeatureExtraction;
import com.jamdev.maven.aipam.layout.ColourArray;
import com.jamdev.maven.aipam.layout.featureExtraction.FeaturePane;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Pane which holds a single spectrogram clip This pane also handles interactions with 
 * the clip such as playing sound, highlighting if selected etc. 
 * 
 * @author Jamie Macaulay
 *
 */
public class PamClipPane extends StackPane implements Comparable<PamClipPane> {

	/**
	 * The pmaClip; 
	 */
	private PAMClip clip;

	/**
	 * Canvas which shows the image of a spectrogram
	 */
	private Canvas imageCanvas;

	/**
	 * The spectrogram image.
	 */
	private SpectrogramImage spectrogramImage;

	/**
	 * Reference to the clip selection manager. 
	 */
	private ClipSelectionManager clipSelectionManager;

	/**
	 * Hex colour property for pane. 
	 */
	private StringProperty colourProperty = new SimpleStringProperty(UtilsFX.toRGBCode(Color.TRANSPARENT)); 

	/**
	 * Pane which overlays the image. This can be sued to add highlights. 
	 */
	private Pane overlayPane;

	/**
	 * The default opacity value to use for overlays 
	 */
	private double defaultOpacity = 0.3;

	/**
	 * Reference to the feature extraction mmethod
	 */
	private FeatureExtraction featureExtraction; 
	
	// Listeners that must be removed to prevent memory leaks
	private ChangeListener<Object> annotationListener; // Listener for annotation property changes
	private ChangeListener<String> colourListener;     // Listener for colour property changes

	/**
	 * Constructor for the clip pane. 
	 * @param clip
	 */
	public PamClipPane(PAMClip clip, int width, int height, SpecParams params, ColourArray colourArray, FeatureExtraction featureExtraction) {
		this.clip=clip; 
		this.featureExtraction  = featureExtraction; 
		//create the pane 
		this.getChildren().add(imageCanvas = new Canvas(width, height)); 
		imageCanvas.getGraphicsContext2D().setImageSmoothing(false);

		//clip features
		generateSpecImage(params.fftLength, params.fftHop, colourArray, params.colourLims); 

		//add an overlay pane so that the clip can be coloured.
		this.getChildren().add(overlayPane = new Pane()); 
		overlayPane.setMouseTransparent(true);

		//need the border so that the tile pane does not change size when a highlight border is first set
		this.setStyle("-fx-border-color: transparent; -fx-border-width: 3px;");

		//set the inital overlay colour. 
		if (clip.annotationProperty().get()!=null) {
			colourProperty.setValue(((SimpleAnnotation) clip.annotationProperty().get()).colorProperty.get());
		}
		// Store and add annotation listener
		// This listener binds the colour property to the annotation's colour property when the annotation changes.
		// If not removed, it will keep a reference to this PamClipPane and cause a memory leak.
		annotationListener = (obsval, oldVal, newVal) -> {
			colourProperty.unbind();
			if (newVal!=null) {
				colourProperty.bind(((SimpleAnnotation) newVal).colorProperty());
			}
			else colourProperty.set(UtilsFX.toRGBCode(Color.TRANSPARENT)); 
		};
		clip.annotationProperty().addListener(annotationListener);
		// Store and add colour property listener
		// This listener updates the overlay colour when the colour property changes.
		// If not removed, it will keep a reference to this PamClipPane and cause a memory leak.
		colourListener = (obsval, oldVal, newVal) -> {
			setOverlayColour(); 
		};
		colourProperty.addListener(colourListener);

		Tooltip tooltip = new Tooltip(
				new File(clip.getFileName()).getName() +"\n"
						+ "Time: " + clip.getDateTimeString() + "\n"
						+ "Cluster ID: " + clip.getGridID() + "\n"
						+ "Original ID: " +  clip.getGridID());
		Tooltip.install(this, tooltip);
		
		//TEMP
		imageCanvas.getGraphicsContext2D().fillText(String.valueOf(clip.getGridID()), 10, 10);

		setOverlayColour(); 
	}
	
	

	private void setOverlayColour() {
		//bit ugly but meh
		if (colourProperty.get().equals(UtilsFX.toRGBCode(Color.TRANSPARENT))) {
			//can;t have opacity ion fully tranparent or goes weird. 
			overlayPane.setStyle("-fx-background-color: transparent;");
		}
		else {
			Color color = Color.web(colourProperty.get(), defaultOpacity); 
			overlayPane.setStyle("-fx-background-color: " +
					"rgba("+255*color.getRed()+ ","  + 255*color.getGreen() +"," +255*color.getBlue()+"," + color.getOpacity()+");");
		}

	}

	/**
	 * Generate the spectrogram image. 
	 */
	public void generateSpecImage(int fftLength, int fftHop, ColourArray colourArray, double[] clims) {

		if (featureExtraction==null)
			this.spectrogramImage = new SpectrogramImage(clip.getSpectrogram(fftLength, fftHop).getAbsoluteSpectrogram(), colourArray, clims); 
		else {
			this.spectrogramImage = FeaturePane.getFeatureImage(clip.getSpectrogram(fftLength, fftHop), featureExtraction, clims, colourArray); 
		}
		//draw the image onto the canvas. 
		//		imageCanvas.getGraphicsContext2D().drawImage(spectrogramImage.getWritableImage(), 0, 0);
		Image specImage = spectrogramImage.getSpecImage((int) imageCanvas.getWidth(), (int) imageCanvas.getHeight()); 
		imageCanvas.getGraphicsContext2D().drawImage(specImage, 0, 0, imageCanvas.getWidth(), imageCanvas.getHeight(), 
				0,0, specImage.getWidth(), specImage.getHeight());
		//		imageCanvas.getGraphicsContext2D().fillOval(2, 2, 10, 10); 
		
		//
	}



	/**
	 * Set the clip selection manager. This manages single and multi-clip selections. Mouse
	 * behaviours of clips need to be passed to the selection manager so it can handle 
	 * appropriate multi clip responses. 
	 * @param clipSelectionManager - the clip selection manager.
	 */
	public void setSelectionManager(ClipSelectionManager clipSelectionManager) {
		this.clipSelectionManager = clipSelectionManager; 
		clipSelectionManager.addMouseBeahviour(this); //add the default mouse behaviours. 
	}

	/**
	 * Convenience function get the AudioPlay from the clip.This plays the clip. 
	 * @return the audio play from the clip. 
	 */
	public AudioPlay getAudioPlay() {
		return clip.getAudioPlay(); 
	}

	@Override
	public int compareTo(PamClipPane comparePane) {
		//when comparing clips want to sort by their grid ID. 
		return clip.getGridID() - comparePane.getPamClip().getGridID();
	}

	/**
	 * Get the pam clip the pane is hswoing.m 
	 * @return the pam clip. 
	 */
	public PAMClip getPamClip() {
		return clip;
	}
	
	public void freeMemory() {
		// Remove listeners to break reference cycles and prevent memory leaks
		if (annotationListener != null) {
			clip.annotationProperty().removeListener(annotationListener); // Remove annotation listener
			annotationListener = null;
		}
		if (colourListener != null) {
			colourProperty.removeListener(colourListener); // Remove colour property listener
			colourListener = null;
		}
		//this.clip=null;
		this.imageCanvas =null;
		this.spectrogramImage =  null;
		this.clipSelectionManager = null;
	}

}