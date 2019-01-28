package com.jamdev.maven.aipam.layout.clips;

import java.util.ArrayList;

import com.jamdev.maven.aipam.layout.AIPamView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

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
	 * The AIPamView
	 */
	private AIPamView aiPamView;

	/**
	 * List of the currently selected clips. 
	 */
	public ArrayList<PamClipPane> selectedClips = new ArrayList<PamClipPane>(); 

	/**
	 * The selection menu for annotating clips. 
	 */
	private SelectionMenu selectionMenu; 



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
		repaintAllClips(); 
		pamClipPane.setStyle("-fx-border-color: ACCENT_COLOR; -fx-border-width: 3px;");
		pamClipPane.getAudioPlay().getVolumePropery().bind(aiPamView.volumeProperty());
		pamClipPane.getAudioPlay().playClipAudio();
		selectedClips.add(pamClipPane); 
	}

	/**
	 * Called whenever a clip is selected with a multi-clip behaviour e.g. dragging or control button down. 
	 * @param pamClipPane - the currently selected PamClipPane. 
	 */
	public void selectMultiClip(PamClipPane pamClipPane) {
		//need to stop playing audio.
		for (PamClipPane pamClip: selectedClips) {
			if (pamClip!=pamClipPane) pamClip.getAudioPlay().stopClipAudio();
		}

		if (!selectedClips.contains(pamClipPane)) {
			pamClipPane.setStyle("-fx-border-color: ACCENT_COLOR; -fx-border-width: 3px;");
			pamClipPane.getAudioPlay().getVolumePropery().bind(aiPamView.volumeProperty());
			pamClipPane.getAudioPlay().playClipAudio();
			selectedClips.add(pamClipPane); 
		}
	}

	/**
	 * Clear all currently selected clips. 
	 */
	private void repaintAllClips() {
		for (PamClipPane pamClipPane: selectedClips) {
			pamClipPane.setStyle("-fx-border-color: transparent; -fx-border-width: 3px;");
			pamClipPane.getAudioPlay().stopClipAudio();
		}
		selectedClips.clear();
	}

	/**
	 * Add mouse functionality to the pamclip 
	 */
	protected void addMouseBeahviour(PamClipPane pamClipPane) {

		pamClipPane.setOnMouseClicked((event)->{
			//pamClipPane.toFront(); //this creates big bug in the tile pane. moves all the clips to a differen location. 
			
			if (event.isControlDown()) {
				selectMultiClip(pamClipPane); 
			}
			else if (!(event.isSecondaryButtonDown() || event.isPopupTrigger())) {
				//don't want multi clips to dissappear on right click. 
				selectClip(pamClipPane); 
			}
			
			if (event.isSecondaryButtonDown() || event.isPopupTrigger()) {
				showSelectionMenu(selectedClips, event); 
			}
			
		});		

		//		this.setOnMouseDragOver((event)->{
		//			clipSelectionManager.selectMultiClip(PamClipPane.this); 
		//		});

		pamClipPane.setOnMouseEntered((event)->{
			//System.out.println("Mouse enterred: " + event);
			if (event.isPrimaryButtonDown()) {
				selectMultiClip(pamClipPane); 
			}
		});		
	}

	/**
	 * Show the selection menu. This has options for annotating clips etc. 
	 */
	private void showSelectionMenu(ArrayList<PamClipPane> selectedClips, javafx.scene.input.MouseEvent event) {

		if (selectionMenu==null) {
			selectionMenu = new SelectionMenu(this.aiPamView.getAIControl(), aiPamView); 
		}

		ArrayList<MenuItem> menuItems = selectionMenu.getMenu(selectedClips); 

		//System.out.println("Hello selection menu: " +  menuItems.size());

		final ContextMenu cm = new ContextMenu();
		cm.getItems().addAll(menuItems); 
		cm.show(aiPamView.getPrimaryStage(), event.getScreenX(), event.getScreenY());

	}


}
