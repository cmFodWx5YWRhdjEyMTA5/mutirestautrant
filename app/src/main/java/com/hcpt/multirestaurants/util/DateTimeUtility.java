package com.hcpt.multirestaurants.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateTimeUtility {

	public static String convertTimeStampToDate(String timeStamp,
			String outputFormat) {
		SimpleDateFormat formater = new SimpleDateFormat(outputFormat,
				Locale.getDefault());
		try {
			Date date = new Date(Long.parseLong(timeStamp) * 1000);
			return formater.format(date);
		} catch (NumberFormatException ex) {
			return formater.format(new Date());
		}
	}

	public static String convertDatetoTimeStamp(Date date) {
		String result = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		try {
			result = cal.getTimeInMillis() + "";
		} catch (Exception ex) {
			result = "";
		}
		return result;
	}

	public static long getDateDiff(Date curDate, Date specDate,
			TimeUnit timeUnit) {
		long diff = curDate.getTime() - specDate.getTime();
		return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static String formatDate(Date date, String outputFormat) {
		SimpleDateFormat formater = new SimpleDateFormat(outputFormat,
				Locale.getDefault());
		return formater.format(date);
	}

	public static String convertStringToDate(String strDate,
			String inputFormat, String outputFormat) {
		SimpleDateFormat dateFormaterInput, dateFormaterOutput;
		dateFormaterInput = new SimpleDateFormat(inputFormat,
				Locale.getDefault());
		dateFormaterOutput = new SimpleDateFormat(outputFormat,
				Locale.getDefault());
		Date dob;
		try {
			dob = dateFormaterInput.parse(strDate);
			return dateFormaterOutput.format(dob);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return strDate;
		}
	}

	public static Date getDateValueFromString(String stringFormat,
			String dateString) {
		DateFormat formatter = new SimpleDateFormat(stringFormat,
				Locale.getDefault());
		try {
			Date date = formatter.parse(dateString);
			return date;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
