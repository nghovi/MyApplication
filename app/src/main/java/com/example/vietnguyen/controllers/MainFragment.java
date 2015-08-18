package com.example.vietnguyen.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.myapplication.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends MyFragment{

	public MainFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void buildLayout(){
		super.buildLayout();
	}
}
