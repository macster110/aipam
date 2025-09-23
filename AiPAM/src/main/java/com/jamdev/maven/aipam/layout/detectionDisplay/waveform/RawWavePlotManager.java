package com.jamdev.maven.aipam.layout.detectionDisplay.waveform;

import java.awt.geom.Path2D;

import com.jamdev.maven.aipam.layout.detectionDisplay.AcousticDataUnit;
import com.jamdev.maven.aipam.layout.detectionDisplay.PAMDataUnit;
import com.jamdev.maven.aipam.layout.detectionDisplay.PlotProjector;
import com.jamdev.maven.aipam.layout.detectionDisplay.waveform.ScrollingPlotSegmenter.PlotSegment;
import com.jamdev.maven.aipam.utils.AiPamUtils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Manages plotting raw data.
 * <p>
 * The raw segmenter holds a 2D array of data for a segment of a plot for each
 * channel. Each plot segment keeps a record of which data units have been
 * added. This allows the segments to be reused for plotting without having to
 * iterate through data units again.
 * <p>
 * An example use case is plotting raw waveform data. At high sample rates,
 * each raw bin can only be plotted as the minimum and maximum bin withing a
 * time segment representing a single pixel. iterating through all raw data bins
 * and calculating this each time a plot needs repainting is processor
 * intensive. So the bin data can be processed and added to PlotSegments, then
 * the PlotSegments can be reused until either the plot scale time scale changes
 * sufficiently that new segments are required (e.g. zooming in) or the plot
 * moves to a completely new time section of data.
 * 
 * @author Jamie Macaulay
 *
 */
public class RawWavePlotManager {

	/**
	 * The maximum samples per pixel. Below this raw data is just stored. 
	 */
	private static final double SAMPLES_PER_PIXEL_CUTOFF = 10;

	/**
	 * The current detection
	 */
	private AcousticDataUnit detection;

	/**
	 * The raw data to plot. 
	 */
	private double fftLineWidth = 1; 

	/**
	 * The width of the circle if plotting samples. 
	 */
	private double circleWidth = 3; 

	/**
	 * Image segmented for fast drawing of FFT's
	 */
	private RawWavPlotsegmenter[] waveImageSegmenter = new RawWavPlotsegmenter[32];

	/**
	 * Raw wave plot manager. 
	 * @param rawClipInfo -0 the raw clip. 
	 */
	public RawWavePlotManager() {

	}

	/**
	 * Reset the image buffer. 
	 */
	public void clear() {
		for (int i=0; i<waveImageSegmenter.length; i++) {
			if (waveImageSegmenter[i]!=null) {
				waveImageSegmenter[i].resetImageBuffer();
			}
		}
	}


	/**
	 * Draw the raw data on the image. 
	 * @param rawData - the raw data to draw. 
	 * @param timeMilliseconds - the time in milliseconds. 
	 * @param plotSegment - the writable image. 
	 */
	private void addRawData(double[] rawData, double timeMilliseconds, long timeNano, float sR,  PlotSegment plotSegment, Color color) {

		System.out.println("Add raw data: " + rawData + " timeMilliseconds: " + timeMilliseconds + "  plot segment start: " + plotSegment.getMillisStart()); 

		//the way we do this is we apply a start sample
		//the problem with PAMGuard in this instance is that the time in millis is a very rough measure of stuff at high sample rates. 	
		double samplesPerPixel = (plotSegment.getScrollingPLot2DSegmenter().getMillisePerPixel()/1000.)*sR; 

		//		System.out.println("Add raw data sample: len: " +rawData.length+  "  sample: " +  rawData[0] + " samplesPerPixel: " 
		//		+ samplesPerPixel + "plot segment start millis : " + plotSegment.getMillisStart() + " end millis: " 
		//				+  plotSegment.getMillisEnd() + " timeMilliseconds: " + timeMilliseconds); 

		/**
		 * OK this is a bit of a hack. here's the issue. We use the start of the plot segment as the time reference to keep everything 
		 *time aligned. That works well, except if there is a new wav file - the sample and nano times change and thus the times all mess up. It's not easy to 
		 *know where the start of the new wav file was so this is a problem 
		 */

		double timeDiffSamples; 

		//we take two different approaches depending on whether we are very zoomed or not. We hope that no one is using ultra small 
		//continuous wav files... this is a little hackey but can't think of a better way
		//		if (samplesPerPixel<2.0) {
		//			timeDiffSamples = (double) sR*(timeNano - plotSegment.getExtraInfo().longValue()) / 1.e9;
		//		}
		//		else {
		timeDiffSamples = sR*(timeMilliseconds - plotSegment.getMillisStart()) / 1.e3;
		//		}

		//we draw a line of the minimum and maximum pixels			
		int drawPixel =  (int) ((timeDiffSamples)/samplesPerPixel); 

		int drawStart = drawPixel;
		//		
				System.out.println("Add raw data: " + timeMilliseconds
						+ " drawPixel: " + drawPixel +  "  samplesPerPixel: " + samplesPerPixel + " plot segment: " + plotSegment.getPixelStart());

		if (samplesPerPixel<SAMPLES_PER_PIXEL_CUTOFF) {
			//System.out.println("ONE PIXEL");
			//simply save one data point for the raw data. 
			for (int i=0; i<rawData.length; i++) {

				drawPixel =  (int) ((timeDiffSamples+i)/samplesPerPixel); 

				if (i==0) {
					System.out.println("First draw pixel: "+ drawPixel + "  data len: " + plotSegment.getData().length + " y " + rawData[i] + " plotSegment: " + plotSegment); 
				}

				if (drawPixel>=0 && drawPixel<plotSegment.getData().length) {
					//System.out.println("Draw a pixel: "+ drawPixel + " rawData: " + rawData[i] + " plot segment: " + + plotSegment.getPixelStart()); 
					plotSegment.getData()[drawPixel][0] = (float) rawData[i];
					plotSegment.getColor()[drawPixel] = color; 

				}
			}

			//			System.out.println("Last draw pixel: " 
			//					+ " drawPixel: " + drawPixel);

		}
		else {
			//the amplitude limits 
			double minX = Double.MAX_VALUE; 
			double maxX= -Double.MAX_VALUE; 

			//now bin the
			int count = 0; 
			for (int i=0; i<rawData.length; i++) {
				
				
				//had to do this because the count>samples pixel ignores the cumulative remainder and this does not work well. 
				//TODO - does this need fixed in PG?
				if (((int) Math.floor(i/samplesPerPixel))>(drawPixel-drawStart)) {


					//				if (count>=samplesPerPixel) {	//FIXME - issue here with rounding...
					if (drawPixel>=0 && drawPixel<plotSegment.getData().length) {
						//add the previous pixel to the plot segment. 

						//it's possible the same pixel will drawn on more than once. in this case must ensure that values are added if current value <max or >min . 
						if (Float.isNaN(plotSegment.getData()[drawPixel][0]) || plotSegment.getData()[drawPixel][0]>minX) {
							plotSegment.getData()[drawPixel][0] = (float) minX;
						}
						if (Float.isNaN(plotSegment.getData()[drawPixel][1]) || plotSegment.getData()[drawPixel][1]<maxX) {
							plotSegment.getData()[drawPixel][1] = (float) maxX;
						}
						if (plotSegment.getColor()[drawPixel]!=null) {
							plotSegment.getColor()[drawPixel] = color;//blend((Color) plotSegment.getColor()[drawPixel], color); 
						}
						else {
							plotSegment.getColor()[drawPixel] = color; 
						}
					}

					//move to the next pixel and reset min max
					minX = Double.MAX_VALUE; 
					maxX= -Double.MAX_VALUE; 
					drawPixel++; 

					count = 0; 
				}

				if (rawData[i]>maxX ) maxX = rawData[i]; 
				if (rawData[i]<minX ) minX = rawData[i]; 

				count++; 	
			}

			if (drawPixel>=0 && drawPixel<plotSegment.getData().length) {
				//System.out.println("Draw a pixel: " + drawPixel); 
				//add the previous pixel to the plot segment. 
				//it's possible the same pixel will drawn on more than once. in this case must ensure that values are added if current value <max or >min . 
				if (Float.isNaN(plotSegment.getData()[drawPixel][0]) || plotSegment.getData()[drawPixel][0]>minX) {
					plotSegment.getData()[drawPixel][0] = (float) minX;
				}
				if (Float.isNaN(plotSegment.getData()[drawPixel][1]) || plotSegment.getData()[drawPixel][1]<maxX) {
					plotSegment.getData()[drawPixel][1] = (float) maxX;
				}
				if (plotSegment.getColor()[drawPixel]!=null) {
					plotSegment.getColor()[drawPixel] = color;//blend((Color) plotSegment.getColor()[drawPixel], color); 
				}
				else {
					plotSegment.getColor()[drawPixel] = color; 
				}
			}
		}
	}



	/**
	 * Implementation of the writable image segmented. 
	 * 
	 * @author Jamie Macaulay
	 *
	 */
	class RawWavPlotsegmenter extends ScrollingPlotSegmenter {



		/**
		 * The relative channel to plot (index within the channel group, not the absolute channel)
		 */
		private int chanClick;



		private float sampleRate;

		//		int count =0; 

		public RawWavPlotsegmenter(int chanClick) {
			super(); 
			this.chanClick = chanClick; //which click channel to plot. int count 
		}


		@Override
		public synchronized void paintPlotData(GraphicsContext g, PlotSegment plotSegmentData, PlotProjector tdProjector,
				double scrollStart) {

			//paint the raw data on the display. this is just a series of lines representing the sound data. 
			//System.out.println("Ok, paint the raw wave data: !");  
			if (g==null) {
				System.err.println("paintPlotData: GraphicsContext g is null"); 
				return; 
			}

			g.setLineWidth(fftLineWidth);

			double y1, y2, x1, x2, prevx1, prevy1; 


			double tcMillis = plotSegmentData.getMillisStart();

			//System.out.println("scrollStart: " + scrollStart + "  plotSegmentData.getMillisStart() " + plotSegmentData.getMillisStart() );

			double samplesPerPixel = (plotSegmentData.getScrollingPLot2DSegmenter().getMillisePerPixel()/1000.)*sampleRate;

			//System.out.println("PP: Plot Segment millis start: " +  plotSegmentData.getMillisStart() + "  scrollStart: " + scrollStart + " First segment: " + plotSegmentData.getData()[0][0]);

			prevx1=-1; 
			prevy1=-1; 
			double lastMillis =-1.; 
			for (int i =0; i<plotSegmentData.getData().length; i++) {				

				//how many pixels to draw on the line. 
				y1=tdProjector.getYPix(plotSegmentData.getData()[i][0]); 
				//y1= Math.random()*100; 
				//								if (Double.isNaN(y1)) {
				//									System.out.println("Temp Y is NaN"); 
				//									y1= Math.random()*100; 
				//								}

				g.setStroke(plotSegmentData.getColor()[i]);
				g.setFill(plotSegmentData.getColor()[i]);

				//start pixel

				x1=tdProjector.getXPix((tcMillis + i*millisPerPixel)); 

				//				//TEMP
				//				if (i==1) {
				//					System.out.println("PLOT DOT: " + x1 + " " + y1);
				//					g.fillOval(x1-9/2, y1-9/4, 9, 9);
				//				}
				//				
				//				
				//				if (Double.isNaN(y1)) {
				//					y1=-1; 
				//				}
				//								

				//				if (i%500==0) {
				//					System.out.println("paintPlotData: x1: " + x1 +  " y1: " + y1 + " millisPerPixel: " + millisPerPixel +  "  tcMillis: "  + (tcMillis + i*millisPerPixel)  + "  n samples: " + plotSegmentData.getData().length ); 
				//				}

				//				g.strokeLine(0, 0, 50, 50); //TEMP

				if (x1>=0  && x1<=tdProjector.getXAxis().getPixSize()) {
					
					
					//System.out.println("x1: " + x1 + " val (seconds) "  + (tcMillis + i*millisPerPixel)/1000. + " y1: " + y1 + "  index: " + i); 

					//System.out.println("x1: " + x1 +  " y1: " + y1 + " millisPerPixel: " + millisPerPixel +  "  tcMillis: "  +tcMillis  ); 
					if (samplesPerPixel<SAMPLES_PER_PIXEL_CUTOFF && !Double.isNaN(y1)) {
						//System.out.println("x1: " + x1 +  " y1: " + y1 + " millisPerPixel: " + millisPerPixel +  "  tcMillis: "  +tcMillis  ); 

						if (samplesPerPixel<1) {
							g.fillOval(x1-circleWidth/2, y1-circleWidth/4, circleWidth, circleWidth);

						}

						if (prevx1>0) {
							g.strokeLine(prevx1, prevy1, x1, y1);
						}
						prevx1 = x1; 
						prevy1 = y1; 

					}
					else {
						//lower y1 value. 
						y2=tdProjector.getYPix(plotSegmentData.getData()[i][1]); 
						//end pixel
						lastMillis=  (tcMillis + (i+1)*millisPerPixel); 

						x2=tdProjector.getXPix((tcMillis + (i+1)*millisPerPixel));

						//System.out.println("x1:" +x1 +" x2: " +  x2 + " y1: " + y1 + " y2: " + y2); 
						//plot the data chunk
						for (double j =x1; j<x2; j++) {
							g.strokeLine(j, y1, j, y2);
						}
			
					}
				}
			}

			//			System.out.println("plotSegmentData millis last:  " + PamCalendar.formatDBDateTime((long) plotSegmentData.getMillisEnd(), true) 
			//			+ " last drawn: " + PamCalendar.formatDBDateTime((long) (lastMillis+scrollStart), true) + " Millis per pixel: " + millisPerPixel); 

			//			//FIXME
			//			// for testing - plors where the windows are. 
			
			int firstNonNan = -1; 
			for (int i=0; i<plotSegmentData.getData().length; i++) {
				if (!Double.isNaN(plotSegmentData.getData()[i][0])){
					firstNonNan = i; 
					break; 
				}
			}
			
//			System.out.println("tcMillis: " +  tcMillis + " plot segment: " + plotSegmentData.getPixelStart() 
//			+ " tcMillis End: " + (tcMillis + plotSegmentData.getWidth()*millisPerPixel)/1000.  + " First non NaN:  " + firstNonNan); 
			double tC = tdProjector.getXPix(tcMillis);
			double tCEnd = tdProjector.getXPix((tcMillis + plotSegmentData.getWidth()*millisPerPixel));
			y1=tdProjector.getYPix(-1); 
			y2=tdProjector.getYPix(1); 
			g.setStroke(Color.ORANGE);
			g.strokeLine(tC, y1, tC, y2);
			g.setStroke(Color.RED);
			g.strokeLine(tCEnd-2, y1, tCEnd-2, y2);
			g.strokeText(" Pixels: " + plotSegmentData.getPixelStart() + " tCEnd: " + tCEnd, tC+10, y1+10); 


		}

		/**
		 * Adds plot data to the store. In this case this is waveform data. 
		 */
		@Override
		public void addPlotData(PAMDataUnit pamDataUnit, PlotSegment plotSegmentData, PlotProjector tdProjector,
				double scrollStart) {

			this.sampleRate = ((AcousticDataUnit) pamDataUnit).getSampleRate(); 
			//the writable image covers the entire frequency spectrum.
			RawWavePlotManager.this.addRawData((AcousticDataUnit) pamDataUnit,  plotSegmentData,
					tdProjector,  scrollStart, chanClick, Color.DODGERBLUE); 
			//it is plotted automatically on the graph.
		}


	}


	/**
	 * Draw the raw data. 
	 * @param acousticDataUnit - the PAM data unit.
	 * @param g - the graphics context. 
	 * @param scrollStart - the scroll start. 
	 * @param tdProjector - the tdProjector for converting data values to pixel positions. 
	 * @param type - the type of drawing. E.g. HIGHLIGHT
	 * @return the Path2D which describes the data unit. 
	 */
	public Path2D addRawData(AcousticDataUnit acousticDataUnit, double scrollStart,
			PlotProjector tdProjector, int plotChannels) {


		double timeMillis=acousticDataUnit.getTimeMilliseconds();

		//get position on time axis
		//double tC = tdProjector.getXPix((timeMillis-scrollStart));

		//		System.out.println("addRawData: tc: "+tC+"  timeMillis" + timeMillis + " scrollStart: " 
		//		+ scrollStart + " (timeMillis-scrollStart)/1000. "+((timeMillis-scrollStart)/1000.));
		//		if (tC < 0 || tC>tdProjector.getXAxis().getPixSize()) {
		//			System.out.println("addRawData: outside tc: " + tC +  " " + tdProjector.getXAxis().getPixSize()); 
		//			return null;
		//		}

		//cycle through the  number of channels the detection contains. 
		detection=acousticDataUnit; 
		//double maxFreq=rawClipInfo.getDataBlock().getSampleRate()/2; 

		int[] chanClick=AiPamUtils.getChannelArray(detection.getChannelBitmap());

		//System.out.println("rawClipInfo.getTDGraph().getCurrentScaleInfo().getPlotChannels(): " + rawClipInfo.getTDGraph().getCurrentScaleInfo().getPlotChannels()[plotNumber]); 
		//g.setLineWidth(fftLineWidth);
		//System.out.println("RawWavePlotManager: chanPlot: " + chanClick[0] + "  chanplot: " + chanPlot[0]);
		Path2D path2D = null; 
		//draw click spectrum
		for (int i=0; i<chanClick.length; i++){

			if (waveImageSegmenter[chanClick[i]]==null) {
				waveImageSegmenter[chanClick[i]] = new RawWavPlotsegmenter(i);
			}		

			//System.out.println("Ok, Add plot data: !");  

			path2D = waveImageSegmenter[chanClick[i]].addPlotData(acousticDataUnit, tdProjector, scrollStart);

			//					//now draw the data units if they have been marked
			//					if (type==TDSymbolChooserFX.HIGHLIGHT_SYMBOL_MARKED || type==TDSymbolChooserFX.HIGHLIGHT_SYMBOL) {
			//						g.setLineWidth(2);
			//						drawHighlightedRaw( g,  AcousticDataUnit,  tdProjector,  scrollStart, type, chanPlot[j]); 
			//					}

			//waveImageSegmenter[chanClick[i]].paintImages(null, tdProjector, scrollStart, i);
		}
		return path2D;
	}



	/**
	 * Draw the data unit. 
	 * @param g = the graphics context.  
	 * @param acousticDataUnit - the raw data unit
	 * @param plotSegment - the plot segment containing data to plot. 
	 * @param tdProjector - the td projector.  
	 * @param scrolLStart - the scroll data. 
	 * @param chanClick - the channle clicks. 
	 */
	private void addRawData(AcousticDataUnit acousticDataUnit, PlotSegment plotSegment,
			PlotProjector tdProjector, double scrollStart, int chanClick, Color color) {

		//we assume that the waveform starts at the start time and that the bins are the sample rate of the datablock. 
		double[][] rawData = AiPamUtils.short2double(acousticDataUnit.getAudioData()); 
		/**
		 * 
		 * Do not use start samples as does not work with short wav files. 
		 */
		//		//lt's make sure that we assign a start sample to the start of the writable image. 
		//		if (plotSegment.getExtraInfo()==null) {
		//			//need to set the start sample of the writable image. This means that all data units are referenced using samples 
		//			//instead if millis and so at when zoomed will be correctly drawn with respect to each other. 
		//
		//			///number of samples from the start of the writable image to the start of the data unit. 
		//			double samples = (AcousticDataUnit.getParentDataBlock().getSampleRate()/1000.)*(AcousticDataUnit.getTimeMilliseconds()-plotSegment.getMillisStart());
		//			
		//			Double imageStartSample = AcousticDataUnit.getStartSample()-samples; 
		//			plotSegment.setExtraInfo(imageStartSample);
		//		}

		//lt's make sure that we assign a nanosecond to the start of the writable image. 
		if (plotSegment.getExtraInfo()==null) {
			//need to set the start sample of the writable image. This means that all data units are referenced using samples 
			//instead if millis and so at when zoomed will be correctly drawn with respect to each other. 

			///number of samples from the start of the writable image to the start of the data unit. 
			long nanoSecondsFromStart = (long) ((acousticDataUnit.getTimeMilliseconds()-plotSegment.getMillisStart())*1000*1000);

			Long imageStartNano = acousticDataUnit.getTimeNanoseconds()- nanoSecondsFromStart; 
			plotSegment.setExtraInfo(imageStartNano);
		}

		if (rawData==null) {
			return; 
		}

		addRawData(rawData[chanClick], acousticDataUnit.getTimeMilliseconds(), acousticDataUnit.getTimeNanoseconds(),
				acousticDataUnit.getSampleRate(), plotSegment, color); 

	}

	/**
	 * Paint the image data on the plot
	 * @param g - the graphics context. 
	 * @param projector - the projector. 
	 * @param scrollStart - the scroll start 
	 * @param channelMap
	 * @param tm
	 */
	public void paintData(GraphicsContext g, PlotProjector projector, double scrollStart, int channelMap, long tm) {

		int[] chans = AiPamUtils.getChannelArray(channelMap);
		//System.out.println("paintData: channelMap: " + channelMap + " num chans: " + chans.length + " wavesegmenter: " + waveImageSegmenter[0] ); 
		for (int j=0; j<chans.length; j++) {
			for (int i=0; i<waveImageSegmenter.length; i++) {
				if (waveImageSegmenter[i]!=null && i==chans[j]) {
					//System.out.println("Paint images:"); 
					waveImageSegmenter[i].paintImages(g, projector, scrollStart, tm);
				}
			}
		}

	}
}

