package com.jamdev.maven.aipam.layout.detectionDisplay.scroller;

import java.util.ArrayList;

import com.jamdev.maven.aipam.layout.detectionDisplay.DetectionPlot;
import com.jamdev.maven.aipam.layout.detectionDisplay.PlotProjector;

/**
 * The detection plot scroll bar pane.
 * 
 * @author Jamie Macaulay
 *
 */
public class DetectionPlotScrollBar extends ScrollBarPane {
	
	/**
	 * Plot projectors. 
	 */
	ArrayList<DetectionPlot> detectionPlots = new ArrayList<DetectionPlot>(); 
	
	
	public DetectionPlotScrollBar(){
		this.currentValueProperty().addListener((obsVal, oldVal, newVal)->{
			valueChanged();
		});
		
		this.visibleAmountProperty().addListener((obsVal, oldVal, newVal)->{
			valueChanged();
		});
	}
	
	/**
	 * Called whenever the current value or the visible property value changes. 
	 */
	public void valueChanged() {
		for (int i=0; i<detectionPlots.size() ; i++) {
			detectionPlots.get(i).scrollChanged(currentValueProperty().get(), visibleAmountProperty().get()); 
		}
	}
	
	/**
	 * Add plot projector. 
	 * @param detectionPlot - the plot projector.
	 */
	public void addDetectionPlot(DetectionPlot detectionPlot) {
		detectionPlots.add(detectionPlot);
	}
	
	/**
	 * Remove plot projector. 
	 * @param detectionPlot - the plot projector. 
	 * @return true if the plot projector. 
	 */
	public boolean removeDetectionPlot(DetectionPlot detectionPlot) {
		return detectionPlots.remove(detectionPlot);
	}

}
