package com.jamdev.maven.aipam.layout;

import java.io.File;

import com.jamdev.maven.aipam.AiPamController;

import javafx.concurrent.Task;
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
	private ClipPane clipPane;

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
		
		clipPane= new ClipPane(this); 
		
		this.setLeft(controlPane);
		
		BorderPane centerPane = new BorderPane(); 
		centerPane.setTop(progressPane = new ProgressBarPane(this));
		progressPane.setVisible(false);
		centerPane.setCenter(clipPane);
		
		this.setCenter(centerPane);

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
		switch (updateType) {
		case AiPamController.START_FILE_LOAD:
			this.progressPane.setVisible(true);
			this.progressPane.setTask((Task) data);
			break;
		case AiPamController.END_FILE_LOAD:
			this.progressPane.setVisible(false);
			break; 
		} 
		
		
		
	}

}
