package com.nguyenhoangviet.vpcorp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nguyenhoangviet.vpcorp.core.controller.MyActivity;
import com.nguyenhoangviet.vpcorp.myapplication.R;

public class SplashActivity extends MyActivity{

	// Splash screen timer
	private static int	SPLASH_TIME_OUT	= 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		/*
		 * Showing splash screen with a timer. This will be useful when you
		 * want to show case your app logo / company
		 */
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run(){
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		}, SPLASH_TIME_OUT);
	}
}
