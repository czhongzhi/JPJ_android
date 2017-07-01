package com.runbo.jpj.util;

import android.util.Log;

public class LogUtil {
	/**正式上线是设为false*/
	private final static boolean debug = true;
	private final static String TAG = "CZZ";
		
	public static void v(String tag, String msg) {
		if (debug) {
			Log.v(tag, msg);
		}
	}
	
	public static void v(String msg) {
		if (debug) {
			Log.v(TAG, msg);
		}
	}
	
	public static void d(String tag, String msg) {
		if (debug) {
			Log.d(tag, msg);
		}
	}
	
	public static void d(String msg) {
		if (debug) {
			Log.d(TAG, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if (debug) {
			Log.i(tag, msg);
		}
		
	}
	
	public static void i(String msg) {
		if (debug) {
			Log.i(TAG, msg);
		}
		
	}

	public static void w(String tag, String msg) {
		if (debug) {
			Log.w(tag, msg);
		}
	}
	
	public static void w(String msg) {
		if (debug) {
			Log.w(TAG, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (debug) {
			Log.e(tag, msg);
		}
	}
	
	public static void e(String msg) {
		if (debug) {
			Log.e(TAG, msg);
		}
	}
}
