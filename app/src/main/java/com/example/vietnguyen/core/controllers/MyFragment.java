package com.example.vietnguyen.core.controllers;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;

/**
 * Created by viet on 8/11/2015.
 */
public class MyFragment extends Fragment implements Api.OnCallApiListener{

	/**
	 * The Activity.
	 */
	protected MyActivity	activity;
	protected DialogBuilder	dlgBuilder;

	public MyFragment(){
		super();
	}

	public View getView(int viewId){
		return getView().findViewById(viewId);
	}

	public TextView getTextView(int viewId){
		return (TextView)getView().findViewById(viewId);
	}

	public EditText getEditText(int viewId){
		return (EditText)getView().findViewById(viewId);
	}

	public ImageView getImageView(int viewId){
		return (ImageView)getView().findViewById(viewId);
	}

	public void getApi(String url, JSONObject param){
		activity.getApi(url, param, this);
	}

	public void postApi(String url, JSONObject param){
		activity.postApi(url, param, this);
	}

	@Override
	public void onApiResponse(String url, JSONObject response){

	}

	@Override
	public void onApiError(String url, String errorMsg){

	}

	public static interface onFragmentActionListenter{

		public void onFragmentAction(Bundle args);
	}

	@Override
	public void onAttach(Activity activity){
		MU.log("Fragment onAttach: " + this.getClass().toString());
		super.onAttach(activity);
		try{
			this.activity = (MyActivity)activity;
			dlgBuilder = new DialogBuilder(this.activity);
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + " must be " + MyActivity.class.toString());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		MU.log("Fragment onCreate: " + this.getClass().toString());
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		// Inflate the layout for this fragment
		MU.log("Fragment onCreateView: " + this.getClass().toString());
		return inflater.inflate(R.layout.core_fragment_my, container, false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.core_options_myfragment, menu);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		MU.log("Fragment onActivityCreated: " + this.getClass().toString());
		super.onActivityCreated(savedInstanceState);
		buildLayout();
	}

	/*
	 * After load xml, build layout with your data
	 */
	protected void buildLayout(){
		MU.log("Fragment buildLayout: " + this.getClass().toString());
	}

	@Override
	public void onStart(){
		MU.log("Fragment onStart: " + this.getClass().toString());
		super.onStart();
	}

	@Override
	public void onResume(){
		MU.log("Fragment onResume: " + this.getClass().toString());
		super.onResume();
	}

	@Override
	public void onPause(){
		MU.log("Fragment onPause: " + this.getClass().toString());
		super.onPause();
	}

	@Override
	public void onStop(){
		MU.log("Fragment onStop: " + this.getClass().toString());
		super.onStop();
	}

	@Override
	public void onDestroyView(){
		MU.log("Fragment onDestroyView: " + this.getClass().toString());
		super.onDestroyView();
	}

	@Override
	public void onDestroy(){
		MU.log("Fragment onDestroy: " + this.getClass().toString());
		super.onDestroy();
	}

	@Override
	public void onDetach(){
		MU.log("Fragment onDetach: " + this.getClass().toString());
		super.onDetach();
	}
}