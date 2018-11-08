package com.jamdev.maven.aipam.layout.clustering;

import java.util.ArrayList;

import com.jamdev.maven.aipam.clips.PAMClip;
import com.jamdev.maven.aipam.layout.AIPamView;
import com.jamdev.maven.aipam.layout.ColourArray;
import com.jamdev.maven.aipam.layout.clips.PamClipPane;
import com.jamdev.maven.aipam.layout.clips.SpectrogramImage;
import com.jamdev.maven.aipam.layout.utilsFX.ZoomableScrollPane;

import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


/**
 * Pane which shows the position of clips on a 2D dimensional scatter plot.
 * Eventually this could be 3D which  would be pretty cool. 
 * 
 * @author Jamie Macaulay. 
 *
 */
public class ClusterGraphPane extends BorderPane {
	
	/**
	 * Reference to the view. 
	 */
	private AIPamView aiPamView;
	
	/**
	 * The main pane. 
	 */
	private Pane mainPane;

	/**
	 * The line chart
	 */
	private ScatterChart<Number, Number> scatterChart;
	
	/**
	 * The last clicked on node. This prevents needing to iterate through all
	 * points to reset highligted nodes. 
	 */
	private Node lastClicked = null; 
	
	public double specImageSize = 30; 


	public ClusterGraphPane(AIPamView aiPamView) {
		this.aiPamView=aiPamView;
		mainPane = createPane();
		this.setCenter(new ZoomableScrollPane(mainPane));
		//need to set an explicit height if in a scroll pane
		mainPane.setPrefWidth(1000);
		mainPane.setPrefHeight(1000);

	}

	
	/**
	 * Create the pane. 
	 * @return create the pane
	 */
	private Pane createPane() {
		
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        //creating the chart
        scatterChart = 
                new ScatterChart<Number,Number>(xAxis,yAxis);
                
        //scatterChart.setTitle("Clustering");
        
        
        BorderPane mainPane = new BorderPane(); 
        mainPane.setCenter(scatterChart);
        
        
		return mainPane;
	}

	/**
	 * Update the cluster pane. 
	 * @param pamClips - the cluster pane. 
	 */
	public void update(ArrayList<PAMClip> pamClips) {
		
		scatterChart.getData().clear();
		
        XYChart.Series series = new XYChart.Series();
        PamClipPane image;
		for (int i=0; i<pamClips.size(); i++) {
			
			XYChart.Data dataPoint = new XYChart.Data(pamClips.get(i).getClusterPoint()[0],
					 pamClips.get(i).getClusterPoint()[1]);
			
			
			image = new PamClipPane(pamClips.get(i), 30, 30, aiPamView.getCurrentColourArray(), aiPamView.getAIParams().colourLims); 
			image.setSelectionManager(aiPamView.getClipSelectionManager());
//			image = new SpectrogramImage(pamClips.get(i).getSpectrogram(), 
//        			aiPamView.getCurrentColourArray(), aiPamView.getAIParams().colourLims); 
        	//toolTip.setGraphic(new ImageView(image.getSpecImage(100, 100)));
			
////			ImageView imageView = new ImageView(image.getSpecImage(30, 30));
//		    BorderPane imageViewWrapper = new BorderPane(image); //need a wrapper for border effects. 
//		    imageViewWrapper.setStyle("-fx-border-color: transparent; -fx-border-width: 2px;");
//
//		    imageViewWrapper.setOnMouseClicked((event)->{
//				//reset last clicks to transparent. 
//				if (lastClicked!=null) lastClicked.setStyle("-fx-border-color: transparent; -fx-border-width: 2px;");
//
//				//set new one to have highlighted border
//				imageViewWrapper.setStyle("-fx-border-color: ACCENT_COLOR; -fx-border-width: 2px;");
//
//				//brinf to front
//				imageViewWrapper.toFront();
//							    
//				//set as last clicked,. 
//				lastClicked=imageViewWrapper; 
//				
//			});
		    
		    
        	
			dataPoint.setNode(image);
						
			series.getData().add(dataPoint); 
			
//			dataPoint.getNode().setOnMouseClicked((event)->{
//				PopOver popOver = new PopOver(); 
//			});
			 
		}
        scatterChart.getData().add(series);
        
//        Tooltip toolTip; 
//        SpectrogramImage image;
//        for (XYChart.Series<Number, Number> s : scatterChart.getData()) {
//            int n=0;
//            for (XYChart.Data<Number, Number> d : s.getData()) {
//     
//            	toolTip = new Tooltip(
//                        String.format("%2.1f = %2.1f", 
//                                d.getXValue().doubleValue(), 
//                                d.getYValue().doubleValue()));
//            	image = new SpectrogramImage(pamClips.get(n).getSpectrogram(), 
//            			aiPamView.getCurrentColourArray(), aiPamView.getAIParams().colourLims); 
//            	//toolTip.setGraphic(new ImageView(image.getSpecImage(100, 100)));
//            	
//            	d.setNode(new ImageView(image.getSpecImage(40, 40)));
//            	
//            	d.setNode(); 
//
//            	
//            	n++;
//                //Tooltip.install(d.getNode(), toolTip);
//            }
//        }

	}

	/**
	 * Clear the scatter chart. 
	 */
	public void clearGraph() {
		scatterChart.getData().clear();
	}
	
	
 

}
