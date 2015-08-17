package com.example.vietnguyen.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.database.DBHelper;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.ImageAdapter;

public class ListFragment extends MyFragment{

	private DBHelper	dbHelper;
	private ListView	listView;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		dbHelper = new DBHelper(getActivity());

		listView = (ListView)view.findViewById(R.id.listView1);

		ImageAdapter adapter = new ImageAdapter(getActivity(), dbHelper.getAllImages());

		// Assign adapter to ListView
		listView.setAdapter(adapter);

		return view;
	}
}
