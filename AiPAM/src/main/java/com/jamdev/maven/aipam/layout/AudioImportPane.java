package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AiPamController;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Pane with controls for importing audio data.
 * 
 * @author Jamie Macaulay
 *
 */
public class AudioImportPane extends DynamicSettingsPane<AIPamParams> {
	private VBox mainPane;
	private ComboBox<String> audioFormatComboBox;
	private Button importButton;
	private Label titleLabel;
	private AIPamView aiPamView;

	public AudioImportPane(AiPamController controller) {
		mainPane = new VBox();
		mainPane.setSpacing(10);

		titleLabel = new Label("Import Audio Files");
		FontIcon iconView = new FontIcon("fa-file-audio");
		iconView.setIconSize(24);
		iconView.setIconColor(Color.DARKCYAN);
		titleLabel.setGraphic(iconView);
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		audioFormatComboBox = new ComboBox<>();
		audioFormatComboBox.getItems().addAll("WAV", "MP3", "FLAC");
		audioFormatComboBox.getSelectionModel().selectFirst();
		audioFormatComboBox.setTooltip(new Tooltip("Select audio format"));

		importButton = new Button("Import");
		FontIcon importIcon = new FontIcon("fa-folder-open");
		importIcon.setIconSize(18);
		importIcon.setIconColor(Color.DARKCYAN);
		importButton.setGraphic(importIcon);
		importButton.setOnAction(e -> {
			// Add import logic here
		});

		mainPane.getChildren().addAll(titleLabel, audioFormatComboBox, importButton);
	}

	public AudioImportPane(AIPamView aiPamView) {
		this.aiPamView = aiPamView;
		mainPane = new VBox();
		mainPane.setSpacing(10);

		titleLabel = new Label("Import Audio Files");
		FontIcon iconView = new FontIcon("fa-file-audio-o");
		iconView.setIconSize(24);
		iconView.setIconColor(Color.DARKCYAN);
		titleLabel.setGraphic(iconView);
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		audioFormatComboBox = new ComboBox<>();
		audioFormatComboBox.getItems().addAll("WAV", "MP3", "FLAC");
		audioFormatComboBox.getSelectionModel().selectFirst();
		audioFormatComboBox.setTooltip(new Tooltip("Select audio format"));

		importButton = new Button("Import");
		FontIcon importIcon = new FontIcon("fa-folder-open");
		importIcon.setIconSize(18);
		importIcon.setIconColor(Color.DARKCYAN);
		importButton.setGraphic(importIcon);
		importButton.setOnAction(e -> {
			// Add import logic here
		});

		mainPane.getChildren().addAll(titleLabel, audioFormatComboBox, importButton);
	}

	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public AIPamParams getParams(AIPamParams paramsIn) {
		// Example: set selected format
		return paramsIn;
	}

	@Override
	public void setParams(AIPamParams params) {
		// Example: set combo selection from params
	}

	@Override
	public Node getIcon() {
		FontIcon iconView = new FontIcon("fa-file-audio-o");
		iconView.setIconSize(24);
		iconView.setIconColor(Color.DARKCYAN);
		return iconView;
	}

	@Override
	public String getTitle() {
		return "Audio Import";
	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// Implement if needed
	}

	@Override
	public String getDescription() {
		return "Import audio files into the application.";
	}
}
