package com.jamdev.maven.aipam.layout.detectionDisplay;

import com.jamdev.maven.aipam.layout.utilsFX.PamAxisFX;
import com.jamdev.maven.aipam.layout.utilsFX.PamAxisPane2;

import javafx.geometry.Side;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;


/**
 * The plot pane contains 4 axis. A top and bottom x axis and a top and bottom y axis. It also contains a canvas 
 * to plot on. 
 * @author Jamie Macaulay
 *
 */
public class PlotPane extends BorderPane {
	
	/**
	 * The x axis which sits at the top of the plot
	 */
	private PamAxisFX xAxisTop;
	
	/**
	 * The x axis which sits at the bottom of the plot
	 */
	private PamAxisFX xAxisBottom;
	
	/**
	 * The y Axis which sits to the left of the plot
	 */
	private PamAxisFX yAxisLeft;
	
	/*
	 *The y axis which sits to right of the plot 
	 */
	private PamAxisFX yAxisRight;

	/**
	 * Holds the canvas for the plot
	 */
	private BorderPane canvasHolder;

	/**
	 * The canvas.
	 */
	private Canvas canvas;

	/**
	 * Pane which holds the top axis.
	 */
	private PamAxisPane2 xAxisTopPane;

	/**
	 * Pane which holds the bottom axis.
	 */
	private PamAxisPane2 xAxisBottomPane;

	/**
	 * Pane which holds the left axis
	 */
	private PamAxisPane2 yAxisLeftPane;

	/**
	 * Pane which holds the right axis. 
	 */
	private PamAxisPane2 yAxisRightPane;

	/**
	 * A pane which holds the top x axis pane and provides suitable corner spaces so horizontal axis does
	 * not overlap with y axis. 
	 */
	private HBox topHolder;

	/**
	 * A pane which holds the bottom x axis pane and provides suitable corner spaces so horizontal axis does
	 * not overlap with y axis. 
	 */
	private HBox bottomHolder;

	/**
	 * Convenience variable, an array with all axis in order, top, right, bottom, left.
	 */
	private PamAxisFX[] axisArray;
	
	/**
	 * Convenience variable, an array with all axis in order, top, right, bottom, left.
	 */
	private PamAxisPane2[] axisPanes; 

	/**
	 * The holder pane for stuff
	 */
	private BorderPane holderPane;
	

	private double topBorder = 5;

	private double rightBorder = 10;

	private double bottomBorder;

	private double leftBorder; 
//	
//	public static final int BOTTOMAXIS = 0; 
//	public static final int BOTTOMAXIS = 1; 
//	public static final int BOTTOMAXIS = 2; 
//	public static final int BOTTOMAXIS = 3; 
//		
	
	/**
	 * Constructs a default plot with an bottom x axis and left y axis. 
	 */
	public PlotPane(){
		this.setCenter(createPlot(false));
	}
	
	
	private BorderPane createPlot(boolean sidePanes){
		
		//create the x axis for the display. 
		xAxisTop = new PamAxisFX(0, 1, 0, 1, 0, 10, PamAxisFX.ABOVE_LEFT, null, PamAxisFX.LABEL_NEAR_CENTRE, null);
		xAxisTop.setCrampLabels(true);
		xAxisTopPane=new PamAxisPane2(xAxisTop, Side.TOP);
		//xAxisTopPane.setOrientation(Orientation.HORIZONTAL);

		xAxisBottom = new PamAxisFX(0, 1, 0, 1, 0, 10, PamAxisFX.BELOW_RIGHT, null, PamAxisFX.LABEL_NEAR_CENTRE, "%4d");
		xAxisBottom.setCrampLabels(true);
		xAxisBottomPane=new PamAxisPane2(xAxisBottom, Side.BOTTOM);
		//xAxisBottomPane.setOrientation(Orientation.HORIZONTAL);

		//create the y axis.
		yAxisLeft = new PamAxisFX(0, 1, 0, 1, 0, 10, PamAxisFX.ABOVE_LEFT, "Graph Y Units", PamAxisFX.LABEL_NEAR_CENTRE, "%4d");
		yAxisLeft.setCrampLabels(true);
		yAxisLeftPane=new PamAxisPane2(yAxisLeft, Side.LEFT);
		//yAxisLeftPane.setOrientation(Orientation.VERTICAL);


		yAxisRight = new PamAxisFX(0, 1, 0, 1, 0, 10, PamAxisFX.BELOW_RIGHT, "Graph Y Units", PamAxisFX.LABEL_NEAR_CENTRE, "%4d");
		yAxisRight.setCrampLabels(true);
		yAxisRightPane=new PamAxisPane2(yAxisRight, Side.RIGHT);
		//yAxisRightPane.setOrientation(Orientation.VERTICAL);

		
		//create the panes to hold the axis; 
		
		//create the plot pane. 
		canvasHolder=new BorderPane();
		
        canvas = new Canvas(50, 50);
        
        //no idea why, but this does not seem to work with height property, only pref height property??
        canvas.heightProperty().bind(canvasHolder.heightProperty());
        canvas.widthProperty().bind(canvasHolder.widthProperty());
        
//        canvasHolder.widthProperty().addListener((obsval, oldval, newVal)->{
//        	System.out.println("HOLDER: " + canvasHolder.getWidth());
//        });
//        
        
        canvasHolder.setCenter(canvas);
        //this is very important to allow resizing
        canvasHolder.setMinHeight(0);
        canvasHolder.setMinWidth(0);
      
        //canvasHolder.getStyleClass().add("pane-plot");

        //now add all axis together
        holderPane=new BorderPane();
 
        //now need to add some corner sections to the top and bottom axis as borderpane is being used
        topHolder=createHorzHolder(xAxisTopPane); 
        bottomHolder=createHorzHolder(xAxisBottomPane); 

        setAxisVisible(true, true, true, true);

        
//        topHolder.toFront();
        //yAxisRightPane.toFront();
//        yAxisLeftPane.toFront();
//        bottomHolder.toFront();
        
        axisArray=new PamAxisFX[4];
        axisArray[0]=xAxisTop;
        axisArray[1]=yAxisRight;
        axisArray[2]=xAxisBottom;
        axisArray[3]=yAxisLeft; 
        
        axisPanes=new PamAxisPane2[4];
        axisPanes[0]=xAxisTopPane;
        axisPanes[1]=yAxisRightPane;
        axisPanes[2]=xAxisBottomPane;
        axisPanes[3]=yAxisLeftPane;
        
        return holderPane;
	}
	
	
	
	/**
	 * Create a hold for horizontal axis panes- needed for border pane. 
	 * @return pane which holds horizontal axis with left and right space for vertical axis. 
	 */
	private HBox createHorzHolder(PamAxisPane2 axisPane){
		HBox horzHolder=new HBox();
		
		Pane leftPane=new Pane();
		//need both min and pref to make binding work properly; 
		leftPane.prefWidthProperty().bind(yAxisLeftPane.widthProperty());
		leftPane.minWidthProperty().bind(yAxisLeftPane.widthProperty());

		Pane rightPane=new Pane();
		rightPane.prefWidthProperty().bind(yAxisRightPane.widthProperty());
		rightPane.minWidthProperty().bind(yAxisRightPane.widthProperty());
		
		horzHolder.getChildren().addAll(leftPane, axisPane, rightPane);
		//axisPane.toFront(); this changes the order of children in a PamHBox. 
	    HBox.setHgrow(axisPane, Priority.ALWAYS);
	     
		//horzHolder.getStyleClass().add("pane");

        return horzHolder; 
		
	}
	

//	public void repaintAxis() {
//		xAxisTopPane.repaint();
//		xAxisBottomPane.repaint();
//		yAxisRightPane.repaint();
//		yAxisLeftPane.repaint();	
//	}

	
	/**
	 * Get the canvas- this is where the plotting takes place. 
	 * @return the plot canvas. 
	 */
	public Canvas getPlotCanvas() {
		return canvas;
	} 
	
	
	/**
	 * Get an axis of the plot pane. 
	 * @param side the axis to get.
	 * @return the PamAxisFX corresponding to the side. 
	 */
	public PamAxisFX getAxis(Side side) {
		switch (side){
		case BOTTOM:
			return xAxisBottom;
		case LEFT:
			return yAxisLeft; 
		case RIGHT:
			return yAxisRight; 
		case TOP:
			return xAxisTop;
		default:
			return null; 
		}
		
	}
	
	
	/**
	 * Get an axis pane. The axis pane is the node which displays a PamAxisFX.
	 * @param side the axis pane to get.
	 * @return the AxisPane corresponding to the side. 
	 */
	public PamAxisPane2 getAxisPane(Side side) {
		switch (side){
		case BOTTOM:
			return xAxisBottomPane; 
		case LEFT:
			return yAxisLeftPane; 
		case RIGHT:
			return yAxisRightPane; 
		case TOP:
			return xAxisTopPane;
		default:
			return null; 
		}
	}
	
	/**
	 * Get all the axis of the plot pane. 
	 * @return a list of axis in the order: TOP, RIGHT, BOTTOM, LEFT. 
	 */
	public PamAxisFX[] getAllAxis() {
		return axisArray;
	}
	
	/**
	 * Get an axis pane
	 * @param side the axis to get. 
	 */
	public PamAxisPane2[] getAllAxisPanes() {
		return axisPanes;
	}
	
	public void setEmptyBorders(double top, double right, double bottom, double left) {
		this.topBorder = top;
		this.rightBorder = right;
		this.bottomBorder = bottom;
		this.leftBorder = left;
	}
	
	/**
	 * Set which axis are visible. 
	 * @param top true to show the top axis
	 * @param right true to show the right axis
	 * @param bottom true to show the bottom axis
	 * @param left true to show the left axis
	 */
	public void setAxisVisible(boolean top, boolean right, boolean bottom,
			boolean left) {
	
		//holderPane.getChildren().clear();
		
		//HACK- 05/08/2016 have to do this because there is a bug in switching children postions in a border pane.
		//casues a duplicate childrne error. 
		holderPane.setRight(null);
		holderPane.setLeft(null);
		holderPane.setTop(null);
		holderPane.setBottom(null);
		holderPane.getChildren().clear();
		//end of HACK. 
		

		if (top) {
			holderPane.setTop(topHolder) ; 
		}
		else if (topBorder > 0) {
//			holderPane.setTopSpace(topBorder);
		}
		if (bottom) {
			holderPane.setBottom(bottomHolder); 
		}
		else if (bottomBorder > 0) {
//			holderPane.setBottomSpace(bottomBorder);
		}
		if (right) {
			holderPane.setRight(yAxisRightPane) ; 
		}
		else if (rightBorder > 0){
//			holderPane.setRightSpace(rightBorder);
		}
		if (left) {
			holderPane.setLeft(yAxisLeftPane) ;
		}
		else if (leftBorder > 0) {
//			holderPane.setLeftSpace(leftBorder);
		}
		holderPane.setCenter(canvasHolder);
		//bottomHolder.toBack();
		
//			this.xAxisTopPane.setVisible(top);
//			this.xAxisBottomPane.setVisible(bottom);
//			this.yAxisRightPane.setVisible(right);
//			this.yAxisLeftPane.setVisible(left);
	}



	/**
	 * @return the xAxisTop
	 */
	public PamAxisFX getxAxisTop() {
		return xAxisTop;
	}


	/**
	 * @return the xAxisBottom
	 */
	public PamAxisFX getxAxisBottom() {
		return xAxisBottom;
	}


	/**
	 * @return the yAxisLeft
	 */
	public PamAxisFX getyAxisLeft() {
		return yAxisLeft;
	}


	/**
	 * @return the yAxisRight
	 */
	public PamAxisFX getyAxisRight() {
		return yAxisRight;
	}


	public void getPlotHolder() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	

}
