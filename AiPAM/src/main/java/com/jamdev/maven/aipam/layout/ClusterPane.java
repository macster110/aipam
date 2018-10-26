package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.utils.SettingsPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
public class ClusterPane implements SettingsPane<AIPamParams> {
	
	/**
	 * The default 
	 */
	public TSNEClusterPane clusterPane;
	
	private AIPamView aiPamView; 
	
	private Pane mainPane; 
	
	public ClusterPane(AIPamView aiPamView) {
		this.aiPamView = aiPamView;
	}
	
	private Pane createPane() {
		
		Label  titleLabel = new Label("Cluster"); 
		titleLabel.setFont(AIPamView.defaultLabelTitle1);
		titleLabel.setTextFill(AIPamView.defaultTitleColour);
		
		Button button = new Button("Cluster"); 
		button.setOnAction((action)->	{
			aiPamView.getAIControl().clusterClips(); 
		});
		
		
		clusterPane= new TSNEClusterPane(); 
		
		VBox holder = new VBox(); 
		holder.setSpacing(5);
		
		holder.getChildren().addAll(titleLabel, button); 
		
		return holder; 
	}

	
	@Override
	public Pane getPane() {
		if (mainPane==null) {
			mainPane= createPane(); 
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
