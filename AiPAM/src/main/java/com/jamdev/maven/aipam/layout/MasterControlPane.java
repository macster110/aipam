package com.jamdev.maven.aipam.layout;



import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.utils.MaterialDesignIconFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 
 * Pane with the some of the main controls e.g. import data and cluster data
 * 
 * @author Jamie Macaulay
 *
 */
public class MasterControlPane {
	
	private Pane mainPane;
	
	private AIPamView aiPamView;

	/**
	 * Import settings button
	 */
	private Button importSettings;

	/**
	 * Save settings button
	 */
	private Button saveSettings;

	/**
	 * Import acoustic button
	 */
	private Button importAcoustic;

	/**
	 * The cluster button
	 */
	private Button cluster; 
	
	public MasterControlPane(AIPamView aiPamView) {
		this.aiPamView = aiPamView; 
		this.mainPane = createPane(); 
		
	}
	
	private Pane createPane() {
		
		VBox holder = new VBox(); 
		
		//main controls, import clips and cluster 
		
		Label labelHome = new Label("Home");
		labelHome.setPadding(new Insets(5,5,5,5));
		labelHome.getStyleClass().add("label-title1");
		AIPamView.setButtonIcon(labelHome, FontAwesomeIcon.HOME); 

		
		importAcoustic = new Button(""); 
		importAcoustic.getStyleClass().add("fluent-menu-button");
		AIPamView.setButtonIcon(importAcoustic, FontAwesomeIcon.TH, Pos.CENTER); 
		importAcoustic.prefWidthProperty().bind(holder.widthProperty());
		importAcoustic.setTooltip(new Tooltip(
				"Start importing clips"));
		
		importAcoustic.setOnAction((action)->{
			aiPamView.importAcoustic(); 
		});

		
		cluster = new Button(""); 
		cluster.getStyleClass().add("fluent-menu-button");
		ImageView icon = new ImageView(aiPamView.getClusterIcon()); 
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(1);
		icon.setEffect(colorAdjust);
		cluster.setGraphic(icon);
		cluster.setAlignment(Pos.CENTER);
		cluster.setGraphicTextGap(15);
		cluster.prefWidthProperty().bind(holder.widthProperty());
		cluster.setTooltip(new Tooltip(
				"Start the clusterring algorithm"));
		
		cluster.setOnAction((action)->{
			aiPamView.cluster();
		});
		
		saveSettings = new Button(""); 
		saveSettings.getStyleClass().add("fluent-menu-button");
		AIPamView.setButtonIcon(saveSettings, FontAwesomeIcon.SAVE, Pos.CENTER); 
		saveSettings.prefWidthProperty().bind(holder.widthProperty());
		saveSettings.setTooltip(new Tooltip(
				"Save a settings file. This can be opened iby a new instance of the \n"
				+ "program to restore the current settings."));
		
		importSettings = new Button(""); 
		importSettings.getStyleClass().add("fluent-menu-button");
		AIPamView.setButtonIcon(importSettings, FontAwesomeIcon.FOLDER_OPEN_ALT, Pos.CENTER); 
		importSettings.prefWidthProperty().bind(holder.widthProperty());
		importSettings.setTooltip(new Tooltip(
				"Import settings from a .mat settings file."));
		
		HBox saveBox = new HBox(); 
		saveBox.prefWidthProperty().bind(holder.widthProperty());
		saveBox.getChildren().addAll(saveSettings, importSettings); 
		
		HBox controlBox = new HBox(); 
		controlBox.prefWidthProperty().bind(holder.widthProperty());
		controlBox.getChildren().addAll(importAcoustic, cluster); 
		
		//holder.setSpacing(5);
		holder.getChildren().addAll(labelHome, saveBox, controlBox); 
		
		//holder.setAlignment(Pos.BASELINE_LEFT);

		return holder; 
	}
	
	/**
	 * 
	 * Create a Glyph icon which can be used as graphics for various controls. 
	 * @param icon - the icon to add
	 * @param size - the size of the icon in font size.
	 * @return the icon in a form to add to a control. 
	 */
	public static Text createPamGlyph(FontAwesomeIcon icon, int size){
		Text text2 = FontAwesomeIconFactory.get().createIcon(icon); 
		FontAwesomeIconView iconViewImport = new FontAwesomeIconView(icon); 

		//text2.getStyleClass().add("glyph-icon");
		//text2.setStyle(String.format("-fx-font-family: %s; -fx-font-size: %dpt;", icon.fontFamily(), size));
		return iconViewImport;
	}
	
	/**
	 * Create a standard Glyph from any Icon library. 
	 * @param icon - the icon,
	 * @return the icon glyph as a Text node. 
	 */
	public static Text createPamGlyph(GlyphIcons icon){
		Text text2 = null; 
		if (icon instanceof FontAwesomeIcon){
			return text2 = FontAwesomeIconFactory.get().createIcon(icon); 
		}
		if (icon instanceof MaterialDesignIcon){
			return text2 = MaterialDesignIconFactory.get().createIcon(icon); 
		}
//		if (icon instanceof MaterialIcon){
//			return text2 = MaterialIconFactory.get().createIcon(icon); 
//		}
//		if (icon instanceof OctIcon){
//			return text2 = OctIconFactory.get().createIcon(icon); 
//		}
//		if (icon instanceof Icons525){
//			return text2 = Icon525Factory.get().createIcon(icon); 
//		}
//		if (icon instanceof WeatherIcon){
//			return text2 = WeatherIconFactory.get().createIcon(icon); 
//		}
		return text2; 
	}

	
	public Pane getPane() {
		// TODO Auto-generated method stub
		return mainPane;
	}


	
	

}
