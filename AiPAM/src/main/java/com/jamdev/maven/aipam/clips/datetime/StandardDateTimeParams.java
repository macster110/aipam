package com.jamdev.maven.aipam.clips.datetime;

import java.util.TimeZone;

/**
 * Date time settings. Copied from PAMGuard. 
 * @author Jamie Macaulay
 *
 */
public class StandardDateTimeParams implements DateTimeParams  {

	public static final long serialVersionUID = 1L;

	//		public long timeOffset;
	private String timeZoneName = "UTC";

	private boolean adjustDaylightSaving = false;

	private long additionalOffsetMillis = 0;

	private transient TimeZone currentZone;

	private boolean forcePCTime = false;

	private boolean useBespokeFormat;

	/**
	 * format to force the type of date string. otherwise it's automatic 
	 */
	private String forcedDateFormat;

	/**
	 * Date-time format to use.  Currently only used when reading Soundtrap XML files
	 */
	private String dateTimeFormatToUse = ""; 


	/**
	 * Get the current time offset for the given date. 
	 * @param date raw data extracted from time string
	 * @return adjustment to make for time zone. 
	 */
	public long getTimeOffset(long date) {
		if (adjustDaylightSaving) {
			return additionalOffsetMillis - getCurrentZone().getOffset(date);
		}
		else {
			return additionalOffsetMillis - getCurrentZone().getRawOffset();
		}
	}

	public String getTimeZoneName() {
		return timeZoneName;
	}

	public void setTimeZoneName(String timeZoneName) {
		this.timeZoneName = timeZoneName;
		setCurrentZone(timeZoneName);
	}

	private TimeZone getCurrentZone() {
		if (currentZone == null) {
			setCurrentZone(timeZoneName);
		}
		return currentZone;
	}

	private void setCurrentZone(String timeZoneName) {
		currentZone = TimeZone.getTimeZone(timeZoneName);
		if (currentZone == null) {
			currentZone = TimeZone.getTimeZone("UTC");
		}
	}

	public boolean isAdjustDaylightSaving() {
		return adjustDaylightSaving;
	}

	public void setAdjustDaylightSaving(boolean adjustDaylightSaving) {
		this.adjustDaylightSaving = adjustDaylightSaving;
	}

	/**
	 * @return the additionalOffsetMillis
	 */
	public long getAdditionalOffsetMillis() {
		return additionalOffsetMillis;
	}

	/**
	 * @param additionalOffsetMillis the additionalOffsetMillis to set
	 */
	public void setAdditionalOffsetMillis(long additionalOffsetMillis) {
		this.additionalOffsetMillis = additionalOffsetMillis;
	}

	public String getDateTimeFormatToUse() {
		return dateTimeFormatToUse;
	}

	public void setDateTimeFormatToUse(String customDateTimeFormat) {
		this.dateTimeFormatToUse = customDateTimeFormat;
	}

	public boolean isForcePCTime() {
		return forcePCTime;
	}

	public void setForcePCTime(boolean forcePCTime) {
		this.forcePCTime = forcePCTime;
	}

	/**
	 * @return the forcedDateFormat
	 */
	public String getForcedDateFormat() {
		return forcedDateFormat;
	}

	/**
	 * @param forcedDateFormat the forcedDateFormat to set
	 */
	public void setForcedDateFormat(String forcedDateFormat) {
		this.forcedDateFormat = forcedDateFormat;
	}

	/**
	 * @return the useBespokeFormat
	 */
	public boolean isUseBespokeFormat() {
		return useBespokeFormat;
	}

	/**
	 * @param useBespokeFormat the useBespokeFormat to set
	 */
	public void setUseBespokeFormat(boolean useBespokeFormat) {
		this.useBespokeFormat = useBespokeFormat;
	}

}
