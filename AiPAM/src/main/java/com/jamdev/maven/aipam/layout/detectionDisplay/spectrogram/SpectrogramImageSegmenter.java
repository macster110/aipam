package com.jamdev.maven.aipam.layout.detectionDisplay.spectrogram;

import com.jamdev.maven.aipam.layout.detectionDisplay.PAMDataUnit;
import com.jamdev.maven.aipam.layout.detectionDisplay.PlotProjector;
import com.jamdev.maven.aipam.layout.detectionDisplay.spectrogram.ScrollingImageSegmenter.WritableImageSegment;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Concrete segmenter for spectrogram images, manages a buffer of SpectrogramImageSegment objects.
 *
 * @author Jamie Macaulay
 */
public class SpectrogramImageSegmenter extends ScrollingImageSegmenter {
	
    private final ArrayList<SpectrogramImageSegment> segments = new ArrayList<>();
    
    private int imageWidth = 1024;
    private int imageHeight = 256;

    public SpectrogramImageSegmenter() {
        super(0, 300);
    }

    public void clear() {
        segments.clear();
    }
    

	/**
	 * Draw the data unit onto a writable image segment. 
	 * @param g - the graphics context the image will be drawn onto
	 * @param pamDataUnit - the data unit. 
	 * @param writableImage - the writable image 
	 * @param tdProjector - the TD  projector for the display
	 * @param scrollStart - the start of the display in milliseconds 
	 */
	public void drawADataUnit(GraphicsContext g, PAMDataUnit  pamDataUnit, WritableImageSegment writableImage, PlotProjector tdProjector, double scrollStart) {
		
//		draw( g,  projector,  scrollStart,  endTimeMillis,  scaleMillisPerPixel)
	}


	/**
	 * Get the polygon for the detection. This allows it to be selected by markers. 
	 * @param g - the graphics context the image will be drawn onto
	 * @param pamDataUnit - the data unit. 
	 * @param writableImage - the writable image 
	 * @param tdProjector - the TD  projector for the display
	 * @param scrollStart - the start of the display in milliseconds 
	 */
	public Path2D getPath2D(PAMDataUnit  pamDataUnit, WritableImageSegment writableImage, PlotProjector tdProjector, double scrollStart) {
		return getClipSpectrogramPath(pamDataUnit.getTimeMilliseconds(), pamDataUnit.getDurationInMilliseconds(),  tdProjector,  scrollStart, pamDataUnit.getSampleRate()
); 
	}

	/**
	 * Get the 2D path for the click spectrogram plot. 
	 * @param timeMilliseconds - the time in millis
	 * @param durationInMilliseconds - the duration in millis
	 * @return the Path2D. 
	 */
	private Path2D getClipSpectrogramPath(double timeMilliseconds, Double durationInMilliseconds, PlotProjector tdProjector, double scrollStart, float sampleRate) {
		
		double tC = tdProjector.getXPix(timeMilliseconds-scrollStart);
		double tCEnd = tdProjector.getXPix(timeMilliseconds+durationInMilliseconds-scrollStart);

		double y1= tdProjector.getYPix(0);
		double y2= tdProjector.getYPix((sampleRate/2)); 

		//		double len = (y1-y2); 
		//		y1 = y2+len*0.2; 
		//		y2 = y2-len*0.2; 

		/**
		 * Bit of a hack but make the selectable path a rectangle in the middle of the FFT so it can be 
		 * slected without having to draw a box right from the top of the frequency axis right to the bottom. 
		 */
		double pix = 0.25*Math.abs(y2-y1); 

		Path2D path2D= new Path2D.Double(0,1); 
		path2D.moveTo(tC, y1-pix);
		path2D.lineTo(tC, y2+pix);
		path2D.lineTo(tCEnd, y2+pix);
		path2D.lineTo(tCEnd, y1-pix);

		//System.out.println("Path2D: get power spectrum" +  tC); 

		return path2D; 
	}

    /**
     * Draw the spectrogram for the given time window and scale.
     */
    public void draw(GraphicsContext g, PlotProjector projector, double startTimeMillis, double endTimeMillis, double scaleMillisPerPixel) {
        for (SpectrogramImageSegment segment : segments) {
            if (segment.getScaleMillisPerPixel() == scaleMillisPerPixel &&
                segment.getStartTimeMillis() + segment.getDurationMillis() > startTimeMillis &&
                segment.getStartTimeMillis() < endTimeMillis) {
                double x = projector.getXPix(segment.getStartTimeMillis());
                double width = projector.getXPix(segment.getStartTimeMillis() + segment.getDurationMillis()) - x;
                g.drawImage(segment.getImage(), x, 0, width, imageHeight);
            }
        }
    }
    
}