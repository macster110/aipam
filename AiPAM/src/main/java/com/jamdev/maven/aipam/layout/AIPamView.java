package com.jamdev.maven.aipam.layout;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.layout.UserPrompts.UserPrompt;
import com.jamdev.maven.aipam.layout.clips.ClipGridPane;
import com.jamdev.maven.aipam.layout.clips.ClipSelectionManager;
import com.jamdev.maven.aipam.layout.clustering.ClusterGraphPane;
import com.jamdev.maven.aipam.layout.utilsFX.HidingPane;
import com.jamdev.maven.aipam.layout.utilsFX.SettingsPane;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.DoubleBinding;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
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
 * The main view for AIPam. This controls the GUI components and recievers messages from the 
 * controller. 
 * 
 * @author Jamie Macaulay
 *
 */
@SuppressWarnings("rawtypes")
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

	/**
	 * User prompts 
	 */
	private UserPrompts userPrompts;

	/**
	 * Pane which shows user prompts
	 */
	private BorderPane userPromptPane;

	/**
	 * Holds the center stakc of nodes
	 */
	private StackPane centerStackPane; 

	public AIPamView(AiPamController aiPamControl, Stage primaryStage) {

		this.primaryStage = primaryStage; 
		this.aiPamContol = aiPamControl; 
		this.aiPamContol.addSensorMessageListener((flag, dataObject)->{
			notifyUpdate(flag, dataObject); 
		});
		
		userPrompts = new UserPrompts(this); 
		
		userPromptPane = new BorderPane(); 
		userPromptPane.setMaxHeight(20);
		userPromptPane.setPadding(new Insets(5,50,5,5));
		//userPromptPane.setStyle("-fx-background-color: BACKGROUND_TRANS;"); 
		//labelSettings.prefWidthProperty().bind(holder.widthProperty());
		
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
		tabPane.setSide(Side.RIGHT);
		Tab tabClip = new Tab("Clips", clipPane); 
		Tab tabCluster = new Tab("ClusterGraph", clusterGraphPane);
		tabClip.setClosable(false);
		tabCluster.setClosable(false);
		tabPane.getTabs().addAll(tabClip, tabCluster); 

		settingsHolder = new BorderPane();
		
		//GaussianBlur blur = new GaussianBlur(20);       
		settingsHolder.setPrefWidth(350);
		settingsHolder.setStyle("-fx-background-color: BACKGROUND_TRANS;"); 
		//settingsHolder.setEffect(blur);
		settingsHolder.setPadding(new Insets(5,5,5,10));

		centerStackPane = new StackPane(); 
		hidingPane=new HidingPane(Side.LEFT, settingsHolder,  centerStackPane, true);
		hidingPane.setPrefWidth(100);
		//hidingPane.setStyle("-fx-background-color: blue;");
		hidingPane.showingProperty().addListener((obsVal, oldVal, newVal)->{
			//return the menu to it's deselected state if no button showing. 
			if (!newVal) {
				controlPane.setMenuDeselected();
			}
		});
		
		
		centerStackPane.getChildren().add(tabPane);
		centerStackPane.getChildren().add(userPromptPane); 
		StackPane.setAlignment(userPromptPane, Pos.TOP_RIGHT);
		//hiding pane must be above the prompt pane or we can;t close it!
		centerStackPane.getChildren().add(hidingPane); 
		StackPane.setAlignment(hidingPane, Pos.TOP_LEFT);

		centerPane.setCenter(centerStackPane);

		setCenter(centerPane);
		setLeft(controlPaneHolder);
		
	}


	/**
	 * Show the progress pane. 
	 * @param show - true to show the pane. 
	 */
	private void showProgressPane(boolean show) {
		if (show) {
			centerStackPane.getChildren().remove(userPromptPane);
			centerPane.setTop(progressPane= new ProgressBarPane(this));
		}
		else {
			centerStackPane.getChildren().add(userPromptPane);
			hidingPane.toFront(); //myust be above everything. 
			centerPane.setTop(null); //replace with user promtp pane. 
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
		//System.out.println("AIPamView: notifyUpdate: " +updateType + " " + data);
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
			checkSettings();
			break; 
		case AiPamController.CANCELLED_IMAGE_LOAD:
			showProgressPane(false); 
			break; 
		case AiPamController.END_IMAGE_LOAD:
			showProgressPane(false); 
			//System.out.println("Tile pane: " + clipPane.getTilePane().getChildren().size());
			checkSettings();
			break; 
		case AiPamController.START_CLUSTERING_ALGORITHM:			
			showProgressPane(true); 
			progressPane.setTask((Task) data);
			break; 
		case AiPamController.END_CLUSTERING_ALGORITHM:
			showProgressPane(false); 
			clusterGraphPane.update(aiPamContol.getPAMClips()); 
			clipPane.layoutClips(); //layout the clips. 
			checkSettings();
			break; 
		case AiPamController.END_FILE_HEADER_LOAD:
			showProgressPane(false); 
			controlPane.getAudioImportPane().notifyUpdate(AiPamController.END_FILE_HEADER_LOAD, null);
			checkSettings();
			break;
		case AiPamController.NO_AUDIO_DIRECTORY:
			showProgressPane(false); 
			this.showSettingsPane(controlPane.getAudioImportPane());
			checkSettings();
			break;
		} 
		
	}


	/**
	 * Starts a thread to generate spectrogram clips. 
	 */
	private void generateSpectrogramClips() {
		//not very neat but need to update control params so that they know last colour limits 
		this.aiPamContol.getLastAiParams().colourLims= this.getAIParams().colourLims;
		this.aiPamContol.getLastAiParams().spectrogramColour= this.getAIParams().spectrogramColour; 

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
	 * Recalculate the spectrogram images. 
	 */
	public void reCalcImages() {
		generateSpectrogramClips();
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
		controlPane.setSelectedPane(settingsPane);
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
	
	public Image getClusterIcon(int size) {
		return UtilsFX.scale(getIcon("Cluster.svg"), size, size, true); 
	}


	public Image getSpectrogramIcon(int size) {
		return UtilsFX.scale(getIcon("Audio.svg"), size, size, true); 
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
	 * Check that the last settings used to import clips and/or cluster are the same
	 * If not then presents a message to the user indicating that files need re imported etc. 
	 */
	public void checkSettings() {
		//ArrayList<Integer> toDoFlags = checkLastSettings(); 
		ArrayList<UserPrompt> userPromptsA = userPrompts.checkLastSettings();		
		//set the message in the user prompt pane. 
		this.userPromptPane.setRight(userPrompts.getUserPromptPane(userPromptsA)); 
		
		//now lets try an highlight some control buttons
		this.controlPane.setUserPrompt(userPromptsA);
		
	}

	/**
	 * The volume property which specifies volume between 0 and 1. Changed by volume controls. 
	 * @return the volume property.
	 */
	public DoubleBinding volumeProperty() {		// TODO Auto-generated method stub
		return controlPane.volumeProperty();
	}

	/**
	 * Browse for audio folder and figure out audio file information 
	 */
	public void browseAndCheckAudio() {
		openAudioFolder(); 
		showSettingsPane(controlPane.getAudioImportPane());
	}


	public EventHandler<ActionEvent> exportAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}




}
