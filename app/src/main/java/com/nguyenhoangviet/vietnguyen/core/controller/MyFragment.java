package com.nguyenhoangviet.vietnguyen.core.controller;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.core.views.widgets.MyTextView;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viet on 8/11/2015.
 * http://developer.android.com/guide/components/fragments.html
 */
public class MyFragment extends Fragment{

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

	public ScrollView getScrollView(int viewId){
		return (ScrollView)getView().findViewById(viewId);
	}

	public EditText getEditText(View v, int viewId){
		return (EditText)v.findViewById(viewId);
	}

	public ImageView getImageView(int viewId){
		return getImageView(getView(), viewId);
	}

	public ImageView getImageView(View parent, int viewId){
		return (ImageView)parent.findViewById(viewId);
	}

	public void setImageResourceFor(int imgId, int resId){
		getImageView(imgId).setImageResource(resId);
	}

	public void setImageResourceFor(View parent, int imgId, int resId){
		getImageView(parent, imgId).setImageResource(resId);
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
		if(view != null){
			view.setOnClickListener(listener);
		}
	}

	public void setTextFor(int resource, String text){
		setTextFor(getView(), resource, text);
	}

	public void setTextFor(View v, int resource, String text){
		TextView tv = getTextView(v, resource);
		tv.setText(text);
	}

	public TextView createTextView(String str){
		TextView tv = new TextView(this.activity);
		tv.setText(str);
		return tv;
	}

	public void setAfterTextChangedListenerFor(int resourceId, final MyTextView.OnAfterTextChangedListener listener){
		final String textBefore = getTextView(resourceId).getText().toString();
		getEditText(resourceId).addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void afterTextChanged(Editable editable){
				listener.afterTextChanged(textBefore, editable.toString());
			}
		});
	}

	// Eg EditorInfo.IME_ACTION_SEARCH
	public void setOnEditorActionFor(int resource, final int actionCode, final MyTextView.OnKeyboardBtnPressed listener){
		getEditText(resource).setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
				if(actionId == actionCode){
					listener.onPress(v.getText().toString());
					return true;
				}
				return false;
			}
		});
	}

	public void goneView(View v){
		v.setVisibility(View.GONE);
	}

	public void goneView(int viewId){
		goneView(getView(), viewId);
	}

	public void goneView(View parent, int viewId){
		getView(parent, viewId).setVisibility(View.GONE);
	}

	public void invisibleView(int viewId){
		invisibleView(getView(), viewId);
	}

	public void invisibleView(View v){
		v.setVisibility(View.VISIBLE);
	}

	public void invisibleView(View parent, int viewId){
		getView(parent, viewId).setVisibility(View.INVISIBLE);
	}

	public void visibleView(int viewId){
		getView(viewId).setVisibility(View.VISIBLE);
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

	/*
	 * other: speak
	 */
	public void setFoldAction(final View foldable, final ImageView imgFoldIcon, int contentId, final View otherView, final View.OnClickListener additionalListener){
		final View content = getView(foldable, contentId);
		foldable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(View.VISIBLE == content.getVisibility()){
					foldable.setBackgroundColor(0);
					imgFoldIcon.setImageResource(R.drawable.ic_expand_less_black_18dp);
					content.setVisibility(View.GONE);
					if(otherView != null){
						otherView.setVisibility(View.GONE);
					}
				}else{
					foldable.setBackgroundColor(getResources().getColor(R.color.bg_selected_panel));
					imgFoldIcon.setImageResource(R.drawable.ic_expand_more_black_18dp);
					content.setVisibility(View.VISIBLE);
					if(otherView != null){
						otherView.setVisibility(View.VISIBLE);
					}
				}

				if(additionalListener != null){
					additionalListener.onClick(view);
				}
			}
		});
	}

	// for not overlap onclick function
	public void setFoldAction2(final View foldable, int clickToFold, final ImageView imgFoldIcon, int contentId, final View otherView){
		final View content = getView(foldable, contentId);
		getView(foldable, clickToFold).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(View.VISIBLE == content.getVisibility()){
					foldable.setBackgroundColor(0);
					imgFoldIcon.setImageResource(R.drawable.ic_expand_less_black_18dp);
					content.setVisibility(View.GONE);
					if(otherView != null){
						otherView.setVisibility(View.GONE);
					}
				}else{
					foldable.setBackgroundColor(getResources().getColor(R.color.bg_selected_panel));
					imgFoldIcon.setImageResource(R.drawable.ic_expand_more_black_18dp);
					content.setVisibility(View.VISIBLE);
					if(otherView != null){
						otherView.setVisibility(View.VISIBLE);
					}
				}
			}
		});
	}

	public void showShortToast(String msg){
		Toast.makeText(this.activity, msg, Toast.LENGTH_SHORT).show();
	}

	public void showLongToast(String msg){
		Toast.makeText(this.activity, msg, Toast.LENGTH_LONG).show();
	}

	// ///////////////////////////////// NET WORK ///////////////////////////////////////////
	public void getApi(String url, JSONObject param, Api.OnCallApiListener listener){
		this.activity.getApi(url, param, listener);
	}

	public void postApi(String url, JSONObject param, Api.OnCallApiListener listener){
		this.activity.postApi(url, param, listener);
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

	public Object getUpdatedData(String updatedDataKey, Object defaultObj){
		if(updatedData != null && updatedData.containsKey(updatedDataKey)){
			return updatedData.get(updatedDataKey);
		}
		return defaultObj;
	}

	// ////////////////////////////////////////// BASIC ////////////////////////////////////////

	/*
	 * After load xml, build layout with your data
	 */
	protected void buildLayout(){
		MU.log("Fragment buildLayout: " + this.getClass().toString());
		setBackBtnOnClick();
	}

	public interface VirtualItemLayoutBuilder{

		public void buildItemLayout(View itemRoot, Object itemData);
	}

	/*
	 * It's not good now buy not using removeFromVirtualListFunction
	 * But it's acceptable by rebuilding the list since its small.
	 */
	public void buildVirtualListByLinearLayout(int lnrParentId, int itemLayoutId, List data, VirtualItemLayoutBuilder builder){
		LinearLayout parent = getLinearLayout(lnrParentId);
		parent.removeAllViews();
		LayoutInflater inflater = activity.getLayoutInflater();
		for(Object itemData : data){
			View itemRoot = inflater.inflate(itemLayoutId, null);
			builder.buildItemLayout(itemRoot, itemData);
			parent.addView(itemRoot);
		}
	}

	public void addToVirtualList(int lnrParentId, int itemLayoutId, Object itemData, VirtualItemLayoutBuilder builder){
		LinearLayout parent = getLinearLayout(lnrParentId);
		LayoutInflater inflater = activity.getLayoutInflater();
		View itemRoot = inflater.inflate(itemLayoutId, null);
		builder.buildItemLayout(itemRoot, itemData);
		parent.addView(itemRoot);
	}

	protected void setBackBtnOnClick(){
		setOnClickFor(R.id.img_back, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickBackBtn();
			}
		});
	}

	protected void onClickBackBtn(){
		this.activity.backOneFragment();
	}

	// /////////////////////////////// onFunctions /////////////////////////////////////
	@Override
	public void onAttach(Activity activity){
		MU.log("Fragment onAttach: " + this.getClass().toString());
		super.onAttach(this.activity);
		try{
			this.activity = (MyActivity)activity;
			dlgBuilder = new DialogBuilder(this.activity);
		}catch(ClassCastException e){
			throw new ClassCastException(this.activity.toString() + " must be " + MyActivity.class.toString());
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
		// can return null if this fragment doesn't return
		MU.log("Fragment onCreateView: " + this.getClass().toString());
		return inflater.inflate(R.layout.core_fragment_my, container, false);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		// inflater.inflate(R.menu.core_options_myfragment, menu);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		MU.log("Fragment onActivityCreated: " + this.getClass().toString());
		super.onActivityCreated(savedInstanceState);
		buildLayout();
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
