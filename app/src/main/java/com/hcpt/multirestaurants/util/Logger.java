package com.hcpt.multirestaurants.util;

import android.util.Log;

public class Logger {
	private static final boolean DEBUG_MODE = true;

	public static void e(Object object) {
		if (DEBUG_MODE) {
			Log.e("Restaurant", "Restaurant: " + object);
		}
	}

	public static void e(String s) {
		if (DEBUG_MODE) {
			Log.e("Restaurant", "Restaurant: " + s);
		}
	}

	public static void e(String TAG, String msg) {
		if (DEBUG_MODE) {
			Log.e(TAG, msg);
		}
	}

	public static void d(String TAG, String msg) {
		if (DEBUG_MODE) {
			Log.d(TAG, msg);
		}
	}

	public static void i(String TAG, String msg) {
		if (DEBUG_MODE) {
			Log.i(TAG, msg);
		}
	}

	public static void w(String TAG, String msg) {
		if (DEBUG_MODE) {
			Log.w(TAG, msg);
		}
	}

	public static void logWS(String TAG, String msg) {
		if (DEBUG_MODE) {
			Log.w(TAG, msg);
		}
	}
}
