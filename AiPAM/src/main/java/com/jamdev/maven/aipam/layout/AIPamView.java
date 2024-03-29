package com.jamdev.maven.aipam.layout;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.controlsfx.control.NotificationPane;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AITheme;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.annotation.AnnotationManager;
import com.jamdev.maven.aipam.layout.UserPrompts.UserPrompt;
import com.jamdev.maven.aipam.layout.clips.ClipGridPane;
import com.jamdev.maven.aipam.layout.clips.ClipSelectionManager;
import com.jamdev.maven.aipam.layout.clips.FullClipPane;
import com.jamdev.maven.aipam.layout.clustering.ClusterGraphPane;
import com.jamdev.maven.aipam.layout.utilsFX.HidingPane;
import com.jamdev.maven.aipam.layout.utilsFX.SettingsPane;
import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import jfxtras.styles.jmetro.MDL2IconFont;

/**
 * The main view for AIPam. This controls the GUI components and receivers messages from the 
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
	 * Holds the center stack of nodes
	 */
	private StackPane centerStackPane;

	/**
	 * Root 
	 */
	private Pane root;

	/**
	 * The theme for the UI. 
	 */
	private AITheme theme; 

	/**
	 * Pane which shows the full spectrogram of the clip and a few other controls. 
	 */
	private FullClipPane fullClipPane;

	private Button displayOptionsButton;

	private NotificationPane notificationPane;

	/**
	 * True if the progress pane is showing. 
	 */
	private boolean progressPaneShowing;



	public AIPamView(AiPamController aiPamControl, Stage primaryStage, Pane root) {

		this.primaryStage = primaryStage; 
		this.root=root; 

		//ai pam control. 
		this.aiPamContol = aiPamControl; 
		this.aiPamContol.addSensorMessageListener((flag, dataObject)->{
			notifyUpdate(flag, dataObject); 
		});

		userPrompts = new UserPrompts(this); 

		userPromptPane = new BorderPane(); 
		userPromptPane.setMaxHeight(20);
		userPromptPane.setPadding(new Insets(5,50,5,5));
		notificationPane = new NotificationPane();
		notificationPane.setStyle("-fx-background-color: BACKGROUND_TRANS;");

		//userPromptPane.setStyle("-fx-background-color: BACKGROUND_TRANS;"); 
		//labelSettings.prefWidthProperty().bind(holder.widthProperty());

		clipSelectionManager = new ClipSelectionManager(this); 


		controlPane= new ControlPane(this); 
		//set the current params here. Otherwise on getParams default values on controls will be returned...
		controlPane.setParams(aiPamContol.getParams()); 

		BorderPane controlPaneHolder = new BorderPane();
		controlPaneHolder.setLeft(controlPane);
		controlPaneHolder.setStyle("-fx-background-color: BACKGROUND_MENU");

		fullClipPane = new FullClipPane(); //for viewing clips in separate dialog

		clusterGraphPane = new ClusterGraphPane(this); 

		centerPane = new BorderPane(); 
		progressPane = new ProgressBarPane(this);
		progressPane.setPadding(new Insets(5,5,5,5));

		//create a cluster pane
		TabPane tabPane = new TabPane(); 
		tabPane.setSide(Side.RIGHT);
		Tab tabClip = new Tab("Clips",  createClipPane()); 
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
		
	
//		StackPane.setAlignment(notificationPane, Pos.TOP_CENTER);
		//hiding pane must be above the prompt pane or we can;t close it!
		centerStackPane.getChildren().add(hidingPane); 
		StackPane.setAlignment(hidingPane, Pos.TOP_LEFT);


		//too maessy having a button 
		//		centerStackPane.getChildren().add(displayOptionsButton = createViewSettingsButton()); 
		//		StackPane.setAlignment(displayOptionsButton, Pos.BOTTOM_RIGHT);
		//		StackPane.setMargin(displayOptionsButton, new Insets(20,20,20,20));

		notificationPane.setContent(centerStackPane);
		centerPane.setCenter(notificationPane);

		setCenter(centerPane);
		setLeft(controlPaneHolder);

		//apply the theme
		theme = new AITheme();
		setTheme(AITheme.JMETRO_DARK_THEME); 
//		theme.applyTheme(AITheme.JMETRO_DARK_THEME, root);
	}
	
	/**
	 * Create the clip pane with buttons. 
	 * @return the clip grid pane. 
	 */
	private Pane createClipPane() {
		//create the clip pane
		clipPane= new ClipGridPane(this); 

	
		final Button nextButton = new Button(); 
		nextButton.setPrefSize(40, 100);
//		MDL2IconFont iconFont1 = new MDL2IconFont("\uE893");
//		iconFont1.setSize(30);
//		iconFont1.setMaxSize(30, 30);
		
		FontAwesomeIconView iconFont1 = new FontAwesomeIconView(FontAwesomeIcon.FORWARD); 
		iconFont1.setGlyphSize(AIPamView.iconSize);
		iconFont1.setFill(Color.WHITE);
		

		nextButton.setGraphic(iconFont1);
		nextButton.setOnAction((action)->{
			//move to the previous section. 
			this.aiPamContol.nextPage(true);
		});
		
		
		final Button prevButton = new Button(); 
		prevButton.setPrefSize(40, 100);
//		MDL2IconFont iconFont2 = new MDL2IconFont("\uE892"
//				+ "");
//		iconFont2.setSize(30);
//		iconFont2.setMaxSize(30, 30);
		
		FontAwesomeIconView iconFont2 = new FontAwesomeIconView(FontAwesomeIcon.BACKWARD); 
		iconFont2.setGlyphSize(AIPamView.iconSize);
		iconFont2.setFill(Color.WHITE);
		
		prevButton.setGraphic(iconFont2);
		prevButton.setOnAction((action)->{
			//move to the next section
			this.aiPamContol.nextPage(false);
		});
		StackPane.setAlignment(prevButton, Pos.CENTER_LEFT);
		StackPane.setAlignment(nextButton, Pos.CENTER_RIGHT);
	
		final TranslateTransition ttPrev = new TranslateTransition(Duration.millis(50), prevButton);
		final TranslateTransition ttNext = new TranslateTransition(Duration.millis(50), nextButton);

		
		StackPane stackPane = new StackPane(); 
		stackPane.getChildren().add(clipPane); 
		stackPane.setOnMouseMoved((event)->{
			if (aiPamContol.getPamClipManager().hasPrevClips()) {
				prevButton.setVisible(true); 
				buttonAnimation(event,  prevButton,  ttPrev, false);
			}
			else {
				prevButton.setVisible(false); 
			}
			
			if (aiPamContol.getPamClipManager().hasNextClips()) {
				nextButton.setVisible(true); 
				buttonAnimation(event,  nextButton,  ttNext, true);
			}
			else {
				nextButton.setVisible(false); 
			}
		});

		stackPane.getChildren().addAll(prevButton, nextButton); 
		
		return stackPane; 
	}
	
	/**
	 * The button animation for shoeing buttons as the mouse comes close. 
	 * @param pane - the pane 
	 * @param button - the button
	 * @param ttPrev - the animation (creating new animations is slow )
	 */
	private void buttonAnimation(MouseEvent event, Button prevButton, TranslateTransition ttPrev, boolean forward) {
		double dtranslate = 60; 
		if (!forward) dtranslate = -1*dtranslate; 
		
		//System.out.println("Button translate: " + prevButton.getTranslateX());
		
		if (Math.abs(event.getX()-prevButton.getLayoutX())>150 && prevButton.getTranslateX()==0) {
			//System.out.println("Hide button: " );
			//hide the button
			ttPrev.setToX(dtranslate);
			ttPrev.play();
		}
		else if (Math.abs(event.getX()-prevButton.getLayoutX())<150 && prevButton.getTranslateX()==dtranslate){
			//System.out.println("Show button: " );
			//show the button
			ttPrev.setToX(0);
			ttPrev.play();
		}
	}


	//	/**
	//	 * Create the settings button for changing some basic click settings.  
	//	 */
	//	private Button createViewSettingsButton() {
	//		MDL2IconFont iconFont1 = new MDL2IconFont("\uE700");
	//		iconFont1.setSize(30);
	//		
	//		Button displayOptionsButton = new Button();
	//		displayOptionsButton.setGraphic(iconFont1);
	//		displayOptionsButton.setOnAction((action)->{
	//			PopMenu popMenu = new PopMenu(); 
	//		});
	//		
	//		return displayOptionsButton;
	//	}

	/**
	 * Show the progress pane. 
	 * @param show - true to show the pane. 
	 */
	private void showProgressPane(boolean show) {
		if (show) {
			this.progressPaneShowing = true; 
			//centerStackPane.getChildren().remove(userPromptPane);
			centerPane.setTop(progressPane= new ProgressBarPane(this));
			notificationPane.hide();
		}
		else {
			this.progressPaneShowing = false; 
			//centerStackPane.getChildren().remove(userPromptPane);
			//centerStackPane.getChildren().add(userPromptPane);
			hidingPane.toFront(); //myust be above everything. 
			centerPane.setTop(null); //replace with user promtp pane. 
		}
		setControlButtonDisable(show);  

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
		//		
		//		try {
		//			Thread.sleep(200);
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

		aiPamContol.loadAudioData(selectedDirectory);
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
			this.clipPane.clearSpecImages();
			this.clusterGraphPane.clearGraph(); 
			showProgressPane(true); 
			this.progressPane.setTask((Task) data);
			break;
		case AiPamController.START_PAGE_LOAD:
			this.clipPane.clearSpecImages();
			this.clusterGraphPane.clearGraph(); 
			showProgressPane(true); 
			this.progressPane.setTask((Task) data);
			break;
			
		case AiPamController.CANCELLED_FILE_LOAD:
			showProgressPane(false); 
			this.generateSpectrogramClips(); 
			break; 
			
		case AiPamController.CANCELLED_PAGE_LOAD:
			showProgressPane(false); 
			this.generateSpectrogramClips(); 
			break; 
		case AiPamController.END_PAGE_LOAD:
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
		case AiPamController.CANCEL_CLUSTERING_ALGORITHM:
			showProgressPane(false); 
			clusterGraphPane.update(aiPamContol.getPAMClips()); 
			checkSettings();
			break; 
		case AiPamController.END_CLUSTERING_ALGORITHM:
			showProgressPane(false); 
			clusterGraphPane.update(aiPamContol.getPAMClips()); 
			clipPane.layoutClips(); //layout the clips. 
			checkSettings();
			break; 
		case AiPamController.END_FILE_HEADER_LOAD:
			showProgressPane(false); 
			controlPane.notifyUpdate(updateType, null);
			checkSettings();
			break;
		case AiPamController.NO_AUDIO_DIRECTORY:
			showProgressPane(false); 
			this.showSettingsPane(controlPane.getAudioImportPane());
			checkSettings();
			break;
		case AiPamController.START_CLIP_EXPORT:
			showProgressPane(true); 
			this.progressPane.setTask((Task) data);
			break; 
		case AiPamController.CANCEL_CLIP_EXPORT:
			showProgressPane(false); 
			break;
		case AiPamController.END_CLIP_EXPORT:
			showProgressPane(false); 
			break;
		case AiPamController.NEW_CLIP_SELECTED:
			break;
		} 
		//update the control pane. 
		controlPane.notifyUpdate(updateType, data);
	}

	/**
	 * Set the control buttons to disabled so users can;t start multiple threads at once. 
	 * @param disable - true to disable. 
	 */
	private void setControlButtonDisable(boolean disable) {
		this.controlPane.setControlButtonDisable(disable); 
	}


	/**
	 * Starts a thread to generate spectrogram clips. 
	 */
	private void generateSpectrogramClips() {
		//not very neat but need to update control params so that they know last colour limits 
		this.aiPamContol.getLastAiParams().spectrogramParams.colourLims= this.getAIParams().spectrogramParams.colourLims;
		this.aiPamContol.getLastAiParams().spectrogramParams.spectrogramColour= this.getAIParams().spectrogramParams.spectrogramColour; 
		this.aiPamContol.getLastAiParams().spectrogramParams.fftHop= this.getAIParams().spectrogramParams.fftHop;
		this.aiPamContol.getLastAiParams().spectrogramParams.fftLength= this.getAIParams().spectrogramParams.fftLength; 

		clipPane.clearSpecImages(); 
		Task<Integer> task  = clipPane.generateSpecImagesTask(this.aiPamContol.getPAMClips());
		if (task == null) {
			System.out.println("There is no spectrogram clip task. The importer may have returned an error or was cancelled"); 
			return; 
		}

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
		return this.aiPamContol.getParams().spectrogramParams.colourLims;
	}


	/**
	 * Get the colour array based on current spectrogramColour ColourArrayType in params
	 * @return the current colour array type. 
	 */
	public ColourArray getCurrentColourArray() {
		if (this.colourArray==null || colourArray.getColorArrayType()!=aiPamContol.getParams().spectrogramParams.spectrogramColour) {
			colourArray = ColourArray.createStandardColourArray(N_COLS, this.aiPamContol.getParams().spectrogramParams.spectrogramColour); 
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
		iconViewSettings.getStyleClass().add("-fx-fill: TEXT_FILL");
		iconViewSettings.setFill(AIPamView.defaultTitleColour);
		button.setAlignment(pos);
		button.setGraphicTextGap(15);
		button.setGraphic(iconViewSettings);
	}


	public Image getClusterIcon() {
		return getIcon("clustering/Cluster.svg"); 
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
		aiPamContol.nextPage(true);
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
		if (userPrompts.getUserPromptPane(userPromptsA)!=null && this.getAIParams().showUserPrompts && !progressPaneShowing) {
			this.userPromptPane.setRight(userPrompts.getUserPromptPane(userPromptsA)); 
			
			notificationPane.setGraphic(userPromptPane);
			System.out.println("Show notification: " + notificationPane.isShowing());
			if (!this.notificationPane.isShowing())
			 this.notificationPane.show("");		
			}
		else {
			System.out.println("Hide notification: ");
			this.notificationPane.hide();
		}

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


	/**
	 * Exports annotation clips and organise into different folders.  
	 */
	public void exportAnnotations() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Export annotations");
		File file = directoryChooser.showDialog(primaryStage);
		if (file != null) {
			this.aiPamContol.exportAnnotations(file); 
		}
	}

	/**
	 * Save settings.
	 */
	public void saveSettings() {
		FileChooser fileChooser = new FileChooser();
		configureFileChooser(fileChooser);
		// Show open file dialog
		File file = fileChooser.showSaveDialog(primaryStage);
		if (file != null) {
			aiPamContol.saveSettings(file); 
		}
	}

	/**
	 * Load settings from a file. 
	 */
	public void loadSettings() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Alert");
		alert.setHeaderText("This feature is not yet available");
		String s ="Load settings is not yet available. Once available load settings will  "
				+ "allow users to reimport their settings, annotation and cluster results";
		alert.setContentText(s);
		alert.show();

		//		FileChooser fileChooser = new FileChooser();
		//		configureFileChooser(fileChooser);
		//		// Show open file dialog
		//		File file = fileChooser.showOpenDialog(primaryStage);
		//		if (file != null) {
		//			aiPamContol.loadSettings(file); 
		//		}
	}

	/**
	 * Configures the file chooser for .mat files
	 * @param fileChooser - the file chooser to set
	 */
	private static void configureFileChooser(final FileChooser fileChooser){                           
		fileChooser.setTitle("Settings file");
		ExtensionFilter extensionFilter = new ExtensionFilter("MAT", "*.mat"); 
		fileChooser.getExtensionFilters().add(extensionFilter);
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); 
	}


	/**
	 * Convenience function to get the annotation manager from the AiPamController. 
	 * This handles annotations
	 * @return the annotations manager. 
	 */
	public AnnotationManager getAnnotationsManager() {
		return this.aiPamContol.getAnnotationManager();
	}

	/**
	 * Set the look and feel of the UI.
	 * @param thetype - the theme type. e.g. AiTheme.JMETRO_LIGHT_THEME
	 */
	public void setTheme(int thetype) {
		
		//TEMP hack needs to go into CSS FILE. 
		switch (thetype) {
		case AITheme.JMETRO_DARK_THEME:
			 notificationPane.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
			break;
		case AITheme.JMETRO_LIGHT_THEME:
			//notificationPane.getStyleClass().clear();
			break;
		}
		this.theme.applyTheme(thetype, root); 
	}

	/**
	 * The full clip pane. Pane which shows the full spectrogram of the clip and a few other controls. 
	 * @return the full clip pane
	 */
	public FullClipPane getFullClipPane() {
		return fullClipPane;
	}

	/**
	 * Get the theme
	 * @return the theme to get. 
	 */
	public AITheme getTheme() {
		return theme;
	}

	/**
	 * Get the clip pane. 
	 * @return the clip pane. 
	 */
	public ClipGridPane getClipPane() {
		return clipPane;
	}


	/**
	 * Get user prompts manager. 
	 * @return the user prompts. 
	 */
	public UserPrompts getUserPrompts() {
		return userPrompts;
	}

}
