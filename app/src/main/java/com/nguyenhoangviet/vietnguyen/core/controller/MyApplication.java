package com.nguyenhoangviet.vietnguyen.core.controller;

import android.app.Application;
import android.content.Context;

//import com.activeandroid.ActiveAndroid;

/**
 * Created by viet on 12/29/2015.
 */
public class MyApplication extends Application{

	// Make sure to check for null for this variable
	public static volatile Context	appContext	= null;

	@Override
	public void onCreate(){
		super.onCreate();
		// MyApplication.appContext = this.getApplicationContext();
	}

}
