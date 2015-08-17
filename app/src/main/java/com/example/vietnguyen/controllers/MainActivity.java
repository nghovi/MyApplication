package com.example.vietnguyen.controllers;

import android.os.Bundle;

import com.example.vietnguyen.core.controllers.MyActivity;
import com.example.vietnguyen.myapplication.R;

public class MainActivity extends MyActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainFragment main = new MainFragment();
		replaceWithFragment(main);
	}
}
