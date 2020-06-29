package com.jamdev.maven.aipam.layout;

import org.controlsfx.control.ToggleSwitch;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.AITheme;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import com.jamdev.maven.aipam.layout.utilsFX.SettingsPane;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * General settings pane for the program
 * 
 * @author Jamie Macaulay
 *
 */
public class GeneralSettingsPane extends DynamicSettingsPane<AIPamParams> {

	private AIPamView aiPamView;
	
	private VBox mainPane;

	private ToggleSwitch showPromptsSwitch; 

	public GeneralSettingsPane(AIPamView aiPamView) {
		this.aiPamView=aiPamView; 
		
		Label title = new Label("General Settings"); 
		title.getStyleClass().add("label-title1");

		
		Label theme = new Label("Choose theme"); 
		theme.getStyleClass().add("label-title2");
		
		final ToggleGroup group = new ToggleGroup();

		RadioButton radioButtonLight = new RadioButton("Light"); 
		radioButtonLight.setToggleGroup(group);
		radioButtonLight.setOnAction((action)->{
			aiPamView.setTheme(AITheme.JMETRO_LIGHT_THEME); 
		});
		RadioButton radioButtonDark = new RadioButton("Dark"); 
		radioButtonDark.setToggleGroup(group);
		radioButtonDark.setOnAction((action)->{
			aiPamView.setTheme(AITheme.JMETRO_DARK_THEME); 
		});
		radioButtonDark.setSelected(true);
		
		//user options ot display all clips as features. 
		showPromptsSwitch = new ToggleSwitch(); 
		showPromptsSwitch.selectedProperty().addListener((obs, oldVal, newVal)->{
			notifySettingsListeners();
		});
//		showPromptsSwitch.setMaxWidth(100);

		Label label = new Label("Show user prompts");
		//label.getStyleClass().add("label-title2");
		HBox hBox = new HBox(); 
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setSpacing(5);
		hBox .getChildren().addAll(showPromptsSwitch, label); 

		mainPane = new VBox(); 
		mainPane.setSpacing(15);
		mainPane.getChildren().addAll(title, theme, radioButtonLight, radioButtonDark, hBox); 
		
		
		
	}
	
	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public AIPamParams getParams(AIPamParams paramsIn) {
		paramsIn.showUserPrompts = showPromptsSwitch.isSelected(); 
		return paramsIn;
	}

	@Override
	public void setParams(AIPamParams params) {
		showPromptsSwitch.setSelected(params.showUserPrompts);
	}

	@Override
	public Node getIcon() {
		FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.GEARS); 
		iconView.setGlyphSize(AIPamView.iconSize);
		iconView.setFill(Color.WHITE);
		return iconView;
	}

	@Override
	public String getTitle() {
		return "General Settings";
	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// TODO Auto-generated method stub
		
	}

}
