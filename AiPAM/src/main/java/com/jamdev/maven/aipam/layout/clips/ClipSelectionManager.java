package com.jamdev.maven.aipam.layout.clips;

import java.util.ArrayList;
import java.util.List;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.layout.AIPamView;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
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
	 * Auto playing boolean
	 */
	private SimpleBooleanProperty autoPlayProperty = new SimpleBooleanProperty();

	/**
	 * The current audio task
	 */
	private AutoPlayBackTask audioTask; 

	/**
	 * Constructor for the clip selection manager.
	 */
	public ClipSelectionManager(AIPamView aiPamView2) {
		this.aiPamView = aiPamView2; 
	}
	
	/**
	 * Clear highlight a clip
	 * @param pamClipPane
	 */
	private void highlightSelectClip(PamClipPane pamClipPane) {
		pamClipPane.setStyle("-fx-border-color: ACCENT_COLOR; -fx-border-width: 3px;");
		pamClipPane.getAudioPlay().getVolumePropery().bind(aiPamView.volumeProperty());
	}

	/**
	 * Called whenever a clip is selected. 
	 * @param pamClipPane - the currently selected PamClipPane. 
	 */
	public void selectClip(PamClipPane pamClipPane) {
		 stopClipAutoPlay();
		repaintAllClips(); 
		highlightSelectClip(pamClipPane); 
		pamClipPane.getAudioPlay().playClipAudio();
	
		//add selected clip to the list. 
		selectedClips.add(pamClipPane); 
		
		//send a notification that clips have been selected. 
		aiPamView.notifyUpdate(AiPamController.NEW_CLIP_SELECTED, pamClipPane.getPamClip());
	}

	/**
	 * Called whenever a clip is selected with a multi-clip behaviour e.g. dragging or control button down. 
	 * @param pamClipPane - the currently selected PamClipPane. 
	 */
	public void selectMultiClip(PamClipPane pamClipPane) {
		 stopClipAutoPlay();
		//need to stop playing audio.
		for (PamClipPane pamClip: selectedClips) {
			if (pamClip!=pamClipPane) pamClip.getAudioPlay().stopClipAudio();
		}

		if (!selectedClips.contains(pamClipPane)) {
			pamClipPane.setStyle("-fx-border-color: ACCENT_COLOR; -fx-border-width: 3px;");
			pamClipPane.getAudioPlay().getVolumePropery().bind(aiPamView.volumeProperty());
			pamClipPane.getAudioPlay().playClipAudio();
			//add a clip to the list. 
			selectedClips.add(pamClipPane); 
			
			//send a notification that clips have been selected. 
			aiPamView.notifyUpdate(AiPamController.NEW_CLIP_SELECTED, pamClipPane.getPamClip());
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
	 * Add mouse functionality to the pam clip 
	 */
	protected void addMouseBeahviour(PamClipPane pamClipPane) {

		pamClipPane.setOnMouseClicked((event)->{
			//pamClipPane.toFront(); //this creates big bug in the tile pane. moves all the clips to a different location. 
			
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

	/**
	 * Auto play all clips. 
	 * 
	 * @param pamClip the pam clip to start play back from
	 */
	public void autoPlayClips(PamClipPane pamClip) {
//		System.out.println("Auto play all the clips!!");
		
		List<PamClipPane> playPanes = aiPamView.getClipPane().getCurrentPamClips(); 
		if (pamClip!=null) {
			playPanes= playPanes.subList(playPanes.indexOf(pamClip), playPanes.size()-1); 
		}

		
		Thread th = new Thread(audioTask= new AutoPlayBackTask(
				playPanes, aiPamView.getAIParams()));
		
		th.setDaemon(true);
		th.start(); 
		autoPlayProperty.setValue(true);
	}

	/**
	 * The auto play property to indicate when all clips are being played in order. 
	 * @return the auto play boolean property. 
	 */
	public BooleanProperty autoPlayProperty() {
		return this.autoPlayProperty;
	}

	/**
	 * Stop auto playing clips. 
	 */
	public void stopClipAutoPlay() {
		if (audioTask==null) return;
		audioTask.cancel(false); 
		autoPlayProperty.setValue(false);
	}
	
	
	/**
	 * The audio play back task. 
	 * 
	 * @author Jamie Macaulay 
	 */
	public class AutoPlayBackTask extends Task<Integer> {

		private List<PamClipPane> pamClips;
		
		private AIPamParams params;
		
		public AutoPlayBackTask(List<PamClipPane> pamClips, AIPamParams params) {
			this.params=params; 
			this.pamClips=pamClips; 		
		}

		@Override
		protected Integer call() throws Exception {
			//progress is in intermediate mode. 
			try {
				for (int i=0; i<pamClips.size(); i++) {
					int ii=i; //make a new reference
					Platform.runLater(()->{
						clearClips();
						highlightSelectClip(pamClips.get(ii)); 
					}); 
					Thread.sleep(500);
//					System.out.println("Auto play clip: " + pamClips.get(i));
					pamClips.get(i).getAudioPlay().playClipAudio();
					while (pamClips.get(i).getAudioPlay().isPlaying()) {
						if (this.isCancelled()) {
							pamClips.get(i).getAudioPlay().stopClipAudio();
							return -1; 
						}
						//do nothing until clip play back has finished
						Thread.sleep(500); 
					}
				}
				//System.out.println("Hello: Finished!!!!");
				return 1; 
			}
			catch (Exception e) {
				e.printStackTrace();
				return -1; 
			}
		}
		
		private void clearClips() {
    		for (int j=0; j<pamClips.size(); j++) {
    			pamClips.get(j).setStyle("-fx-border-color: transparent; -fx-border-width: 3px;");
    		}
		}
		
        @Override 
        protected void cancelled() {
        	Platform.runLater(()->{
        		clearClips();
        	}); 
            super.cancelled();
        }

	}

}
