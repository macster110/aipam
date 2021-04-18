package com.jamdev.maven.aipam.layout.detectionDisplay;

import java.util.List;

/**
 * Provides data to the plot. Every plot has a data provider. The plot requests
 * data based on it's current axis values and the DataProvider notifies
 * listeners once the data is ready. This could simply be a class that holds
 * some data and instantly sends it on request or something more complicated
 * which requires time to access data.
 * 
 * @author Jamie Macaulay
 *
 */
public interface DataProvider {

	/**
	 * The data provider.
	 * 
	 * @param dataProviderListener
	 */
	public void addListener(DataProviderListener dataProviderListener);

	/**
	 * Remove a DataProviderListener
	 * 
	 * @param dataProviderListener - the listener to remove.
	 * @return
	 */
	public boolean removeListener(DataProviderListener dataProviderListener);

	/**
	 * Get the current list of data listeners.
	 * 
	 * @return
	 */
	public List<DataProviderListener> getDataProviderListeners();

	/**
	 * Request data between two time stamps. Note the the minimum resolution here is
	 * 1 millisecond of data. This can be filtered down further by the plot so all
	 * we are doing here is assuming there will never be a situation where 1
	 * millisecond of data is too much data to send at one time...should be safe for
	 * acoustics
	 * <p>
	 * 
	 * @param start - the start in millis date number.
	 * @param end   - the end in millis date number.
	 */
	public void requestData(double start, double end);

	public DataLimits getDataLimits();

}
