package com.jamdev.maven.aipam.layout.detectionDisplay.spectrogram;

import com.jamdev.maven.aipam.layout.detectionDisplay.PAMDataUnit;
import com.jamdev.maven.aipam.layout.detectionDisplay.PlotProjector;
import com.jamdev.maven.aipam.layout.detectionDisplay.TimeDisplayPlot;

import javafx.scene.canvas.GraphicsContext;

/**
 * The spectrogram plot. 
 * 
 * @author  Jamie Macaulay 
 *
 */
public class SpectrogramPlot extends TimeDisplayPlot {

    private final SpectrogramImageSegmenter segmenter = new SpectrogramImageSegmenter();

    @Override
    public void addData(PlotProjector projector, PAMDataUnit dataUit, int channelBitMap) {
        // Delegate to the segmenter
        //segmenter.addData(dataUit, projector);
    }

    @Override
    public void drawData(GraphicsContext g, PlotProjector projector, int channelMap) {
        // Draw the visible spectrogram using the segmenter
        double startTimeMillis = projector.getXAxis().getMinVal();
        double endTimeMillis = projector.getXAxis().getMaxVal();
        double scaleMillisPerPixel = (endTimeMillis - startTimeMillis) / g.getCanvas().getWidth();
        segmenter.draw(g, projector, startTimeMillis, endTimeMillis, scaleMillisPerPixel);
    }

    public void reset() {
        segmenter.clear();
        reDraw();
    }
}