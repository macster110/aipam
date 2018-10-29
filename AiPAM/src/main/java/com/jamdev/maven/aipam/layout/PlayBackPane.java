package com.jamdev.maven.aipam.layout;

import org.controlsfx.glyphfont.FontAwesome;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.utils.SettingsPane;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Pane with controls for  audio playback settings. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class PlayBackPane extends BorderPane implements SettingsPane<AIPamParams> {
	
	private Slider volume;
	
	
	private Label volumeIconLabel; 
	
	private Label volumePercLabel;

	/**
	 * Reference to AIPamView
	 */
	private AIPamView aiPamView; 

	
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
		volumeTitle.getStyleClass().add("label-title1");


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
	public AIPamParams getParams(AIPamParams paramsIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParams(AIPamParams params) {
		// TODO Auto-generated method stub
		
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

}
