package com.jamdev.maven.aipam.layout;


import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.clips.AudioInfo;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.util.StringConverter;

/**
 * Pane with controls for importing audio data.
 * 
 * @author Jamie Macaulay
 *
 */
public class AudioImportPane extends DynamicSettingsPane<AIPamParams>{

	/**
	 * Text field for the file. 
	 */
	private Label textField;

	/**
	 * The import button 
	 */
	private Button importButton;

	/**
	 * Directory chooser for audio files 
	 */
	private DirectoryChooser chooser;

	/**
	 * Selects possible clips sizes. 
	 */
	private ComboBox<Double> clipLengthBox;

	/**
	 * Reference to a.i. pam view. 
	 */
	private AIPamView aiPamView;

	/**
	 * The main pane. 
	 */
	private Pane mainPane;

	/**
	 * Shows decimator values. 
	 */
	private ComboBox<Integer> decimatorBox;

	/**
	 * Shows channel box values. 
	 */
	private ComboBox<Integer> channelBox;

	/**
	 * The audio info. 
	 */
	private Label clipLength;

	private Label audioInfoLabel;

	/**
	 * A list of possible decimator values based on standard sample rates in bioacoustics. 
	 */
	private ObservableList<Integer> decimatorSR; 

	/**
	 * The audio importing pane
	 */
	public AudioImportPane(AIPamView aiPamView) {
		this.aiPamView = aiPamView; 
		mainPane = createPane(); 
	}


	private Pane createPane() {

		VBox holder = new VBox();
		holder.setSpacing(5); 
		holder.setMaxWidth(300);
		
		//create
		HBox fileImportHolder = new HBox();
		fileImportHolder.setSpacing(5);

		Label label = new Label("Import Audio Files"); 
		label.getStyleClass().add("label-title2");

		importButton = new Button("Browse"); 
		//	Text iconText = new MaterialDesignIconView(MaterialDesignIcon.FOLDER_DOWNLOAD);
		//	iconText.setStyle(String.format("-fx-font-size: %dpt", AIPamView.iconSize));
		//	importButton.setGraphic(iconText);
		importButton.setOnAction((action)->{
			aiPamView.openAudioFolder(); 
			
			//do not notify settings listeners here. Need to check out the .wav  file first. 
		});

		//Label which shows audio info. 
		audioInfoLabel = new Label("AudioInfo: "); 


		/**
		 * FIXME. There seems to be a bug in JavaFX 13 which causesa textfield to constantly expand slowly.
		 * Really really weird
		 */
		//textField = new TextField(); 
		textField = new Label(); 
		//textField.setEditable(true);
		textField.setStyle("-fx-border-color:gray; -fx-background-color: gray;");
		textField.prefHeightProperty().bind(importButton.heightProperty());
		textField.setText("Open an audio folder");
		textField.prefWidthProperty().bind(holder.widthProperty().subtract(10));
		
		//below worked with Java 8 but causes the pane to resize slwly to infinity in JavaFX 13 
//		HBox.setHgrow(textField, Priority.ALWAYS);

		//fileImportHolder.getChildren().addAll(textField);


		//clip length 
		Label clipLength = new Label("Max. Clip Length"); 
		clipLength.getStyleClass().add("label-title2");


		ObservableList<Double> defaultClipTimes = FXCollections.observableArrayList(); 
		defaultClipTimes.addAll(0.2, 0.5, 1.0, 1.5, 2.0, 3.0, 5.0, 7.5, 10.0, 15.0, 20.0, 30.0, 40.0);
		clipLengthBox = new ComboBox<Double>(defaultClipTimes); 
		clipLengthBox.setEditable(false);
		clipLengthBox.getSelectionModel().select(3.0);
		clipLengthBox.setConverter(new ClipSizeConverter());
		clipLengthBox.setTooltip(new Tooltip("Clips should be the same length. When imported \n"
				+ " the AIPAM will automatically trim clips which are longer than the max. clip \n"
				+ "length value. The trim take place from the center of the clip such i.e. \n. "
				+ "maximum clip length/2 before center of clip and maximum clip length/2 after the \n "
				+ "center of the clip. "));
		clipLengthBox.setOnAction((action)->{
			notifySettingsListeners(); 
		});

		Label decimatorLabel = new Label("Decimator"); 
		decimatorLabel.getStyleClass().add("label-title2");


		decimatorBox = new ComboBox<Integer>(); 
		decimatorBox.setDisable(true);
		decimatorBox.setTooltip(new Tooltip("The decimator allows users to down sample data \n"
				+ "Decimated data is filtered first to prevent aliasing. It is then down sampled \n"
				+ "to the desired sample rate. It is useful to deimate data if the features of \n"
				+ "intertest are in lower frequency bands, e.g. baleen whales. This gives the \n"
				+ "clusterring algorithm more relevent spectrogram images to analyse."));
		decimatorBox.setOnAction((action)->{
			notifySettingsListeners(); 
		});

		decimatorSR = FXCollections.observableArrayList(); 
		//set some standard decimator values. 
		decimatorSR.addAll(50, 100, 200, 400, 500, 1000, 2000, 4000, 6000, 10000, 16000, 24000, 32000, 36000, 44100, 48000, 
				60000, 80000, 96000, 128000, 192000, 256000, 500000, 1000000);


		Label channelLabel = new Label("Channel"); 
		channelLabel.getStyleClass().add("label-title2");

		channelBox = new ComboBox<Integer>(); 
		channelBox.setDisable(true);
		channelBox.setOnAction((action)->{
			notifySettingsListeners(); 
		});

		holder.getChildren().addAll(label,textField, importButton, audioInfoLabel, clipLength, clipLengthBox, 
				decimatorLabel, decimatorBox, channelLabel, channelBox); 

		return holder; 
	}

	/**
	 * Set Audio labels and set up the decimator and channel combo boxes so that they have the correct
	 * values i.e. correct possible decimator values and the correct channel numbers. 
	 * @param audioInfo - audio info describing the audio files. 
	 */
	private void setAudioInfo(AudioInfo audioInfo, AIPamParams aiParams) {
		
		//System.out.println("TextField text: setAudioInfo"); 

		audioInfoLabel.setTextFill(Color.WHITE);
		if (audioInfo==null) {
			audioInfoLabel.setText("No audio data in current directory \n"
					+ "Browse to a directory of audio files");
			audioInfoLabel.setTextFill(Color.YELLOW);
			textField.setText(""); 
		}
		else if (!audioInfo.isSameChannels) {
			audioInfoLabel.setText("The clips have different numbers of channels");
			audioInfoLabel.setTextFill(Color.RED);
			textField.setText(""); 
		}
		else if (!audioInfo.isSameSampleRate) {
			audioInfoLabel.setText("The clips have different sample rates");
			audioInfoLabel.setTextFill(Color.RED);
			textField.setText(""); 
		}

		else {
			audioInfoLabel.setText(String.format("No. Files: % d \nNo.Corrupt Files: %d \nChannels: %d \nSample Rate: %.0f \n"
					+ "Press 'Generate Clips to import", 
					audioInfo.nFiles, audioInfo.nFilesCorrupt, audioInfo.channels, audioInfo.sampleRate));
		
		
			textField.setText(audioInfo.file); 

			//set up the channel and audio info boxes
			this.setAllowNotify(false);
			setUpDecimatorBox(audioInfo, aiParams); 
			setUpChannelBox(audioInfo, aiParams);
			this.setAllowNotify(true);

			//			System.out.println("SET AUDIO INFO: decimatorBox: " + decimatorBox.getValue()); 
			//			System.out.println("SET AUDIO INFO: channelBox: " + channelBox.getValue()); 

			return;
		}

		setUpDecimatorBox(null, null); 
		setUpChannelBox(null, null);

		//now notify the setting listeners that the audio file settings have changed. 
		this.notifySettingsListeners();
	}

	/**
	 * Set the decimator values- these obviously have to be less than the Nyquist frequency
	 * @param audioInfo - the current audio information
	 * @param aiParams 
	 */
	private void setUpDecimatorBox(AudioInfo audioInfo, AIPamParams aiParams) {
		decimatorBox.getItems().clear();
		if (audioInfo==null) {
			//decimatorBox.setDisable(true);
		}
		else {
			int n=0; 
			decimatorBox.setDisable(false);
			while (audioInfo.sampleRate>=decimatorSR.get(n) && n<decimatorSR.size()) {
				decimatorBox.getItems().add(decimatorSR.get(n)); 
				n++;
			}
			//now try an set from current params
			if (decimatorBox.getItems().indexOf(aiPamView.getAIParams().decimatorSR)!=-1) {
				decimatorBox.getSelectionModel().select(aiParams.decimatorSR);
			}
			else decimatorBox.getSelectionModel().selectLast(); //select the current sample rate: default not decimation 
		}

	}

	/**
	 * Set up the channel box
	 * @param audioInfo - the current audio information
	 */
	private void setUpChannelBox(AudioInfo audioInfo, AIPamParams aiParams) {
		channelBox.getItems().clear(); 
		if (audioInfo==null || audioInfo.channels==1) {
			//channelBox.setDisable(true);
			if (audioInfo!=null) {
				channelBox.getItems().add(0); 
			}
		}
		else {
			channelBox.setDisable(false);
			for (int i=0; i<audioInfo.channels; i++) {
				channelBox.getItems().add(i); 
			}
			//now try an set from current params
			if (channelBox.getItems().indexOf(aiParams.channel)!=-1) {
				channelBox.getSelectionModel().select(aiParams.channel);
			}
			else channelBox.getSelectionModel().selectFirst(); //channel 0
		}
	}



	/**
	 * Class for showing seconds value on clip size combo box. 
	 * @author Jamie Macaulay 
	 *
	 */
	public class ClipSizeConverter extends StringConverter<Double>
	{

		@Override
		public String toString(Double object) {
			//add a seconds value
			return String.format("%.1f s", object);
		}

		@Override
		public Double fromString(String string) {
			//remove all letters from value and try to parse the double
			String str = string.replaceAll("[^\\d.]", "");
			return Double.parseDouble(str); 
		}

	}

	@Override
	public Pane getPane() {
		return mainPane;
	}


	@Override
	public AIPamParams getParams(AIPamParams paramsIn) {

		paramsIn.audioFolder=textField.getText(); 
		paramsIn.maximumClipLength = clipLengthBox.getValue(); 

		//		System.out.println("GETPARAMS: ChannelBox value: " + channelBox.getValue()); 
		//		System.out.println("GETPARAMS: DecimatorBox value: " + decimatorBox.getSelectionModel().getSelectedItem()); 

		if (!channelBox.isDisable()) paramsIn.channel = channelBox.getValue();
		if (!decimatorBox.isDisable()) paramsIn.decimatorSR = decimatorBox.getSelectionModel().getSelectedItem(); 

		//		System.out.println("The decimator value is: " + paramsIn.decimatorSR);
		return paramsIn;
	}



	@Override
	public void setParams(AIPamParams params) {

		//System.out.println("SEPARAMS:  decimator value is: " + params.decimatorSR);
		//		System.out.println("SEPARAMS:  channel value is: " + params.channel);

		this.setAllowNotify(false);

		textField.setText(params.audioFolder); 
		clipLengthBox.getSelectionModel().select(params.maximumClipLength);

		setAudioInfo(this.aiPamView.getAIControl().getAudioInfo(), params); 

		this.setAllowNotify(true);


	}


	@Override
	public Node getIcon() {
		FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.FILE_AUDIO_ALT); 
		iconView.setGlyphSize(AIPamView.iconSize);
		iconView.setFill(Color.WHITE);
		return iconView;
	}
	;

	@Override
	public String getTitle() {
		return "Audio";
	}


	@Override
	public void notifyUpdate(int flag, Object stuff) {
		switch (flag) {
		case AiPamController.END_FILE_HEADER_LOAD:
			setAudioInfo(this.aiPamView.getAIControl().getAudioInfo(),
					aiPamView.getAIControl().getParams()); 
			break;
		} 

	}

}
