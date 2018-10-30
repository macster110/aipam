package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.layout.utilsFX.DynamicSettingsPane;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Pane for clustering algorithms.
 * <p>
 * Currently this pane simply holds the TSNE algorithm settings. 
 * If there were another clustering algorithm then this pane 
 * would allows users to select algorithms and change the settings
 * controls appropriately.
 * 
 * @author Jamie Macaulay 
 *
 */
public class ClusterPane extends DynamicSettingsPane<AIPamParams> {
	
	/**
	 * The default 
	 */
	public TSNEClusterPane clusterPane;
	
	private AIPamView aiPamView; 
	
	private Pane mainPane; 
	
	public ClusterPane(AIPamView aiPamView) {
		this.aiPamView = aiPamView;
		mainPane= createPane(); 

	}
	
	private Pane createPane() {
		
		Label  titleLabel = new Label("Cluster"); 
		titleLabel.getStyleClass().add("label-title1");
		
		clusterPane= new TSNEClusterPane(); 
		
		VBox holder = new VBox(); 
		holder.setSpacing(5);
		
		holder.getChildren().addAll(titleLabel); 
		
		return holder; 
	}

	
	@Override
	public Pane getPane() {
		return mainPane;
	}

	@Override
	public AIPamParams getParams(AIPamParams paramsIn) {
		// TODO Auto-generated method stub
		return paramsIn;
	}

	@Override
	public void setParams(AIPamParams params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node getIcon() {
		//get the cluster image 
		ImageView icon = new ImageView(aiPamView.getClusterIcon()); 
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(1);
		icon.setEffect(colorAdjust);
		
		FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.GEARS); 
		iconView.setGlyphSize(8);
		iconView.setFill(Color.WHITE);
		
		StackPane stackPane = new StackPane(); 
		
		stackPane.getChildren().addAll(icon, iconView); 
		StackPane.setAlignment(iconView, Pos.TOP_RIGHT);
		
		return stackPane;
	}

	@Override
	public String getTitle() {
		return "Cluster Algorithm";
	}

	@Override
	public void notifyUpdate(int flag, Object stuff) {
		// TODO Auto-generated method stub
		
	}


}
