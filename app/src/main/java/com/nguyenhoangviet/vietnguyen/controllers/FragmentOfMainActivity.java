package com.nguyenhoangviet.vietnguyen.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyenhoangviet.vietnguyen.core.controller.DialogBuilder;
import com.nguyenhoangviet.vietnguyen.core.controller.MyActivity;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragment;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.core.views.widgets.MyTextView;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

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
