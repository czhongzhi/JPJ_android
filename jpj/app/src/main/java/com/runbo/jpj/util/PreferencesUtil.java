package com.runbo.jpj.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.runbo.jpj.application.MyApplication;

public class PreferencesUtil {
	
	/**
	 * 用SharedPreferences存储一个键值到xml
	 * @param context 
	 * @param preference xml文件名
	 * @param key 键
	 * @param value 值
	 */
    public static void setStringPreferences(Context context, String preference, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, MyApplication.MODE_1);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringPreference(Context context, String preference, String key, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, MyApplication.MODE_1);
        return sharedPreferences.getString(key, defaultValue);
    }

	/**
	 * 用SharedPreferences存储一个键值(int)到xml
	 * @param context 
	 * @param preference xml文件名
	 * @param key 键
	 * @param value 值
	 */
    public static void setIntPreference(Context context, String preference, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, MyApplication.MODE_1);
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

	/**
	 * 用SharedPreferences存储一个键值(int)到xml
	 * @param context 
	 * @param preference xml文件名
	 * @param key 键
	 * @param defaultValue 值
	 */
    public static int getIntPreference(Context context, String preference, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, MyApplication.MODE_1);
        return sharedPreferences.getInt(key, defaultValue);
    }
    
    public static boolean getbooleanPreference(Context context, String preference, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, MyApplication.MODE_1);
        return sharedPreferences.getBoolean(key, false);
    }
    
    public static void setbooleanPreference(Context context,String preference, String key, boolean flag) {
		// TODO Auto-generated method stub
		  SharedPreferences sharedPreferences = context.getSharedPreferences(preference, MyApplication.MODE_1);
		  Editor editor = sharedPreferences.edit();
		  editor.putBoolean(key, flag);
		  editor.commit();
	}

	public static boolean getDefualtState(Context mContext, String tpreference,String key, boolean flag) {
		 SharedPreferences sharedPreferences = mContext.getSharedPreferences(tpreference, MyApplication.MODE_1);
	     return sharedPreferences.getBoolean(key, flag);
	}

	public static void setDefualtState(Context mContext, String tpreference,
			String key, boolean flag) {
		 SharedPreferences sharedPreferences = mContext.getSharedPreferences(tpreference, MyApplication.MODE_1);
		 Editor editor = sharedPreferences.edit();
		 editor.putBoolean(key, flag);
		 editor.commit();
	}

}
