package com.jamdev.maven.aipam.layout;



import com.jamdev.maven.aipam.layout.utilsFX.UtilsFX;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.utils.MaterialDesignIconFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
	 * Import acoustic button
	 */
	private Button importAcoustic;

	/**
	 * The cluster button
	 */
	private Button cluster;

	/**
	 * Browse to clips
	 */
	private Button browseButton; 
	
	public MasterControlPane(AIPamView aiPamView) {
		this.aiPamView = aiPamView; 
		this.mainPane = createPane(); 
		
	}
	
	private Pane createPane() {
		
		VBox holder = new VBox(); 
		
		//main controls, import clips and cluster 
		
		Label labelHome = new Label("Controls");
		labelHome.setPadding(new Insets(5,5,5,5));
		labelHome.getStyleClass().add("label-title1");
		AIPamView.setButtonIcon(labelHome, FontAwesomeIcon.HOME); 
		
		
		browseButton = new Button("Browse..."); 
		browseButton.getStyleClass().add("fluent-menu-button");
		AIPamView.setButtonIcon(browseButton, FontAwesomeIcon.FOLDER_OPEN_ALT); 
		browseButton.prefWidthProperty().bind(holder.widthProperty());
		browseButton.setTooltip(new Tooltip(
				"Start importing clips"));
		
		browseButton.setOnAction((action)->{
			aiPamView.browseAndCheckAudio();
		});

		
		importAcoustic = new Button("Import Clips"); 
		importAcoustic.getStyleClass().add("fluent-menu-button");
		AIPamView.setButtonIcon(importAcoustic, FontAwesomeIcon.TH); 
		importAcoustic.prefWidthProperty().bind(holder.widthProperty());
		importAcoustic.setTooltip(new Tooltip(
				"Start importing clips"));
		
		importAcoustic.setOnAction((action)->{
			aiPamView.importAcoustic(); 
		});

		
		cluster = new Button("Cluster Clips"); 
		cluster.getStyleClass().add("fluent-menu-button");
		ImageView icon = UtilsFX.whitenImage(new ImageView(aiPamView.getClusterIcon())); 
		cluster.setGraphic(icon);
		cluster.setAlignment(Pos.CENTER_LEFT);
		cluster.setGraphicTextGap(15);
		cluster.prefWidthProperty().bind(holder.widthProperty());
		cluster.setTooltip(new Tooltip(
				"Start the clustering algorithm"));
		
		cluster.setOnAction((action)->{
			aiPamView.cluster();
		});
		
		
		HBox controlBox = new HBox(); 
		controlBox.prefWidthProperty().bind(holder.widthProperty());
		controlBox.getChildren().addAll(importAcoustic, cluster); 
		
		//holder.setSpacing(5);
		holder.getChildren().addAll(labelHome, browseButton,  importAcoustic, cluster); 
		
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
