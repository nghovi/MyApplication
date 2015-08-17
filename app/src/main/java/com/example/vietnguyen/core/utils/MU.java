package com.example.vietnguyen.core.utils;

import org.json.JSONObject;

import android.util.Log;

/**
 * Created by viet on 8/7/2015.
 */

public class MU{

	public static int		LOG_COUNTER	= 0;
	public static String	LOG_TAG		= "******";

	public static void log(){
		LOG_COUNTER++;
		Log.e(LOG_TAG, String.valueOf(LOG_COUNTER));
	}

	public static void log(String msg){
		LOG_COUNTER++;
		Log.e(LOG_TAG, String.valueOf(LOG_COUNTER) + ' ' + msg);
	}

	public static void log(String msg, JSONObject jsonObject){
        LOG_COUNTER++;
		log(msg + ":\n" + jsonObject.toString());
	}
}
