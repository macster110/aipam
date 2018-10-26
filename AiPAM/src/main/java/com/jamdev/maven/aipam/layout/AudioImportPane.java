package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.utils.SettingsPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
	 * Diurectory chooser for audio files 
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
	
	private Pane mainPane;
	
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
		label.setFont(AIPamView.defaultLabelTitle1);
		label.setTextFill(AIPamView.defaultTitleColour);

		importButton = new Button("Browse"); 
		//	Text iconText = new MaterialDesignIconView(MaterialDesignIcon.FOLDER_DOWNLOAD);
		//	iconText.setStyle(String.format("-fx-font-size: %dpt", AIPamView.iconSize));
		//	importButton.setGraphic(iconText);
		importButton.setOnAction((action)->{
			aiPamView.openAudioFolder(); 
		});

		textField = new TextField(); 
		textField.setEditable(false);
		textField.prefHeightProperty().bind(importButton.heightProperty());
		textField.setText("Open an audio folder");

		fileImportHolder.getChildren().addAll(textField, importButton);


		//clip length 
		Label clipLength = new Label("Max. Clip Length"); 
		clipLength.setFont(AIPamView.defaultLabelTitle2);
		clipLength.setTextFill(AIPamView.defaultTitleColour);

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
		
		//TODO add decimator and channel options. 
		
		VBox holder = new VBox();
		holder.setSpacing(5); 
		holder.getChildren().addAll(label,fileImportHolder, clipLengthBox ); 
		
		return holder; 
	}

	
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setParams(AIPamParams params) {
		// TODO Auto-generated method stub
		
	}

}
