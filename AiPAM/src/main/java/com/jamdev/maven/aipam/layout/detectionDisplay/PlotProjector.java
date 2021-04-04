package com.jamdev.maven.aipam.layout.detectionDisplay;

/**
 * Handles the conversion between data values and pixels. 
 * @author Jamie Macaulay 
 *
 */
public class PlotProjector  {
	
	/**
	 * The x axis 
	 */
	SimpleAxis xAxis = new SimpleAxis(); 
	
	/**
	 * The y axis
	 */
	SimpleAxis yAxis = new SimpleAxis(); 
	
	/**
	 * The z axis
	 */
	SimpleAxis zAxis = new SimpleAxis(); 
	

	/**
	 * The plot projector. 
	 * @param axisValue
	 * @return
	 */
	public double getXPix(double axisValue) {
		return xAxis.getPixelValue(axisValue);
	}
	
	/**
	 * 	Get the y pix. 
	 * @param axisValue
	 * @return
	 */
	public double getYPix(double axisValue) {
		//System.out.println("Get Y axis pixel value: " + axisValue + " yAxisPix: " + yAxis.getPixSize());
		return yAxis.getPixelValue(axisValue);

	}

	public double getZPix(double axisValue) {
		return zAxis.getPixelValue(axisValue);
	}
	
	public double getXPixSize() {
		return xAxis.getPixSize();
	}
	
	public double getYPixSize() {
		return yAxis.getPixSize();
	}
	
	public double getZPixSize() {
		return zAxis.getPixSize();
	}
	
	public SimpleAxis getXAxis() {
		return xAxis;
	}

	public void setXAxis(SimpleAxis xAxis) {
		this.xAxis = xAxis;
	}

	public SimpleAxis getYAxis() {
		return yAxis;
	}

	public void setYAxis(SimpleAxis yAxis) {
		this.yAxis = yAxis;
	}

	public SimpleAxis getZAxis() {
		return zAxis;
	}

	public void setZAxis(SimpleAxis zAxis) {
		this.zAxis = zAxis;
	}

	
	/**
	 * Stores information and has basic conversion functions between axis and pixel values. 
	 * @author Jamie Macaulay 
	 *
	 */
	public class SimpleAxis {
		
	
		/**
		 * The minimum value fo the axis (not pixels)
		 */
		public double minVal = 0; 
		
		/**
		 * The maximum value of the axis (not pixels)
		 */
		public double maxVal = 1; 
		
		
		/**
		 * The number of pixels
		 */
		public double pixSize = 100; 
		
		
		/**
		 * Get the value in pixels for an axis value
		 * @param axisValue - the axis value
		 * @return the corresponding pixel value. 
		 */
		public double getPixelValue(double axisValue) {
			//System.out.println("Get pixel value: " + axisValue);
			return pixSize*((axisValue-minVal)/(maxVal-minVal)); 
		}
		
		/**
		 * Get the axis value
		 * @param pixelValue
		 * @return
		 */
		public double getAxisValue(double  pixelValue) {
			return ((maxVal-minVal)*(pixelValue/pixSize))+minVal; 
		}
		
		/**
		 * Get the minimum axis value /(maxVal-minVal). 
		 * @return the minimum axis value. 
		 */
		public double getMinVal() {
			return minVal;
		}

		/**
		 * The the minimum axis value (not the pixel value). 
		 * @param minVal - the minimum axis value. 
		 */
		public void setMinVal(double minVal) {
			this.minVal = minVal;
		}

		/**
		 * Get the maximum axis value (not the pixel value)
		 * @return - the maximum axis value. 
		 */
		public double getMaxVal() {
			return maxVal;
		}

		/**
		 * Set the maximum axis value (not the pixel value). 
		 * @param maxVal - the maximum axis value to set. 
		 */
		public void setMaxVal(double maxVal) {
			this.maxVal = maxVal;
		}
		
		/**
		 * Get the number of pixels long the axis is 
		 * @return the number of pixels long the axis is. 
		 */
		public double getPixSize() {
			return pixSize;
		}

		/**
		 * Set the number of pixels long the axis is 
		 * @param pixSize = the number of pixels to srt. 
		 */
		public void setPixSize(double pixSize) {
			this.pixSize = pixSize;
		}


	}



	

}
