package com.jamdev.maven.aipam.layout.detectionDisplay;

import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * A plot showing some data from a detection, e.g. spectrum, spectrogram, waveform etc. 
 * 
 * @author Jamie Macaulay
 *
 */
public abstract class DetectionPlot extends BorderPane {

	private PlotPane plotPane = new PlotPane(); 

	private PlotProjector plotProjector = new PlotProjector(); 
	
	/**
	 * The detection plot parameters. 
	 */
	DetectionPlotParams detectionPlotParams = new DetectionPlotParams();

	private DataProvider dataProvider; 

	public DetectionPlot() {
		this.setCenter(createPlotPane()); 
	}


	public void setDataProvider(DataProvider provider) {
		this.dataProvider=provider; 
		provider.addListener((dataUnit)->{
			newDataAdded(dataUnit); 
		});
	}

	/**
	 * Update the axis. 
	 * @param xminmax - the minimum and maximum x values - can be null.
	 * @param yminmax - the minimum and maximum y values - can be null.
	 * @param zminmax - the minimum and maximum z values - can be null.

	 */
	public void updateAxis(double[] xminmax, double[] yminmax, double[] zminmax) {

		//update the projector. 
		
		if (xminmax!=null) {
			this.plotProjector.getXAxis().setMinVal(xminmax[0]);
			this.plotProjector.getXAxis().setMaxVal(xminmax[1]);
		}

		if (yminmax!=null) {
			this.plotProjector.getYAxis().setMinVal(yminmax[0]);
			this.plotProjector.getYAxis().setMaxVal(yminmax[1]);
		}

		if (zminmax!=null) {
			this.plotProjector.getZAxis().setMinVal(zminmax[0]);
			this.plotProjector.getZAxis().setMaxVal(zminmax[1]);
		}
		
		//now do the GUI bits. 
		reDraw();

	}

	/**
	 * Called whenever the data provider provides new data. 
	 * @param dataUnit - the data unit. 
	 */
	private void newDataAdded(PAMDataUnit dataUnit) {
		//System.out.println("New data added: " + dataUnit + "  " + dataUnit.getSampleRate()); 
		addData(plotProjector, dataUnit, detectionPlotParams.plotBitMap); 
		reDraw();
	}
	
	
	/**
	 * Add data to the plot. This could mean simply setting a reference to the data unit or adding
	 * it to some memory efficient plot store. 
	 * @param projector - the projector. 
	 * @param dataUit - the data unit. 
	 * @param channelBitMap - the channels to plot. 
	 */
	public abstract void addData(PlotProjector projector, PAMDataUnit dataUit, int channelBitMap); 


	/**
	 * Draw the data on the plot  
	 * @param g - the graphics context. 
	 * @param projector - the projector. 
	 * @param dataUnit - the data unit. 
	 * @param the start of the plot scroll time i.e. the axis value at pixel x0. 
	 * @param channelBitMap - the channels to plot. 
	 */
	public abstract void drawData(GraphicsContext g, PlotProjector projector, int channelMap); 
	
	/**
	 * Create the plot pane. 
	 * @return the plot pane. 
	 */
	protected Pane createPlotPane() {

		plotPane = new PlotPane(); 
		plotPane.setPrefSize(300, 200);
		//		plotPane.prefWidthProperty().bind(this.widthProperty()); 
		//		plotPane.prefWidthProperty().bind(this.widthProperty()); 

		//the holder
		BorderPane holder = new BorderPane(); 
		
		plotPane.getPlotCanvas().widthProperty().addListener((obsval, oldVal, newVal)->{
			this.plotProjector.getXAxis().setPixSize(newVal.doubleValue());
			reDraw();
			//plotPane.getPlotCanvas().getGraphicsContext2D().strokeLine(0, 0, Math.random()*50, 50);
		});
		
		plotPane.getPlotCanvas().heightProperty().addListener((obsval, oldVal, newVal)->{
			this.plotProjector.getYAxis().setPixSize(newVal.doubleValue());
			reDraw();
			//plotPane.getPlotCanvas().getGraphicsContext2D().strokeLine(0, 0, 50, 50);
		});
		
//		holder.widthProperty().addListener((obsval, oldVal, newVal)->{
//			System.out.println("Holder width: " + holder.getWidth());
//		});

//		holder.prefWidthProperty().bind(this.widthProperty());
//		holder.prefHeightProperty().bind(this.heightProperty());
				
//		plotPane.getPlotCanvas().widthProperty().bind(holder.widthProperty());
//		plotPane.getPlotCanvas().heightProperty().bind(holder.heightProperty());
		
		Spinner<Double> scrollSpinner = new Spinner<Double>(0.1, 10.0, 1.0, 1.0);
		scrollSpinner.valueProperty().addListener((obsVal, oldVal, newVal)->{
//			System.out.println("Scroll spinner changed!"); 
			this.plotProjector.getXAxis().setPixSize(plotPane.getPlotCanvas().getWidth());
			this.plotProjector.getYAxis().setPixSize(plotPane.getPlotCanvas().getHeight());
			this.plotProjector.getXAxis().setMaxVal((this.plotProjector.getXAxis().getMinVal()+ newVal)*1000.);
			
			dataProvider.requestData((long) (this.plotProjector.getXAxis().getMinVal()), 
					(long) (this.plotProjector.getXAxis().getMaxVal()));  

			reDraw();
		});
		
		Spinner<Double> scrollStartSpinner = new Spinner<Double>(0.0, 10.0, 0.0, 0.1);
		scrollStartSpinner.valueProperty().addListener((obsVal, oldVal, newVal)->{
			
//			System.out.println("Scroll spinner changed!"); 
			this.plotProjector.getXAxis().setPixSize(plotPane.getPlotCanvas().getWidth());
			this.plotProjector.getYAxis().setPixSize(plotPane.getPlotCanvas().getHeight());
			
			double range = this.plotProjector.getXAxis().getMaxVal() - this.plotProjector.getXAxis().getMinVal();
						
			this.plotProjector.getXAxis().setMinVal(newVal*1000);
			this.plotProjector.getXAxis().setMaxVal(newVal*1000 + range);

			System.out.println("Request data between: " + (long) (this.plotProjector.getXAxis().getMinVal()) + "  " + 
					(long) (this.plotProjector.getXAxis().getMaxVal()));
			
			dataProvider.requestData((long) (this.plotProjector.getXAxis().getMinVal()), 
					(long) (this.plotProjector.getXAxis().getMaxVal())); 
			
			reDraw();
		});
		
		
		scrollSpinner.setEditable(true);
		
		HBox topBox = new HBox(); 
		topBox.setSpacing(5);
		topBox.setAlignment(Pos.CENTER_RIGHT);
		
		topBox.getChildren().addAll(new Label("Range"), scrollSpinner, new Label("Start"), scrollStartSpinner); 

		holder.setTop(topBox);
		holder.setCenter(plotPane);

		return holder;
	}

	/**
	 * Reapint the data on the plot. 
	 */
	private void reDraw() {
		drawData(plotPane.getPlotCanvas().getGraphicsContext2D(), plotProjector, detectionPlotParams.plotBitMap); 		
	}




}
