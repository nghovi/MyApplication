package com.nguyenhoangviet.vpcorp.controllers;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.nguyenhoangviet.vpcorp.core.controller.MyFragment;

/**
 * Created by viet on 8/11/2015.
 * http://developer.android.com/guide/components/fragments.html
 */
public class FragmentOfMainActivity extends MyFragment{

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		getMainActivity().footer.show();
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * The Activity.
	 */
	public MainActivity getMainActivity(){
		return (MainActivity)activity;
	};

	public void hideSofeKeyboard(){
		View view = activity.getCurrentFocus();
		if(view != null){
			InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public void showSofeKeyboard(){
		View view = activity.getCurrentFocus();
		if(view != null){
			InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		}
	}

	@Override
	public void onDestroyView(){
		hideSofeKeyboard();
		super.onDestroyView();
	}
}
