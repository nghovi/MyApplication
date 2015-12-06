package com.example.vietnguyen.core.controllers;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vietnguyen.core.views.MyArrayAdapter;
import com.example.vietnguyen.models.MyModel;
import com.example.vietnguyen.myapplication.R;

import java.util.ArrayList;
import java.util.List;
//abstract classes aren't enforced to implement methods on interface, great
public abstract class MyFragmentWithList extends MyFragment implements  MyFragmentWithListInterface{

	protected List<MyModel> models;
	protected MyArrayAdapter adapter;
	protected ListView listView;

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildListView();
	}


	private void buildListView(){
		listView = getListView(getListViewId());
		listView.setOnItemClickListener(this);
		if (adapter == null) {
			initAdapter();
		}
		listView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		MyModel myModel = (MyModel)adapterView.getItemAtPosition(i);
		onClickItem(myModel);
	}

	//Do not confuse with onItemClick of android
	protected abstract void onClickItem(MyModel model);


	@Override
	public void onDestroy() {
		super.onDestroy();
		adapter = null;
		listView = null;
	}

}