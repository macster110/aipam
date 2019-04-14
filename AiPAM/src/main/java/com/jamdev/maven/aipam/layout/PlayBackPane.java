package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Pane with controls for  audio playback settings. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class PlayBackPane extends DynamicSettingsPane<AIPamParams> {
	
	private Slider volume;
	
	private Label volumeIconLabel; 
	
	private Label volumePercLabel;

	/**
	 * Reference to AIPamView
	 */
	private AIPamView aiPamView;

	/**
	 * The main holder pane. 
	 */
	private VBox mainPane;


	private Button playAllButton;


	private Button stopButton; 

	
	/**
	 * Constructor for the playback pane.
	 * @param aiPamView
	 */
	public PlayBackPane(AIPamView aiPamView){
		this.aiPamView = aiPamView;
		
		volumeIconLabel = new Label();
		volumeIconLabel.getStyleClass().add("label-title2");


		volumePercLabel = new Label(); 
		volumePercLabel.getStyleClass().add("label-title2");
		
		volume= new Slider(0.,100.,50.); 
		volume.setValue(0.5);
		volume.valueProperty().addListener((oldval, newVal, obsVal)->{
			setIconGraphic(); 
			setLabelText(); 
		});
		
		setIconGraphic(); 
		setLabelText(); 
			
		HBox holder = new HBox();
		holder.setSpacing(5); 
		holder.getChildren().addAll(volumeIconLabel, volume, volumePercLabel);
		holder.setAlignment(Pos.CENTER);
		HBox.setHgrow(volume, Priority.ALWAYS);
		
		Label volumeTitle = new Label("Volume");
		volumeTitle.getStyleClass().add("label-title1");


		mainPane = new VBox(); 
		mainPane.setSpacing(5); 
		mainPane.getChildren().addAll(volumeTitle, holder, allClipPlaybackPane() );
	}
	
	/**
	 * Create a pane which plays back all clips. 
	 * @return a pane which plays back all clips. 
	 */
	private Pane allClipPlaybackPane() {
		
		Label label = new Label("Play all clips");
		label.getStyleClass().add("label-title2");
		
		playAllButton = new Button(); 
		playAllButton.setOnAction((action)->{
			aiPamView.getClipSelectionManager().autoPlayClips(null);
		});
		playAllButton.setTooltip(new Tooltip("Play all the clips from the start"));
		MaterialDesignIconView iconView = new MaterialDesignIconView(MaterialDesignIcon.PLAY); 
		iconView.setGlyphSize(33);
		iconView.setFill(Color.WHITE);
		playAllButton.setGraphic(iconView);
		playAllButton.disableProperty().bind(aiPamView.getClipSelectionManager().autoPlayProperty());

		stopButton = new Button(); 
		stopButton.setTooltip(new Tooltip("Stop auto playback of clips"));
		stopButton.setOnAction((action)->{
			aiPamView.getClipSelectionManager().stopClipAutoPlay();
		});
		iconView = new MaterialDesignIconView(MaterialDesignIcon.STOP); 
		iconView.setGlyphSize(33);
		iconView.setFill(Color.WHITE);
		stopButton.setGraphic(iconView);
		stopButton.disableProperty().bind(aiPamView.getClipSelectionManager().autoPlayProperty().not());
	
		
		HBox playBackPane = new HBox(); 
		playBackPane.setSpacing(5);
		playBackPane.getChildren().addAll(playAllButton, stopButton); 
		
		volumeIconLabel.setGraphic(iconView);

		VBox holder = new VBox(); 
		holder.setSpacing(5);
		holder.getChildren().addAll(label, playBackPane); 
		
		return holder; 
	}

	/**
	 * Set the label text depending on the volume
	 */
	private void setLabelText() {
		// TODO Auto-generated method stub
		volumePercLabel.setText(String.format("%.0f", volume.getValue())+"%"); 
	}

	/**
	 * Set the label graphic depending on the volume. 
	 */
	private void setIconGraphic() {
		MaterialDesignIcon icon = MaterialDesignIcon.VOLUME_OFF; 
		
		if (volume.getValue()>0 && volume.getValue()<=25) {
			icon=MaterialDesignIcon.VOLUME_LOW; 
		}
		if (volume.getValue()>25 && volume.getValue()<75) {
			icon=MaterialDesignIcon.VOLUME_MEDIUM; 
		}
		if (volume.getValue()>75) {
			icon=MaterialDesignIcon.VOLUME_HIGH; 
		}
		
		MaterialDesignIconView iconView = new MaterialDesignIconView(icon); 
		iconView.setGlyphSize(33);
		iconView.setFill(Color.WHITE);
		
		volumeIconLabel.setGraphic(iconView);
	}

	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public AIPamParams getParams(AIPamParams paramsIn) {
		paramsIn.volume = this.volume.getValue();
		return paramsIn;
	}

	@Override
	public void setParams(AIPamParams params) {
		volume.setValue(params.volume);
	}

	@Override
	public Node getIcon() {
		FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.PLAY); 
		iconView.setGlyphSize(AIPamView.iconSize);
		iconView.setFill(Color.WHITE);
		return iconView;
	}

	@Override
	public String getTitle() {
		return "Playback";
	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * The volume property. 
	 * @return the volume property. 
	 */
	public DoubleBinding volumeProperty() {
		return this.volume.valueProperty().divide(100);
	}

}
