package com.hcpt.multirestaurants.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkUtil checks available network
 * 
 */
public class NetworkUtil {

	/**
	 * Enable Strict mode
	 * 
	 */
	public static void enableStrictMode() {

		try {
			@SuppressWarnings("rawtypes")
			Class strictModeClass = Class.forName("android.os.StrictMode");
			@SuppressWarnings("rawtypes")
			Class strictModeThreadPolicyClass = Class
					.forName("android.os.StrictMode$ThreadPolicy");
			Object laxPolicy = strictModeThreadPolicyClass.getField("LAX").get(
					null);
			@SuppressWarnings("unchecked")
			Method method_setThreadPolicy = strictModeClass.getMethod(
					"setThreadPolicy", strictModeThreadPolicyClass);
			method_setThreadPolicy.invoke(null, laxPolicy);
		} catch (Exception e) {
		}
	}

	/**
	 * Check network connection
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetworkAvailable(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null) {
			return false;
		}
		if (!i.isConnected()) {
			return false;
		}
		if (!i.isAvailable()) {
			return false;
		}
		return true;
	}
}
