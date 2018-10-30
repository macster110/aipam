package com.jamdev.maven.aipam.layout;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.layout.clips.ClipGridPane;
import com.jamdev.maven.aipam.layout.clips.ClipSelectionManager;
import com.jamdev.maven.aipam.layout.clustering.ClusterGraphPane;
import com.jamdev.maven.aipam.layout.utilsFX.HidingPane;
import com.jamdev.maven.aipam.utils.SettingsPane;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * The main view for PamSne
 * 
 * @author Jamie Macaulay
 *
 */
public class AIPamView extends BorderPane {

	/**
	 * The standard icon size. 
	 */
	public static int iconSize = 16;

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
	 * The default colour for title fonts. 
	 */
	public static Color defaultTitleColour = Color.WHITE;

	/**
	 * The hiding pane for settings. 
	 */
	private HidingPane hidingPane;

	/**
	 * The pane which holds the settings panes inside the hide pane
	 */
	private BorderPane settingsHolder; 

	public AIPamView(AiPamController aiPamControl, Stage primaryStage) {

		this.primaryStage = primaryStage; 
		this.aiPamContol = aiPamControl; 
		this.aiPamContol.addSensorMessageListener((flag, dataObject)->{
			notifyUpdate(flag, dataObject); 
		});
		controlPane= new ControlPane(this); 
		//set the current params here. Otherwise on getParams default values on controls will be returned...
		controlPane.setParams(aiPamContol.getParams()); 

		BorderPane controlPaneHolder = new BorderPane();
		controlPaneHolder.setLeft(controlPane);
		controlPaneHolder.setStyle("-fx-background-color: BACKGROUND_MENU");

		clipPane= new ClipGridPane(this); 

		clipSelectionManager = new ClipSelectionManager(this); 

		clusterGraphPane = new ClusterGraphPane(this); 

		centerPane = new BorderPane(); 
		progressPane = new ProgressBarPane(this);
		progressPane.setPadding(new Insets(5,5,5,5));

		//create a cluster pane
		TabPane tabPane = new TabPane(); 
		Tab tabClip = new Tab("Clips", clipPane); 
		Tab tabCluster = new Tab("ClusterGraph", clusterGraphPane);
		tabClip.setClosable(false);
		tabCluster.setClosable(false);
		tabPane.getTabs().addAll(tabClip, tabCluster); 

		settingsHolder = new BorderPane();
		settingsHolder.setCenter(new Label("hello"));
		settingsHolder.setPrefWidth(300);
		settingsHolder.setStyle("-fx-background-color: BACKGROUND;"); 
		settingsHolder.setPadding(new Insets(5,5,5,10));

		StackPane pane = new StackPane(); 
		hidingPane=new HidingPane(Side.LEFT, settingsHolder,  pane, true);
		hidingPane.setPrefWidth(100);
		hidingPane.setStyle("-fx-background-color: blue;");
		pane.getChildren().add(tabPane);
		pane.getChildren().add(hidingPane); 
		StackPane.setAlignment(hidingPane, Pos.TOP_LEFT);

		centerPane.setCenter(pane);

		setCenter(centerPane);
		setLeft(controlPaneHolder);
		
	}


	/**
	 * Show the progress pane. 
	 * @param show - true to show the pane. 
	 */
	private void showProgressPane(boolean show) {
		if (show) {
			centerPane.setTop(progressPane= new ProgressBarPane(this));
		}
		else {
			centerPane.setTop(null);
		}
	}



	public void openAudioFolder() {
		chooser = new DirectoryChooser();
		chooser.setTitle("Select the Directory with Clips");
		//		File defaultDirectory = new File("c:/dev/javafx");
		//		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(getPrimaryStage());

		if (selectedDirectory==null) {
			//do nothing
			return;
		}
		
		aiPamContol.loadAudioData(selectedDirectory, false);
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
		case AiPamController.END_FILE_HEADER_LOAD:
			showProgressPane(false); 
			controlPane.getAudioImportPane().notifyUpdate(AiPamController.END_FILE_HEADER_LOAD, null);
			break;
		case AiPamController.NO_AUDIO_DIRECTORY:
			showProgressPane(false); 
			this.showSettingsPane(controlPane.getAudioImportPane());
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

	/**
	 * Show a settings pane. 
	 * @param settingsPane
	 */
	public void showSettingsPane(SettingsPane<AIPamParams> settingsPane) {
		settingsHolder.setCenter(settingsPane.getPane());
		settingsPane.setParams(this.aiPamContol.getParams());
		hidingPane.showHidePane(true); 


	}

	/**
	 * Set an icon on a button
	 * @param button - the button to set icon on. 
	 * @param icon - the icon type
	 */
	public static void setButtonIcon(Labeled button, FontAwesomeIcon icon) {
		setButtonIcon(button, icon, Pos.BASELINE_LEFT); 
	}
	
	/**
	 * Set an icon on a button
	 * @param button - the button to set icon on. 
	 * @param icon - the icon type
	 * @param pos - the position of the icon and text. 
	 */
	public static void setButtonIcon(Labeled button, FontAwesomeIcon icon, Pos pos) {
		FontAwesomeIconView iconViewSettings = new FontAwesomeIconView(icon); 
		iconViewSettings.setGlyphSize(AIPamView.iconSize);
		iconViewSettings.setFill(AIPamView.defaultTitleColour);
		button.setAlignment(pos);
		button.setGraphicTextGap(15);
		button.setGraphic(iconViewSettings);
	}


	public Image getClusterIcon() {
		return getIcon("Cluster.svg"); 
	}


	public Image getSpectrogramIcon() {
		return getIcon("Audio.svg"); 
	}

	/**
	 * Get the cluster image. 
	 * @return the cluster image. 
	 */
	public Image getIcon(String relPath) {
		Image icon;
		try {
			icon = new Image(getClass().getResource(relPath).toURI().toURL().toExternalForm());
			return icon;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null; 
		}
	}

	/**
	 * Makes sure all settings are up to data and imports clips. 
	 */
	public void importAcoustic() {
		controlPane.getParams(this.aiPamContol.getParams()); 
		aiPamContol.importClips(); 
	}

	/**
	 * makes sure all settings are up to date and clusters. 
	 */
	public void cluster() {
		controlPane.getParams(this.aiPamContol.getParams()); 
		aiPamContol.clusterClips();
	}


	/**
	 * Figure what has changed since lad audio load. 
	 * @return a list of flags indicating what needs re run. 
	 */
	private ArrayList<Integer> checkLastSettings() {
		
		if (this.aiPamContol.getLastAiParams().fftHop!=this.aiPamContol.getParams().fftHop);
		if (this.aiPamContol.getLastAiParams().fttLength!=this.aiPamContol.getParams().fttLength); 
		
		if (this.aiPamContol.getLastAiParams().decimatorSR!=this.aiPamContol.getParams().decimatorSR);
		if (this.aiPamContol.getLastAiParams().channel!=this.aiPamContol.getParams().channel); 
		if (!this.aiPamContol.getLastAiParams().audioFolder.equals(aiPamContol.getParams().audioFolder)); 
		
		if (!this.aiPamContol.getLastAiParams().audioFolder.equals(aiPamContol.getParams().audioFolder)); 

		//TODO 
		return null; 
	}
	
	
	/**
	 * Check that the last settings used to import clips and/or cluster are the same
	 * If not then presents a message to the user indicating that files need re imported etc. 
	 */
	public void checkSettings() {
		//TODO
		
	}


}
