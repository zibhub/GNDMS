package de.zib.gndms.infra.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;





public final class ISO8601 {

	public enum datetimeFormat{
		
		YEAR,
		YEAR_MONTH,
		YEAR_MONTH_DAY,
		DATE_HM,
		DATE_HMS,
		DATE_HMSMS;		
	}

	private static Map<datetimeFormat,SimpleDateFormat> ISO08601format=null;

	private static TimeZone zeroZone=TimeZone.getTimeZone("GMT+00:00");
	private static TimeZone myZone=TimeZone.getDefault();
	
	
	//Splits a time-String into an Array 
	//so that a time like '12:14:04.12' will result in [12,14,4,12] 
	private static final int[] getTimeEntries(String str) {
		int j = 0;
		int milSec = 0;
		String[] values = new String[4];
		values = str.split(":");
		if (values.length > 2 && values[2].contains(".")) {
			j = values[2].indexOf(".");
			milSec = Integer.valueOf(values[2].substring(j + 1));
			values[2] = values[2].substring(0, j);
		}
		int[] time = new int[4];
		for (int i = 0; i < values.length; i++) {
			time[i] = Integer.valueOf(values[i]);
		}
		time[3] = milSec;

		return time;
	}
	
	//sets all values of the calendar to zero ,
	//	parses a given String in ISO08601-format, and applies it to the calendar.
	public static final Calendar parseISO08601Date(Calendar cal,
			String dateformat) throws java.text.ParseException {
		dateformat = dateformat.trim();
		cal.clear();
		return parseISO08601DateNoInit(cal, dateformat);
	}
	
	// Parses a given String in ISO08601-format and applies it to a given calendar.
	// Pay attention that the calendar will not have been reset before the string is applied.
	// So it will take the current date and time as it's values and will overwrite only the
	// these values which are written in the string.
	public static final Calendar parseISO08601DateNoInit(Calendar cal,
			String dateformat) throws ParseException {
		if (ISO08601format == null) {
			initMap();
		}
		try {

			// SPLITTING DATE and TIME
			String timeStr = "";
			boolean fullFormat = false;
			if (dateformat.contains("T")) {
				String[] tmp = dateformat.split("T");
				if (tmp.length > 2)
					throw new ParseException(dateformat, 0);
				dateformat = tmp[0];
				timeStr = tmp[1];
				fullFormat = true;
			}
			boolean inPast = false;
			if (dateformat.charAt(0) == '-') {
				dateformat = dateformat.substring(1);
				inPast = true;
			}

			String[] dateval = dateformat.split("-");

			String zone = "";
			int[] timeEntries = null;
			if (dateval.length > 4 || (fullFormat && dateval.length != 3))
				throw new ParseException(timeStr, 0);
			//SPLITTING TIME and OFFSET
			if (timeStr.length() > 0) {

				if (timeStr.contains("+") || timeStr.contains("-")) {

					int j = timeStr.lastIndexOf('+');
					if (j == -1)
						j = timeStr.lastIndexOf('-');
					else if (timeStr.lastIndexOf("-") > -1)
						throw new ParseException(timeStr, 0);
					zone = timeStr.substring(j, timeStr.length());
					timeStr = timeStr.substring(0, j);
				}
				if (timeStr.contains("Z")) {
					timeStr = timeStr.substring(0, timeStr.length() - 1);
					zone = "00:00";
				}
				timeEntries = getTimeEntries(timeStr);
			}
			// SETTING TIMEZONE
			if (!zone.equals("")) {
				cal.setTimeZone(TimeZone.getTimeZone("GMT" + zone));
			} else {
				cal.setTimeZone(TimeZone.getDefault());
			}
			// SETTING DATE-VALUES
			cal.set(Calendar.YEAR, Integer.valueOf((dateval[0])));
			for (int i = 0; i < dateval.length - 1; i++) {
				cal.set(i + 2 + 2 * (i % 2), Integer.valueOf(dateval[i + 1])
						+ (i - 1));
			}
			// SETTING TIME-VALUES
			if (timeEntries != null) {
				cal.set(Calendar.HOUR_OF_DAY, timeEntries[0]);
				cal.set(Calendar.MINUTE, timeEntries[1]);
				cal.set(Calendar.SECOND, timeEntries[2]);
				cal.set(Calendar.MILLISECOND, timeEntries[3]);
			}
			if (inPast) {
				cal.set(Calendar.ERA, 0);
			} else {
				cal.set(Calendar.ERA, 1);
			}
		} catch (NumberFormatException e) {
			throw new ParseException(e.getMessage(), 0);
		}
		return cal;
	}
	
	public static final String toISO8601Date(datetimeFormat m, Calendar cal) {
		return toISO8601Date(m, false, cal);
	}

	public static final String toISO8601Date(datetimeFormat m,
			boolean normalizedTZ, Calendar cal) {

		SimpleDateFormat t= ISO08601format.get(m);
		if (normalizedTZ){
			t.setTimeZone(zeroZone);
		}
		StringBuffer output = new StringBuffer(t.format(cal.getTimeInMillis()));

		if (output.charAt(0) == 'A') {
			output.delete(0, 2);
		} else {
			output.replace(0, 2, "-");
		}
		if (normalizedTZ) t.setTimeZone(myZone);
		return output.toString();
	}
	
	
	//initializes several format-types according to ISO08601
	public static void initMap(){
		ISO08601format=new HashMap<datetimeFormat, SimpleDateFormat>();
		ISO08601format.put(datetimeFormat.YEAR, new SimpleDateFormat("GGyyyy",Locale.ENGLISH));
		ISO08601format.put(datetimeFormat.YEAR_MONTH, new SimpleDateFormat("GGyyyy-MM",Locale.ENGLISH));
		ISO08601format.put(datetimeFormat.YEAR_MONTH_DAY, new SimpleDateFormat("GGyyyy-MM-dd",Locale.ENGLISH));
		ISO08601format.put(datetimeFormat.DATE_HM, new SimpleDateFormat("GGyyyy-MM-dd'T'HH:mmZ",Locale.ENGLISH));
		ISO08601format.put(datetimeFormat.DATE_HMS, new SimpleDateFormat("GGyyyy-MM-dd'T'HH:mm:ssZ",Locale.ENGLISH));
		ISO08601format.put(datetimeFormat.DATE_HMSMS, new SimpleDateFormat("GGyyyy-MM-dd'T'hh:mm:ss.SZ",Locale.ENGLISH));
	}
	
	
	public static void main(String[] args) {

		String test="2008-06-06T14:20:14+06:00";
		Calendar cal=Calendar.getInstance();
		try{
			cal=parseISO08601Date(cal,test);
		} catch (ParseException e){
			e.printStackTrace();
		}
		System.out.println(toISO8601Date(datetimeFormat.DATE_HMS, cal));
		System.out.println(toISO8601Date(datetimeFormat.DATE_HMS,  true, cal));
	}
}