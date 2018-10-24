package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.utils.SettingsPane;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

/**
 * Pane ith controls for audio playback. 
 * @author Jamie Macaulay 
 *
 */
public class PlayBackPane extends BorderPane implements SettingsPane<PLayBackParams> {
	
	private Slider volume;
	
	
	private Label volumeIconLabel; 
	
	private Label volumePercLabel; 

	
	/**
	 * Constructor for the playback pane.
	 * @param aiPamView
	 */
	public PlayBackPane(AIPamView aiPamView){
		
		volumeIconLabel = new Label();
		volumeIconLabel.setTextFill(AIPamView.defaultTitleColour);
		volumeIconLabel.setFont(AIPamView.defaultLabelTitle2);

		volumePercLabel = new Label(); 
		volumePercLabel.setTextFill(AIPamView.defaultTitleColour);
		volumePercLabel.setFont(AIPamView.defaultLabelTitle2);
		
		volume= new Slider(0.,1.,0.5); 
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
		volumeTitle.setTextFill(AIPamView.defaultTitleColour);
		volumeTitle.setFont(AIPamView.defaultLabelTitle1);

		this.setTop(volumeTitle);
		this.setCenter(holder);
		
	}

	/**
	 * Set the label text depending on the volume
	 */
	private void setLabelText() {
		// TODO Auto-generated method stub
		volumePercLabel.setText(String.format("%.0f", volume.getValue()*100)+"%"); 
	}

	/**
	 * Set the label graphic depending on the volume. 
	 */
	private void setIconGraphic() {
		MaterialDesignIcon icon = MaterialDesignIcon.VOLUME_OFF; 
		
		if (volume.getValue()>0 && volume.getValue()<=0.25) {
			icon=MaterialDesignIcon.VOLUME_LOW; 
		}
		if (volume.getValue()>0.25 && volume.getValue()<0.75) {
			icon=MaterialDesignIcon.VOLUME_MEDIUM; 
		}
		if (volume.getValue()>0.75) {
			icon=MaterialDesignIcon.VOLUME_HIGH; 
		}
		
		MaterialDesignIconView iconView = new MaterialDesignIconView(icon); 
		iconView.setGlyphSize(33);
		iconView.setFill(Color.WHITE);
		
		volumeIconLabel.setGraphic(iconView);
	}

	@Override
	public Pane getPane() {
		return this;
	}

	@Override
	public PLayBackParams getParams(PLayBackParams paramsIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParams(PLayBackParams params) {
		// TODO Auto-generated method stub
		
	}

}
