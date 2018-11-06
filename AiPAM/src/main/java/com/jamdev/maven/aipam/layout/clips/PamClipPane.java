package com.jamdev.maven.aipam.layout.clips;

import java.io.File;

import com.jamdev.maven.aipam.clips.AudioPlay;
import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.layout.ColourArray;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

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
	 * Constructor for the clip pane. 
	 * @param clip
	 */
	public PamClipPane(PAMClip clip, int width, int height, ColourArray colourArray, double clims[]) {
		this.clip=clip; 
		//create the pane 
		this.getChildren().add(imageCanvas = new Canvas(width, height)); 
		addMouseBeahviour(); //add the default mouse behaviours. 
		generateSpecImage(colourArray, clims); 
		//need the border so that the tile pane does not change size when a highlight border is first set
		this.setStyle("-fx-border-color: transparent; -fx-border-width: 2px;");
		Tooltip tooltip = new Tooltip(new File(clip.fileName).getName());
		Tooltip.install(this, tooltip);
		
	}
	
	/**
	 * Generate the spectrogram image. 
	 */
	public void generateSpecImage(ColourArray colourArray, double[] clims) {
		this.spectrogramImage = new SpectrogramImage(clip.getSpectrogram(), colourArray, clims); 
		//draw the image onto the canvas. 
//		imageCanvas.getGraphicsContext2D().drawImage(spectrogramImage.getWritableImage(), 0, 0);
		Image specImage = spectrogramImage.getSpecImage((int) imageCanvas.getWidth(), (int) imageCanvas.getWidth()); 
		imageCanvas.getGraphicsContext2D().drawImage(specImage, 0, 0, imageCanvas.getWidth(), imageCanvas.getHeight(), 
				0,0, specImage.getWidth(), specImage.getHeight());
//		imageCanvas.getGraphicsContext2D().fillOval(2, 2, 10, 10); 
	}
	/**
	 * Add mouse functionality. 
	 */
	private void addMouseBeahviour() {
		
		this.setOnMouseClicked((event)->{
			if (event.isControlDown()) clipSelectionManager.selectMultiClip(PamClipPane.this); 
			else clipSelectionManager.selectClip(PamClipPane.this); 
		});		
		
//		this.setOnMouseDragOver((event)->{
//			clipSelectionManager.selectMultiClip(PamClipPane.this); 
//		});
		
		this.setOnMouseEntered((event)->{
			//System.out.println("Mouse enterred: " + event);
			if (event.isPrimaryButtonDown()) {
				clipSelectionManager.selectMultiClip(PamClipPane.this); 
			}
		});
		
	}

	/**
	 * Set the clip selection manager. This manages single and multi-clip selections. Mouse
	 * behaviours of clips need to be passed to the selection manager so it can handle 
	 * appropriate multi clip responses. 
	 * @param clipSelectionManager - the clip selection manager.
	 */
	public void setSelectionManager(ClipSelectionManager clipSelectionManager) {
		this.clipSelectionManager = clipSelectionManager; 
		
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
	private PAMClip getPamClip() {
		return clip;
	}

}
