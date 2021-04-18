package com.jamdev.maven.aipam.clips.datetime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SoundSpotCalendar {
	

	public static TimeZone getDisplayTimeZone(boolean useLocal) {
		return TimeZone.getTimeZone("UTC");
		//		return useLocal ? CalendarControl.getInstance().getChosenTimeZone() : defaultTimeZone;
	}
	
	/**
	 * Format a time string optionally showing the milliseconds with
	 * a given precision for UTC time zone
	 * @param timeMillis time in milliseconds
	 * @param millisDigits number of millisecond decimal places. 
	 * @return formatted time string. 
	 */
	public static String formatTime(long timeMillis, boolean showMillis, boolean useLocal) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeMillis);
		c.setTimeZone(getDisplayTimeZone(useLocal));
		String formatString = "HH:mm:ss";
		if (showMillis) {
			formatString += ".SSS";
		}
		DateFormat df = new SimpleDateFormat(formatString);
		df.setTimeZone(getDisplayTimeZone(useLocal));
		Date d = c.getTime();
		return df.format(d);
	}
	
	
	/**
	 * Format a time string in the format HH:MM:SS
	 * @param timeMillis time in milliseconds
	 * @return formatted string
	 */
	public static String formatTime(long timeMillis, boolean showMills) {
		return formatTime(timeMillis, showMills, false);
	}
	

	/**
	 * Format a time string optionally showing the milliseconds with
	 * a given precision.  The time string is formatted as HH:mm:ss.SSSSS.
	 * @param timeMillis time in milliseconds
	 * @param millisDigits number of millsecond decimal places.
	 * @return formatted time string.
	 */
	public static String formatTime(long timeMillis, int millisDigits) {
		return formatTime(timeMillis, millisDigits, false);
	}
	
	/**
	 * Format a time string optionally showing the milliseconds with
	 * a given precision.  The time string is formatted as HH:mm:ss.SSSSS.
	 * @param timeMillis time in milliseconds
	 * @param millisDigits number of millsecond decimal places.
	 * @return formatted time string.
	 */
	public static String formatTime(long timeMillis, int millisDigits, boolean useLocal) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeMillis);
		c.setTimeZone(getDisplayTimeZone(useLocal));
		String formatString = "HH:mm:ss";
		if (millisDigits > 0) {
			formatString += ".";
			for (int i=1; i<=millisDigits; i++) {
				formatString += "S";
			}
		}
		DateFormat df = new SimpleDateFormat(formatString);
		df.setTimeZone(getDisplayTimeZone(useLocal));
		Date d = c.getTime();
		return df.format(d);
	}

	/**
	 * Format a time string optionally showing the milliseconds with
	 * a given precision.  The time string is formatted as HHmmss.SSSSS.
	 * @param timeMillis time in milliseconds
	 * @param millisDigits number of millsecond decimal places.
	 * @return formatted time string.
	 */
	public static String formatTime2(long timeMillis, int millisDigits, boolean useLocal) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeMillis);
		c.setTimeZone(getDisplayTimeZone(useLocal));
		String formatString = "HHmmss";
		if (millisDigits > 0) {
			formatString += ".";
			for (int i=1; i<=millisDigits; i++) {
				formatString += "S";
			}
		}
		DateFormat df = new SimpleDateFormat(formatString);
		df.setTimeZone(getDisplayTimeZone(useLocal));
		Date d = c.getTime();
		return df.format(d);
	}



	/**
	 * Format a time in milliseconds as a number of days / seconds, etc. 
	 * @param timeInMillis time in milliseconds. 
	 * @return formatted time interval
	 */
	public static String formatDuration(long timeInMillis) {
		return formatDuration(timeInMillis, " day ", " days ");
	}

	public static String formatDuration(long timeInMillis, String middleString) {
		return formatDuration(timeInMillis, middleString, middleString);
	}

	public static String formatDuration(long timeInMillis, String middleString1, String middleString2) {
		long aDay = 3600 * 24 * 1000;
		if (timeInMillis < 60000) {
			return String.format("%.3fs", timeInMillis/1000.);
		}
		if (timeInMillis < aDay) {
			return formatTime(timeInMillis, false);
		}
		long days = (int) Math.floor(timeInMillis / aDay);
		long millis = timeInMillis - days * aDay;
		if (days == 1) {
			return String.format("%d%s%s", days, middleString1, formatTime(millis, false));
		}
		else {
			return String.format("%d%s%s", days, middleString2, formatTime(millis, false));
		}
	}
	
	
}