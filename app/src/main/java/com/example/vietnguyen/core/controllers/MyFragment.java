package com.example.vietnguyen.core.controllers;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by viet on 8/11/2015.
 * http://developer.android.com/guide/components/fragments.html
 */
public class MyFragment extends Fragment implements Api.OnCallApiListener{

	/**
	 * The Activity.
	 */
	protected MyActivity			activity;
	protected DialogBuilder			dlgBuilder;
	protected Map<String, Object>	updatedData;

	public MyFragment(){
		super();
	}

	// ////////////////////// VIEW ////////////////////////////////////////////////
	public View getView(int viewId){
		return getView(getView(), viewId);
	}

	public View getView(View parent, int viewId){
		return parent.findViewById(viewId);
	}

	public TextView getTextView(int viewId){
		return (TextView)getView().findViewById(viewId);
	}

	public TextView getTextView(View v, int viewId){
		return (TextView)v.findViewById(viewId);
	}

	public LinearLayout getLinearLayout(int viewId){
		return (LinearLayout)getView().findViewById(viewId);
	}

	public LinearLayout getLinearLayout(View v, int viewId){
		return (LinearLayout)v.findViewById(viewId);
	}

	public ListView getListView(int viewId){
		return (ListView)getView().findViewById(viewId);
	}

	public Button getButton(int viewId){
		return (Button)getView().findViewById(viewId);
	}

	public EditText getEditText(int viewId){
		return (EditText)getView().findViewById(viewId);
	}

	public EditText getEditText(View v, int viewId){
		return (EditText)v.findViewById(viewId);
	}

	public ImageView getImageView(int viewId){
		return (ImageView)getView().findViewById(viewId);
	}

	public void setImageResourceFor(int imgId, int resId) {
		getImageView(imgId).setImageResource(resId);
	}

	public void setLinkFor(int resource, String url){
		if(!url.startsWith("http://") && !url.startsWith("https://")){
			url = "http://" + url;
		}

		final String finalUrl = url;
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View view){

				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
				startActivity(browserIntent);
			}
		};

		this.setOnClickFor(resource, listener);
	}

	public void setOnClickFor(int resource, View.OnClickListener listener){
		setOnClickFor(getView(), resource, listener);
	}

	public void setOnClickFor(View parent, int resource, View.OnClickListener listener){
		View view = getView(parent, resource);
		view.setOnClickListener(listener);
	}

	public void setTextFor(int resource, String text){
		setTextFor(getView(), resource, text);
	}

	public void setTextFor(View v, int resource, String text){
		TextView tv = getTextView(v, resource);
		tv.setText(text);
	}

	public TextView createTextView(String str){
		TextView tv = new TextView(activity);
		tv.setText(str);
		return tv;
	}

	public void goneView(View v){
		v.setVisibility(View.GONE);
	}

	public void invisibleView(View v){
		v.setVisibility(View.VISIBLE);
	}

	public void visibleView(View v){
		v.setVisibility(View.VISIBLE);
	}

	/*
	 * Determine the enable/disable status of a button base on EditTexts or TextViews
	 * ONLY enable button when ALL text views already has data.
	 * @param views: List of EditText or TextView
	 * usage example: addTextWatcher(mBtnCheckRecruiting, R.drawable.shape_button_enable2, Arrays.asList((View)mTxtStartDt, mEdtQuestion));
	 */
	public static void addTextWatcher(final View clickableText, final List<View> views){
		clickableText.setEnabled(false);
		TextWatcher tw = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){

			}

			@Override
			public void afterTextChanged(Editable s){
				for(View view : views){
					if(((TextView)view).getText().toString().equals("")){
						clickableText.setVisibility(View.INVISIBLE);
						return;
					}
				}
				clickableText.setVisibility(View.VISIBLE);
				clickableText.setEnabled(true);
			}
		};

		for(View view : views){
			((TextView)(view)).addTextChangedListener(tw);
		}
	}

	public void setFoldAction(final int imgFoldIcon,int contentId) {
		setFoldAction(getView(), imgFoldIcon, contentId);
	}

	public void setFoldAction(View parent, final int imgFoldIcon,int contentId) {
		final View content = getView(parent, contentId);
		setOnClickFor(parent, imgFoldIcon, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (View.VISIBLE == content.getVisibility()) {
					setImageResourceFor(imgFoldIcon, R.drawable.ico_female_16);
					content.setVisibility(View.GONE);
				} else {
					setImageResourceFor(imgFoldIcon, R.drawable.ico_male_16);
					content.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	public void showShortToast(String msg){
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

	public void showLongToast(String msg){
		Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
	}

	// ///////////////////////////////// NET WORK ///////////////////////////////////////////
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

	// /////////////////////////// ACTIVITY CONTACT ///////////////////////////////
	public static interface onFragmentActionListenter{

		public void onFragmentAction(Bundle args);
	}

	// /////////////////////////// UTIL ///////////////////////////////////////
	public void setUpdatedData(String key, Object value){
		if(updatedData == null){
			updatedData = new HashMap<String, Object>();
		}
		updatedData.put(key, value);
	}

	public Date getUpdatedDate(String updatedDataKey, Date defaultDate){
		if(updatedData != null && updatedData.containsKey(updatedDataKey)){
			return (Date)updatedData.get(updatedDataKey);
		}
		return defaultDate;
	}

	// ////////////////////////////////////////// BASIC ////////////////////////////////////////
	@Override
	public void onAttach(Activity activity){
		// MU.log("Fragment onAttach: " + this.getClass().toString());
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
		// MU.log("Fragment onCreateView: " + this.getClass().toString());
		return inflater.inflate(R.layout.core_fragment_my, container, false);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.core_options_myfragment, menu);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		// MU.log("Fragment onActivityCreated: " + this.getClass().toString());
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
		// MU.log("Fragment onStart: " + this.getClass().toString());
		super.onStart();
	}

	@Override
	public void onResume(){
		// MU.log("Fragment onResume: " + this.getClass().toString());
		super.onResume();
	}

	@Override
	public void onPause(){
		// MU.log("Fragment onPause: " + this.getClass().toString());
		super.onPause();
	}

	@Override
	public void onStop(){
		// MU.log("Fragment onStop: " + this.getClass().toString());
		super.onStop();
	}

	@Override
	public void onDestroyView(){
		// MU.log("Fragment onDestroyView: " + this.getClass().toString());
		super.onDestroyView();
	}

	@Override
	public void onDestroy(){
		// MU.log("Fragment onDestroy: " + this.getClass().toString());
		super.onDestroy();
	}

	@Override
	public void onDetach(){
		// MU.log("Fragment onDetach: " + this.getClass().toString());
		super.onDetach();
	}
}
