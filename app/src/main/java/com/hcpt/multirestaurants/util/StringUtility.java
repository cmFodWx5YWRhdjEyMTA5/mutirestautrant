package com.hcpt.multirestaurants.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

/**
 * StringUtility class
 * 
 */
public final class StringUtility {
	/**
	 * Check Edit Text input string
	 * 
	 * @param editText
	 * @return
	 */
	public static boolean isEmpty(EditText editText) {
		if (editText == null || editText.getEditableText() == null
				|| editText.getEditableText().toString().trim().equalsIgnoreCase("")) {
			return true;
		}
		return false;
	}

	public static String replaceImagePathInHtml(String htmlFileName, String imagePath, Context context) {
		String result = "";
		InputStream is;

		ArrayList<String> listUrlImage = new ArrayList<String>();
		try {
			is = context.getResources().getAssets().open(htmlFileName);

			String textfile = convertStreamToString(is);

			Pattern titleFinder = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>", Pattern.DOTALL
					| Pattern.CASE_INSENSITIVE);
			Matcher regexMatcher = titleFinder.matcher(textfile);
			while (regexMatcher.find()) {
				Log.i("==== Image Src", regexMatcher.group(1));
				listUrlImage.add(regexMatcher.group(1));
			}
			for (String string : listUrlImage) {
				String fileName = string.substring(string.lastIndexOf("/") + 1, string.length());
				Log.i("Lemon", "File name :" + fileName);
				textfile = textfile.replace(string, "file:///android_asset/" + imagePath + fileName);

			}
			result = textfile;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String replaceImagePathInHtml2(String htmlInput, String imagePath, Context context) {
		String result = "";

		ArrayList<String> listUrlImage = new ArrayList<String>();
		String textfile = htmlInput.replace("\"\"", "\"");
		Pattern titleFinder = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>", Pattern.DOTALL
				| Pattern.CASE_INSENSITIVE);
		Matcher regexMatcher = titleFinder.matcher(textfile);
		while (regexMatcher.find()) {
			Log.i("==== Image Src", regexMatcher.group(1));
			listUrlImage.add(regexMatcher.group(1));
		}
		for (String string : listUrlImage) {
			String fileName = string.substring(string.lastIndexOf("/") + 1, string.length());
			Log.i("Lemon", "File name :" + fileName);
			textfile = textfile.replace(string, "file:///android_asset/" + imagePath + fileName);
		}
		result = textfile;
		return result;
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		Writer writer = new StringWriter();

		char[] buffer = new char[2048];
		try {
			Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} finally {
			is.close();
		}
		String text = writer.toString();
		return text;
	}

	/**
	 * Check input string
	 * 
	 * @param editText
	 * @return
	 */
	public static boolean isEmpty(String editText) {
		if (editText == null || "".equals(editText.trim())) {
			return true;
		}
		return false;
	}

	public static String getSubString(String input, int maxLength) {
		String temp = input;
		if (input.length() < maxLength)
			return temp;
		else
			return input.substring(0, maxLength - 1) + "...";
	}

	/**
	 * Merge all elements of a string array into a string
	 * 
	 * @param strings
	 * @param separator
	 * @return
	 */
	public static String join(String[] strings, String separator) {
		StringBuffer sb = new StringBuffer();
		int max = strings.length;
		for (int i = 0; i < max; i++) {
			if (i != 0)
				sb.append(separator);
			sb.append(strings[i]);
		}
		return sb.toString();
	}

	/**
	 * Initial sync date string
	 * 
	 * @return
	 */
	public static String initDateString() {
		return "1900-01-01 09:00:00";
	}

	/**
	 * Convert a string divided by ";" to multiple xmpp users
	 * 
	 * @param userString
	 * @return
	 */
	public static String[] convertStringToXmppUsers(String userString) {
		return userString.split(";");
	}

	/**
	 * get Unique Random String
	 * 
	 * @return
	 */
	public static String getUniqueRandomString() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	/**
	 * Check mail valid
	 * @param email
	 * @return
	 */
	
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	
}
