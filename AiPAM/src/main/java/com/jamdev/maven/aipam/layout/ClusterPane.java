package com.jamdev.maven.aipam.layout;

import com.jamdev.maven.aipam.AIPamParams;
import com.jamdev.maven.aipam.utils.SettingsPane;

import javafx.scene.layout.Pane;

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
	
	public TSNEClusterPane clusterPane; 
	
	public ClusterPane() {
		
	}

	@Override
	public Pane getPane() {
		if (clusterPane==null) {
			clusterPane= new TSNEClusterPane(); 
		}
		return clusterPane.getPane();
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
