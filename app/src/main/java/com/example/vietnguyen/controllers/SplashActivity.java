package com.example.vietnguyen.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.vietnguyen.core.controllers.MyActivity;
import com.example.vietnguyen.myapplication.R;

public class SplashActivity extends MyActivity{

	// Splash screen timer
	private static int	SPLASH_TIME_OUT	= 2000;

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
