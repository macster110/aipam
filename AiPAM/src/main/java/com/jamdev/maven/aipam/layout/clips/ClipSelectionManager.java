package com.jamdev.maven.aipam.layout.clips;

import java.util.ArrayList;

import com.jamdev.maven.aipam.layout.AIPamView;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Manages which clips are selected by the user
 * <p>
 * Also handles the audio playback as this occurs when a clip is selected. 
 * 
 * @author Jamie macaulay 
 *
 */
public class ClipSelectionManager {

	/**
	 * The border used to select the clip 
	 */
	private  Border border = new Border(new BorderStroke(Color.LIMEGREEN, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(3)));

	/**
	 * The AIPamView
	 */
	private AIPamView aiPamView;

	/**
	 * List of the currently selected clips. 
	 */
	public ArrayList<PamClipPane> selectedClips = new ArrayList<PamClipPane>(); 



	/**
	 * Constructor for the clip selection manager.
	 */
	public ClipSelectionManager(AIPamView aiPamView2) {
		this.aiPamView = aiPamView2; 
	}

	/**
	 * Called whenever a clip is selected. 
	 * @param pamClipPane - the currently selected PamClipPane. 
	 */
	public void selectClip(PamClipPane pamClipPane) {
		clearSelectedClips(); 
		pamClipPane.setBorder(border);
		pamClipPane.getAudioPlay().playClipAudio();
		selectedClips.add(pamClipPane); 
	}

	/**
	 * Called whenever a clip is selected with a multi clip behaviour e.g. dragging or control button down. 
	 * @param pamClipPane - the currently selected PamClipPane. 
	 */
	public void selectMultiClip(PamClipPane pamClipPane) {
		//need to stop playing audio.
		for (PamClipPane pamClip: selectedClips) {
			if (pamClip!=pamClipPane) pamClip.getAudioPlay().stopClipAudio();
		}
		
		if (!selectedClips.contains(pamClipPane)) {
			pamClipPane.setBorder(border);
			pamClipPane.getAudioPlay().playClipAudio();
			selectedClips.add(pamClipPane); 
		}
	}
	
	/**
	 * Clear all currently selected clips. 
	 */
	private void clearSelectedClips() {
		for (PamClipPane pamClipPane: selectedClips) {
			pamClipPane.setBorder(null);
			pamClipPane.getAudioPlay().stopClipAudio();
		}
		selectedClips.clear();
	}


}
