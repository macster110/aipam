package com.jamdev.maven.aipam.layout.detectionDisplay.waveform;

import com.jamdev.maven.aipam.layout.detectionDisplay.AcousticDataUnit;
import com.jamdev.maven.aipam.layout.detectionDisplay.PAMDataUnit;
import com.jamdev.maven.aipam.layout.detectionDisplay.PlotProjector;
import com.jamdev.maven.aipam.layout.detectionDisplay.TimeDisplayPlot;

import javafx.scene.canvas.GraphicsContext;

/**
 * Waveform plot. 
 * @author Jamie Macaulay
 *
 */
public class WaveformPlot extends TimeDisplayPlot {
	
	
	/**
	 * The raw waveform manager which handles plotting. 
	 */
	RawWavePlotManager rawWavePlotManager =  new RawWavePlotManager();
	
	
	@Override
	public void drawData(GraphicsContext g, PlotProjector projector, int channelMap) {
		rawWavePlotManager.paintData(g, projector, projector.getXAxis().getMinVal(), channelMap, 50); 
	}

	@Override
	public void addData(PlotProjector projector, PAMDataUnit dataUit, int channelBitMap) {
		//System.out.println("Add data: " +  dataUit.getTimeMilliseconds() + " " + dataUit.getDurationInMilliseconds() + " nanonseconds:   " +  ((AcousticDataUnit)dataUit).getTimeNanoseconds());
		rawWavePlotManager.addRawData((AcousticDataUnit) dataUit, channelBitMap, projector, channelBitMap);
	}
	
	@Override
	public void reset() {
		rawWavePlotManager.clear();
		getDataProvider().requestData((long) (this.getPlotProjector().getXAxis().getMinVal()), 
				(long) (this.getPlotProjector().getXAxis().getMaxVal()));  

		reDraw();
	}

}
