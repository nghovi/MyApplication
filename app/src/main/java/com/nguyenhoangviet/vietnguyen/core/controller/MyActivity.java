package com.nguyenhoangviet.vietnguyen.core.controller;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.nguyenhoangviet.vietnguyen.controllers.Background;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

//import com.facebook.AccessToken;

/**
 * Created by viet on 8/7/2015.
 */
public class MyActivity extends Activity implements FragmentManager.OnBackStackChangedListener{

	public static final String	SHARED_PREFERENCES_NAME	= "MY_PREFERENCES";
	public static final String	PREF_SHOW_MOTTOS		= "SHOW_MOTTOS";
	public static final String	PREF_PUSH_NOTIFICATION	= "PUSH_NOTIFICATION";
	protected Api				api;

	protected FragmentManager	mFragmentManager;
	// private AccessToken accessToken;
	public Background			bg;

	// /////////////////////////// ACTIVITY CONTACT ///////////////////////////////
	public static interface onActivityActionListenter{

		public void onActivityAction(Bundle args);
	}

	/*
	 * To use this function, make sure that activity's layout file include fragment_container.
	 * no add to back stack
	 */
	public void replaceWithFragment(MyFragment fragment, String updatedKey, Object updatedValue){
		mFragmentManager.popBackStack();
		addFragment(fragment, updatedKey, updatedValue);
	}

	public void replaceWithFragment(MyFragment fragment){
		mFragmentManager.popBackStack();
		addFragment(fragment, null, null);
	}

	// add to back stack
	public void addFragment(MyFragment fragment, String updatedKey, Object updatedValue){
		fragment.setUpdatedData(updatedKey, updatedValue);
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);// todo
		fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().toString());
		fragmentTransaction.addToBackStack(fragment.getClass().toString());
		fragmentTransaction.commit();
	}

	public void addFragment(MyFragment fragment){
		addFragment(fragment, null, null);
	}

	public void backOneFragment(){
		mFragmentManager.popBackStack();
	}

	public void backToFragment(Class fragmentClass, String updatedKey, Object updatedValue){
		mFragmentManager.popBackStack(fragmentClass.toString(), 0);
		setUpdatedData(fragmentClass, updatedKey, updatedValue);
	}

	public void backToFragment(Class fragmentClass){
		backToFragment(fragmentClass, null, null);
	}

	public void backToBeforeFragment(Class fragmentClass){
		String tag = fragmentClass.toString();
		mFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	protected void emptyFragmentStack(){
		mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public void registerOnClickListener(List<View> views, View.OnClickListener listener){
		for(View view : views){
			view.setOnClickListener(listener);
		}
	}

	private void setUpdatedData(Class fragmentClass, String updatedKey, Object updatedValue){
		if(updatedKey != null){
			MyFragment currentFragment = (MyFragment)mFragmentManager.findFragmentByTag(fragmentClass.toString());
			currentFragment.setUpdatedData(updatedKey, updatedValue);
		}
	}

	public boolean isCurrentFragment(String fragment){
		String currentFragmentName = mFragmentManager.getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1).getName();
		return fragment.equals(currentFragmentName);
	}

	// //////////////////////////////////Preferences/////////////////////////////////////
	public String getStringPreference(String preferenceName, String defaultValue){
		SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		return pref.getString(preferenceName, defaultValue);
	}

	public int getIntPreference(String preferenceName, int defaultValue){
		SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		return pref.getInt(preferenceName, defaultValue);
	}

	public long getLongPreference(String preferenceName, long defaultValue){
		SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		return pref.getLong(preferenceName, defaultValue);
	}

	public void saveStringPreference(String key, String value){
		SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void saveIntPreference(String key, int value){
		SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void saveLongPreference(String key, long value){
		SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
		editor.putLong(key, value);
		editor.commit();
	}

	// //////////////////////////////////Preferences/////////////////////////////////////

	public void getApi(String url, JSONObject param, Api.OnCallApiListener onCallApiListener){
		api.get(this, url, param, onCallApiListener);
	}

	public void postApi(String url, JSONObject param, Api.OnCallApiListener onCallApiListener){
		api.post(this, url, param, onCallApiListener);
	}

	// ///////////////////////////// onFunctions ///////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState){
		MU.log("onCreate " + this.getLocalClassName());
		super.onCreate(savedInstanceState);
		mFragmentManager = getFragmentManager();
		mFragmentManager.addOnBackStackChangedListener(this);
		api = new Api();
		// setContentView(R.layout.activity_my);
	}

	@Override
	protected void onStart(){
		MU.log("onStart " + this.getLocalClassName());
		super.onStart();
	}

	@Override
	protected void onRestart(){
		MU.log("onRestart " + this.getLocalClassName());
		super.onRestart();
	}

	@Override
	protected void onResume(){
		MU.log("onResume " + this.getLocalClassName());
		super.onResume();
	}

	@Override
	protected void onPause(){
		MU.log("onPause " + this.getLocalClassName());
		super.onPause();
	}

	@Override
	protected void onStop(){
		MU.log("onStop " + this.getLocalClassName());
		super.onStop();
	}

	@Override
	protected void onDestroy(){
		MU.log("onDestroy " + this.getLocalClassName());
		super.onDestroy();
	}

	@Override
	public void onBackStackChanged(){
		MU.log("Fragment backstack changed: ");
		for(int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++){
			String fragmentName = mFragmentManager.getBackStackEntryAt(i).getName();
			MU.log(fragmentName);
		}
	}

	// public void setAccessToken(AccessToken accessToken){
	// this.accessToken = accessToken;
	// }
	//
	// public AccessToken getAccessToken(){
	// return this.accessToken;
	// }
}
