package com.jamdev.maven.aipam.layout;

import java.io.File;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.layout.clips.ClipGridPane;
import com.jamdev.maven.aipam.layout.clips.ClipSelectionManager;
import com.jamdev.maven.aipam.layout.clustering.ClusterGraphPane;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * The main view for PamSne
 * @author Jamie Macaulay
 *
 */
public class AIPamView extends BorderPane {

	/**
	 * The standard icon size. 
	 */
	public static int iconSize = 20;

	/**
	 * The control pane. 
	 */
	private ControlPane controlPane; 

	/**
	 * Holds the clips. 
	 */
	private ClipGridPane clipPane;

	/**
	 * The primary stage. 
	 */
	private Stage primaryStage;

	/**
	 * The directory chooser for audio file 
	 */
	private DirectoryChooser chooser;

	/**
	 * Reference to the control. 
	 */
	private AiPamController aiPamContol;

	/**
	 * Pane which shows the progress of any running task. 
	 */
	private ProgressBarPane progressPane;

	/**
	 * The current colour array. 
	 */
	private ColourArray colourArray;

	/**
	 * The number of colours in the colour array.
	 */
	private int N_COLS = 100;

	/**
	 * The center pane holder. 
	 */
	private BorderPane centerPane;

	/**
	 * The clip selection manager handles clip selection. 
	 */
	private ClipSelectionManager clipSelectionManager;

	/**
	 * The cluster graph pane. 
	 */	
	private ClusterGraphPane clusterGraphPane;  

	/**
	 * Default font for main label title
	 */
	public static Font defaultLabelTitle1 = new Font ("Segeo", 20); 

	/**
	 * Default font for sub title. 
	 */
	public static Font defaultLabelTitle2 = new Font("Segeo", 16);

	/**
	 * The default colour for title fonts. 
	 */
	public static Color defaultTitleColour = Color.WHITE;



	public AIPamView(AiPamController aiPamControl, Stage primaryStage) {

		this.primaryStage = primaryStage; 
		this.aiPamContol = aiPamControl; 
		this.aiPamContol.addSensorMessageListener((flag, dataObject)->{
			notifyUpdate(flag, dataObject); 
		});

		controlPane= new ControlPane(this); 

		clipPane= new ClipGridPane(this); 
		
		clipSelectionManager = new ClipSelectionManager(this); 
		
		clusterGraphPane = new ClusterGraphPane(this); 

		setLeft(controlPane);

		centerPane = new BorderPane(); 
		progressPane = new ProgressBarPane(this);
		progressPane.setPadding(new Insets(5,5,5,5));
		centerPane.setCenter(clipPane);
		centerPane.setRight(clusterGraphPane);
		
		this.setCenter(centerPane);

	}
	
	/**
	 * Show the progress pane. 
	 * @param show - true to show the pane. 
	 */
	private void showProgressPane(boolean show) {
		if (show) {
			centerPane.setTop(progressPane);
		}
		else {
			centerPane.setTop(null);
		}
	}



	public void openAudioFolder() {
		chooser = new DirectoryChooser();
		chooser.setTitle("JavaFX Projects");
		//		File defaultDirectory = new File("c:/dev/javafx");
		//		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(getPrimaryStage());

		if (selectedDirectory==null) {
			//do nothing
			return;
		}

		this.aiPamContol.loadAudioData(selectedDirectory);
	}


	/**
	 * Get the primary stage for the program. 
	 * @return the primary stage. 
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Notifies the view when there has been an update 
	 * @param updateType - the update type. 
	 */
	public void notifyUpdate(int updateType, Object data) {
		System.out.println("AIPamView: notifyUpdate: " +updateType + " " + data);
		switch (updateType) {
		case AiPamController.START_FILE_LOAD:
			showProgressPane(true); 
			this.progressPane.setTask((Task) data);
			break;
		case AiPamController.CANCELLED_FILE_LOAD:
			showProgressPane(false); 
			this.generateSpectrogramClips(); 
			break; 
		case AiPamController.END_FILE_LOAD:
			showProgressPane(false); 
			this.generateSpectrogramClips(); 
			break; 
		case AiPamController.CANCELLED_IMAGE_LOAD:
			showProgressPane(false); 
			break; 
		case AiPamController.END_IMAGE_LOAD:
			showProgressPane(false); 
			//System.out.println("Tile pane: " + clipPane.getTilePane().getChildren().size());
			break; 
		case AiPamController.START_CLUSTERING_ALGORITHM:			
			showProgressPane(true); 
			progressPane.setTask((Task) data);
			break; 
		case AiPamController.END_CLUSTERING_ALGORITHM:
			showProgressPane(false); 
			clusterGraphPane.update(aiPamContol.getPAMClips()); 
			break; 
		} 
	}


	/**
	 * Starts a thread to generate spectrogram clips. 
	 */
	private void generateSpectrogramClips() {
		clipPane.clearSpecImages(); 
		Task<Integer> task  = clipPane.generateSpecImagesTask(this.aiPamContol.getPAMClips());
		task.setOnCancelled((value)->{
			//send notification when 
			notifyUpdate(AiPamController.CANCELLED_IMAGE_LOAD, null); 
		});
		task.setOnSucceeded((value)->{
			//
			notifyUpdate(AiPamController.END_IMAGE_LOAD, null); 
		});
		
		showProgressPane(true); 
		this.progressPane.setTask((Task) task);

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start(); 
	}



	/**
	 * Convenience function to get the colour limits from current parameters.
	 */
	public double[] getClims() {
		return this.aiPamContol.getParams().colourLims;
	}


	/**
	 * Get the colour array based on current spectrogramColour ColourArrayType in params
	 * @return the current colour array type. 
	 */
	public ColourArray getCurrentColourArray() {
		if (this.colourArray==null || colourArray.getColorArrayType()!=aiPamContol.getParams().spectrogramColour) {
			colourArray = ColourArray.createStandardColourArray(N_COLS, this.aiPamContol.getParams().spectrogramColour); 
			System.out.println("Colour Array: " + colourArray.getColour(0) + " " + colourArray.getColour(colourArray.getNumbColours()-1)); 
		}
		return colourArray;
	}

	/**
	 * Get the clip selection manager. This manages the clip selections. 
	 * @return the clip selection manager. 
	 */
	public ClipSelectionManager getClipSelectionManager() {
		return this.clipSelectionManager;
	}

	/**
	 * Get the AiPamController
	 * @return the AiPamController. 
	 */
	public AiPamController getAIControl() {
		return aiPamContol; 
	}

	public AIPamParams getAIParams() {
		return getAIControl() .getParams();
	}

}
