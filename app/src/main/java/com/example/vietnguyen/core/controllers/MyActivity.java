package com.example.vietnguyen.core.controllers;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.vietnguyen.controllers.Background;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;
import com.facebook.AccessToken;

/**
 * Created by viet on 8/7/2015.
 */
public class MyActivity extends Activity implements FragmentManager.OnBackStackChangedListener,MyFragment.onFragmentActionListenter{

	protected final String	SHARED_PREFERENCES_NAME	= "MY_PREFERENCES";
	public final String		PREF_SHOW_MOTTOS		= "SHOW_MOTTOS";
	public final String		PREF_PUSH_NOTIFICATION	= "PUSH_NOTIFICATION";
	protected Api			api;

	private FragmentManager	mFragmentManager;
	private AccessToken		accessToken;
	public Background		bg;

	/*
	 * To use this function, make sure that activity's layout file include fragment_container.
	 * no add to back stack
	 */
	public void replaceWithFragment(Fragment fragment, String updatedKey, Object updatedValue){
		mFragmentManager.popBackStack();
		addFragment(fragment, updatedKey, updatedValue);
	}

	public void replaceWithFragment(Fragment fragment){
		mFragmentManager.popBackStack();
		addFragment(fragment, null, null);
	}

	// add to back stack
	public void addFragment(Fragment fragment, String updatedKey, Object updatedValue){
		setUpdatedData(fragment.getClass(), updatedKey, updatedValue);
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().toString());
		fragmentTransaction.addToBackStack(fragment.getClass().toString());
		fragmentTransaction.commit();
	}

	public void addFragment(Fragment fragment){
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

	// //////////////////////////////////Preferences/////////////////////////////////////

	public void getApi(String url, JSONObject param, Api.OnCallApiListener onCallApiListener){
		api.get(this, url, param, onCallApiListener);
	}

	public void postApi(String url, JSONObject param, Api.OnCallApiListener onCallApiListener){
		api.post(this, url, param, onCallApiListener);
	}

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

	/*
	 * usage from fragment:
	 * Bundle args = new Bundle()
	 * args.putString("cmd", "exit");
	 * activity.onFragmentAction(args);
	 */
	@Override
	public void onFragmentAction(Bundle args){
		MU.log("onFragmentAction " + this.getLocalClassName() + " with" + args.toString());
	}

	public void setAccessToken(AccessToken accessToken){
		this.accessToken = accessToken;
	}

	public AccessToken getAccessToken(){
		return this.accessToken;
	}
}
