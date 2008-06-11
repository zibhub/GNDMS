package de.zib.gndms.infra.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * This is a helper class for parsing/printing of ISO8601 dates in correspondence
 * with the ISO8601 date profile from W3C / the xml schema spec.
 *
 * It's a bit of an hack, it's not particularly optimized for performance but it get's the job done.
 *
 * Notes:
 * Supports years with more than four digits, negative years (instead of AD/BC era code),
 * second fractions are converted to milliseconds and properly rounded (SimpleDateFormat is severly
 * broken in how it prints them), RFC822 timezone specifiers with middle ':', and 'Z' for
 * UTC/GMT
 *
 * @author Roberto Henschenl <henschel@zib.de>
 * @author Stefan Plantikow <plantikow@zib.de>
 *
 */
@SuppressWarnings({"ClassNamingConvention"})
public final class ISO8601 {
	private ISO8601() {}

	/**
	 * ISO8601Subformat is used to specify how much precision is to be given
	 * when printing a GregorianCalendar as an ISO8601-formatted string using
	 * ISO08601format.toISO8601Date
	 */
	@SuppressWarnings({"EnumeratedClassNamingConvention"})
	public enum ISO8601Subformat {
		YEAR,
		YEAR_MONTH,
		YEAR_MONTH_DAY,
		DATE_HM(true),
		DATE_HMS(true),
		DATE_HMSMS(true);

		public final boolean hasTimeZoneSpecifier;

		ISO8601Subformat(boolean formatHasTimeZoneSpecifier) {
			hasTimeZoneSpecifier = formatHasTimeZoneSpecifier;
		}

		ISO8601Subformat() { this(false); }
	}

	// Map from subformat to "as-close-as-possible" SimpleDateFormat
	private static final Map<ISO8601Subformat,SimpleDateFormat> ISO08601format;

	// Silly set of all the ways to write GMT+0 (without the leading GMT)
	private static final Set<String> zeroZoneStrings;

	// UTC
	private static final TimeZone zeroZone=TimeZone.getTimeZone("UTC");

	static {
		//initializes several format-types according to
		// W3C XML-Schema profile for ISO8601
		ISO08601format=new EnumMap<ISO8601Subformat, SimpleDateFormat>(ISO8601Subformat.class);
		ISO08601format.put(ISO8601Subformat.YEAR, new SimpleDateFormat("yyyy", Locale.ENGLISH));
		ISO08601format.put(ISO8601Subformat.YEAR_MONTH, new SimpleDateFormat("yyyy-MM", Locale.ENGLISH));
		ISO08601format.put(ISO8601Subformat.YEAR_MONTH_DAY, new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH));
		ISO08601format.put(ISO8601Subformat.DATE_HM, new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.ENGLISH));
		ISO08601format.put(ISO8601Subformat.DATE_HMS, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH));
		ISO08601format.put(ISO8601Subformat.DATE_HMSMS, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.Z", Locale.ENGLISH));

		// helps detect weird GMT+00:00 specifiers 
		zeroZoneStrings = new CopyOnWriteArraySet<String>();
		zeroZoneStrings.add("+0");
		zeroZoneStrings.add("+0:0");
		zeroZoneStrings.add("+0:00");
		zeroZoneStrings.add("+00");
		zeroZoneStrings.add("+00:0");
		zeroZoneStrings.add("+00:00");
	}

	/**
	 * 	Sets all values of the calendar to zero,
	 *	parses a given String in ISO8601-format, and applies it to the calendar.
	 *
	 *  When an exception is thrown, the state of the calendar is undefined.
	 *
	 */
	public static void parseISO8601DateStr(GregorianCalendar cal,
			String isoDateStr) throws java.text.ParseException {
		cal.clear();
		parseISO8601DateStrNoInit(cal, isoDateStr.trim());
	}

	/**
	 * Parses a given String in ISO08601-format and applies it to a given calendar.
	 * Pay attention that the calendar will not have been reset before the string is applied.
	 * So it will take the current date and time as it's values and will overwrite only the
	 * values which are contained in the string.
 	 */
	@SuppressWarnings({"AssignmentToMethodParameter"})
	private static void parseISO8601DateStrNoInit(GregorianCalendar cal,
			String isoDateStr) throws ParseException {

		try {
			// SPLITTING DATE and TIME
			String timeStr = "";
			boolean isFullFormat = false;
			if (isoDateStr.contains("T")) {
				String[] tmp = isoDateStr.split("T");
				if (tmp.length > 2)
					throw new ParseException(isoDateStr, 0);
				isoDateStr = tmp[0];
				timeStr = tmp[1];
				isFullFormat = true;
			}

			parseSplitIsoAndTimeStr(cal, isoDateStr, timeStr, isFullFormat);
		} catch (NumberFormatException e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	/**
	 * Renders cal as an ISO8601-formatted string.
	 *
	 * @param subformat specifies the precision of the string representation
	 * @param cal the dateTime to print
	 * @return an ISO8601 representation of cal as described by m
	 */
	public static String formatAsISO8601DateStr(ISO8601Subformat subformat, Calendar cal) {
		return formatAsISO8601DateStr(subformat, false, cal);
	}

	/**
	 * Renders cal as an ISO8601-formatted string.
	 *
	 * @param subformat specifies the precision of the string representation
	 * @param normalizeTZ if true, shift cal to UTC (Z timezone in ISO8601)
	 * @param cal the dateTime to print
	 * @return an ISO8601 representation of cal as described by m
	 */
	public static String formatAsISO8601DateStr(
		  ISO8601Subformat subformat, boolean normalizeTZ, Calendar cal) {

		// We cant avoid cloning here since we might have to change teh dateFormats timezone
		// TODO: Cache to avoid cloning?
		SimpleDateFormat dateFormat= (SimpleDateFormat) ISO08601format.get(subformat).clone();
		dateFormat.setTimeZone(normalizeTZ ? zeroZone : cal.getTimeZone());
		final String formattedString = dateFormat.format(cal.getTimeInMillis());
		final String resultString =
			  subformat.hasTimeZoneSpecifier ? fixTimeZone(formattedString) : formattedString;

		if (ISO8601Subformat.DATE_HMSMS.equals(subformat)) {
			final int dotIndex = resultString.indexOf('.');
			return prefixWithEra(cal,
				  resultString.substring(0, dotIndex) + '.' +
				  prefixWithZeros(cal.get(Calendar.MILLISECOND)) +
				  resultString.substring(dotIndex + 1));
		}
		else
			return prefixWithEra(cal, resultString);
	}

	private static void parseSplitIsoAndTimeStr(
		  GregorianCalendar cal, String theIsoDateStr, String timeStr, boolean fullFormat)
		  throws ParseException {
		int era = GregorianCalendar.AD;
		final String isoDateStr;
		if (theIsoDateStr.charAt(0) == '-') {
			isoDateStr = theIsoDateStr.substring(1);
			era = GregorianCalendar.BC;
		}
		else isoDateStr = theIsoDateStr;

		String[] dateval = isoDateStr.split("-");

		String zone = "";
		int[] timeEntries = null;
		if (dateval.length > 4 || fullFormat && dateval.length != 3)
			throw new ParseException(timeStr, 0);

		//SPLITTING TIME and OFFSET
		if (timeStr.length() > 0) {
			if (timeStr.contains("+") || timeStr.contains("-")) {

				int index = timeStr.lastIndexOf('+');
				if (index == -1)
					index = timeStr.lastIndexOf('-');
				else if (timeStr.lastIndexOf('-') > -1)
					throw new ParseException(timeStr, 0);
				zone = timeStr.substring(index, timeStr.length());
				timeEntries = getTimeEntries(timeStr.substring(0, index));
			}
			if (timeStr.contains("Z")) {
				timeEntries = getTimeEntries(timeStr.substring(0, timeStr.length() - 1));
				zone = "+00:00";
			}
		}

		cal.set(Calendar.ERA, era);
		setTimezone(cal, zone);
		setDateValue(cal, dateval);
		setTimeValue(cal, timeEntries);
	}



	//Splits a time-String into an Array
	//so that a time like '12:14:04.12' will result in [12,14,4,12]
	@SuppressWarnings({"NumericCastThatLosesPrecision", "MagicNumber"})
	private static int[] getTimeEntries(String str) throws ParseException {
		int milSec = 0;
		String[] values = str.split(":");
		switch (values.length) {
			case 0:
			case 1:
			case 2:
				break;
			case 3:
				if (values[2].contains(".")) {
					int dotPos = values[2].indexOf('.');
					if (dotPos + 1 >= values[2].length())
						throw new ParseException("Missing milliseconds", dotPos);
					final String milSecStr = values[2].substring(dotPos);
					double milSecApprox = Math.round(Double.valueOf(milSecStr) * 1000.0D);
					milSec = (int) Math.floor(milSecApprox);
					if (milSec < 0)
						throw new ParseException("Negative milliseconds", dotPos);
					values[2] = values[2].substring(0, dotPos);
				}
				break;
			default:
				throw new ParseException("Time string contains more components than expeced", 0);
		}
		int[] time = new int[4];
		for (int i = 0; i < values.length; i++) {
			time[i] = Integer.valueOf(values[i]);
		}
		time[3] = milSec;

		return time;
	}

	private static void setTimezone(Calendar cal, String zoneString) throws ParseException {
		if (!"".equals(zoneString)) {
			final String gmtZoneString = "GMT" + zoneString;
			final TimeZone zone = TimeZone.getTimeZone(gmtZoneString);
			// Since this is targeted at Java 1.5, this is the only way
			// to find out wether getTimezone() did return a match 
			// (it returns GMT if fed with an unknown zone identifier)
			if ("GMT".equals(zone.getID()) && !zeroZoneStrings.contains(zoneString))
				throw new ParseException("Unknown time zone", 0);
			cal.setTimeZone(zone);
		} else {
			cal.setTimeZone(TimeZone.getDefault());
		}
	}

	private static void setDateValue(Calendar cal, String[] dateval) {
		switch (dateval.length) {
			case 3:
				cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateval[2]));
			case 2:
				// Why, oh, why, dear Java overlords
				cal.set(Calendar.MONTH, Integer.valueOf(dateval[1]) - 1);
			case 1:
				cal.set(Calendar.YEAR, Integer.valueOf(dateval[0]));
			default:
		}
	}

	private static void setTimeValue(Calendar cal, int[] timeEntries) {
		if (timeEntries != null) {
			cal.set(Calendar.HOUR_OF_DAY, timeEntries[0]);
			cal.set(Calendar.MINUTE, timeEntries[1]);
			cal.set(Calendar.SECOND, timeEntries[2]);
			cal.set(Calendar.MILLISECOND, timeEntries[3]);
		}
	}

	private static String fixTimeZone(final String inStr) {
		int lastColon = inStr.length() - 2;
		if (lastColon < 0)
			throw new RuntimeException("Invalid timeStr during output");
		final String tailStr = inStr.substring(0, lastColon) + ':' + inStr.substring(lastColon);

		int index = tailStr.indexOf('+');
		if (index < 0 || index == tailStr.length())
			return tailStr;
		if (tailStr.indexOf('+', index + 1) >= 0)
			throw new RuntimeException("Too many + encountered");
		String remainder = tailStr.substring(index);
		return tailStr.substring(0, index) + (zeroZoneStrings.contains(remainder) ? 'Z' : remainder);
	}

	@SuppressWarnings({"MagicNumber"})
	private static String prefixWithZeros(int millis) {
		if (millis < 0)
			throw new IllegalArgumentException("Negative milliseconds");
		if (millis < 10)
			return "00" + Integer.toString(millis);
		if (millis < 100)
			return '0' + Integer.toString(millis);
		return Integer.toString(millis);
	}

	private static String prefixWithEra(Calendar cal, String resultString) {
		return cal.get(Calendar.ERA) == GregorianCalendar.BC ?
				  '-' + resultString
		          : resultString;
	}


	@SuppressWarnings({"CallToPrintStackTrace", "UseOfSystemOutOrSystemErr"})
	public static void main(String[] args) {

		String test="-200003-11-01T14:20:14.99999-06:00";
		GregorianCalendar cal= new GregorianCalendar();
		try{
			parseISO8601DateStr(cal, test);
		} catch (ParseException e){
			e.printStackTrace();
		}
		System.out.println(formatAsISO8601DateStr(ISO8601Subformat.DATE_HMSMS, false, cal));
		final String result = formatAsISO8601DateStr(ISO8601Subformat.DATE_HMSMS, true, cal);
		System.out.println(result);

		try {
			parseISO8601DateStr(cal, result);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(formatAsISO8601DateStr(ISO8601Subformat.DATE_HMS, false, cal));
		System.out.println(formatAsISO8601DateStr(ISO8601Subformat.DATE_HMSMS, true, cal));
	}
}