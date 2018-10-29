package com.jamdev.maven.aipam.layout;


import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.utils.SettingsPane;
import com.jamdev.maven.clips.AudioInfo;

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
import javafx.scene.layout.Priority;
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
public class AudioImportPane implements SettingsPane<AIPamParams>{
	
	/**
	 * Text field for the file. 
	 */
	private TextField textField;
	
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
	private ComboBox<Double> decimatorBox;

	/**
	 * Shows channel box values. 
	 */
	private ComboBox<Double> channelBox;

	/**
	 * The audio info. 
	 */
	private Label clipLength;

	private Label audioInfoLabel;
	
	/**
	 * The audio importing pane
	 */
	public AudioImportPane(AIPamView aiPamView) {
		this.aiPamView = aiPamView; 
	}


	private Pane createPane() {
		
		//create
		HBox fileImportHolder = new HBox();
		fileImportHolder.setSpacing(5);

		Label label = new Label("Import Audio Files"); 
		label.getStyleClass().add("label-title1");

		importButton = new Button("Browse"); 
		//	Text iconText = new MaterialDesignIconView(MaterialDesignIcon.FOLDER_DOWNLOAD);
		//	iconText.setStyle(String.format("-fx-font-size: %dpt", AIPamView.iconSize));
		//	importButton.setGraphic(iconText);
		importButton.setOnAction((action)->{
			aiPamView.openAudioFolder(); 
		});
		
		//Label which shows audio info. 
		audioInfoLabel = new Label("AudioInfo: "); 

		
		textField = new TextField(); 
		textField.setEditable(false);
		textField.prefHeightProperty().bind(importButton.heightProperty());
		textField.setText("Open an audio folder");
		HBox.setHgrow(textField, Priority.ALWAYS);

		fileImportHolder.getChildren().addAll(textField);


		//clip length 
		Label clipLength = new Label("Max. Clip Length"); 
		clipLength.getStyleClass().add("label-title1");


		ObservableList<Double> defaultClipTimes = FXCollections.observableArrayList(); 
		defaultClipTimes.addAll(0.2, 0.5, 1.0, 1.5, 2.0, 3.0, 5.0, 10.0);
		clipLengthBox = new ComboBox<Double>(defaultClipTimes); 
		clipLengthBox.setEditable(false);
		clipLengthBox.getSelectionModel().select(3.0);
		clipLengthBox.setConverter(new ClipSizeConverter());
		clipLengthBox.setTooltip(new Tooltip("Clips should be the same length. When imported \n"
				+ " the AIPAM will automatically trim clips which are longer than the max. clip \n"
				+ "length value. The trim take place from the center of the clip such i.e. \n. "
				+ "maximum clip length/2 before center of clip and maximum clip length/2 after the \n "
				+ "center of the clip. "));
		
		Label decimatorLabel = new Label("Decimator"); 
		decimatorLabel.getStyleClass().add("label-title1");

		
		decimatorBox = new ComboBox<Double>(defaultClipTimes); 
		decimatorBox.setDisable(true);
		
		Label channelLabel = new Label("Channel"); 
		channelLabel.getStyleClass().add("label-title1");
		
		channelBox = new ComboBox<Double>(defaultClipTimes); 
		channelBox.setDisable(true);

		VBox holder = new VBox();
		holder.setSpacing(5); 
		holder.getChildren().addAll(label,fileImportHolder, importButton, audioInfoLabel, clipLength, clipLengthBox, 
				decimatorLabel, decimatorBox, channelLabel, channelBox); 
		
		return holder; 
	}
	
	/**
	 * Set Audio Label. 
	 * @param audioInfo. 
	 */
	private void setAudioInfo(AudioInfo audioInfo) {
		audioInfoLabel.setTextFill(Color.WHITE);
		if (audioInfo==null) {
			audioInfoLabel.setText("No audio data in current directory \n"
					+ "Browse to a directory of audio files");
			audioInfoLabel.setTextFill(Color.YELLOW);
			textField.setText(""); 
			return;
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
			return;
		}
		else {
			audioInfoLabel.setText(String.format("No. Files: % d Channels: %d Sample Rate: %.0f", 
					audioInfo.nFiles, audioInfo.channels, audioInfo.sampleRate));
			textField.setText(audioInfo.file); 
			setUpDecimatorBox(); 
			setUpChannelBox();
		}
		
	}
	
	/**
	 * Set the decimator values- theseobviously have to be less than the nyquist frequency
	 */
	private void setUpChannelBox() {
		// TODO Auto-generated method stub
		
	}


	private void setUpDecimatorBox() {
		// TODO Auto-generated method stub
		
	}


	private void setSampleRate() {
		ObservableList<Double> decimatorSR = FXCollections.observableArrayList(); 
		decimatorSR.addAll(0.2, 0.5, 1.0, 1.5, 2.0, 3.0, 5.0, 10.0);
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
		if (mainPane==null) {
			mainPane = createPane(); 
		}
		return mainPane;
	}


	@Override
	public AIPamParams getParams(AIPamParams paramsIn) {
			
		paramsIn.audioFolder=this.textField.getText(); 
		paramsIn.maximumClipLength = clipLengthBox.getValue(); 

		paramsIn.audioFolder=this.textField.getText(); 
		paramsIn.maximumClipLength = clipLengthBox.getValue(); 
	
		
		return paramsIn;
	}

	private void checkFolder() {
		
	}


	@Override
	public void setParams(AIPamParams params) {
		
		// TODO Auto-generated method stub
		textField.setText(params.audioFolder); 
		clipLengthBox.getSelectionModel().select(params.maximumClipLength);
		
		setAudioInfo(this.aiPamView.getAIControl().getAudioInfo()); 
	
		textField.setText(params.audioFolder); 
		clipLengthBox.getSelectionModel().select(params.maximumClipLength);
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
			setAudioInfo(this.aiPamView.getAIControl().getAudioInfo()); 
			break;
		} 
		
	}

}
