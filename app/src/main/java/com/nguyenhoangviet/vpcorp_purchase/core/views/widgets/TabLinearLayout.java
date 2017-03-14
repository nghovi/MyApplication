package com.nguyenhoangviet.vpcorp_purchase.core.views.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.nguyenhoangviet.vpcorp_purchase.myapplication.R;

/**
 * Created by VietNH on 20151204.
 */
public class TabLinearLayout extends LinearLayout {

	public Map<String, LinearLayout> mTabs;
	public OnTabSelectedListener		mListener;

	public interface OnTabSelectedListener{

		public void onTabSelected(String tabName);
	}

	public TabLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public TabLinearLayout(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	public TabLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public TabLinearLayout(Context context){
		super(context);
	}

	public void initTabs(List<String> tabNames, OnTabSelectedListener listener){
		mListener = listener;
		mTabs = new HashMap<String, LinearLayout>();
		LinearLayout tabsContainer = buildTabsContainer();
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(String tabName : tabNames){
			LinearLayout lnrTab = builTab(inflater, tabsContainer, tabName);
			mTabs.put(tabName, lnrTab);
		}
		this.addView(tabsContainer, 0);
		this.switchToTab(tabNames.get(0));
	}

	private void setOnTabClickListener(LinearLayout lnrTab, final String tabName){
		lnrTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				switchToTab(tabName);
			}
		});
	}

	private LinearLayout buildTabsContainer(){
		LinearLayout lnrTab = new LinearLayout(getContext());
		lnrTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		lnrTab.setOrientation(LinearLayout.HORIZONTAL);
		lnrTab.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		lnrTab.setVerticalGravity(Gravity.CENTER_VERTICAL);
		lnrTab.setBackgroundColor(Color.WHITE);
		// lnrTab.setPadding(6, 6, 6, 6);
		return lnrTab;
	}

	private LinearLayout builTab(LayoutInflater inflater, LinearLayout tabsContainer, String tabName){
		LinearLayout lnrTab = (LinearLayout)inflater.inflate(R.layout.core_linear_layout_tab, null);
		lnrTab.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		TextView txtTabName = (TextView)lnrTab.findViewById(R.id.txt_common_bienban_tab_name);
		txtTabName.setText(tabName);
		setOnTabClickListener(lnrTab, tabName);
		tabsContainer.addView(lnrTab);
		return lnrTab;
	}

	private void switchToTab(String tabNameSelected){
		for(String tabName : mTabs.keySet()){
			LinearLayout lnrTab = mTabs.get(tabName);
			TextView txtTabName = (TextView)lnrTab.findViewById(R.id.txt_common_bienban_tab_name);
			TextView txtLineTab = (TextView)lnrTab.findViewById(R.id.txt_lineTab);
			if(tabName.equals(tabNameSelected)) {
				txtTabName.setTextColor(getResources().getColor(R.color.bg_selected_panel));
				txtLineTab.setVisibility(VISIBLE);
				// lnrTab.setBackgroundResource(R.color.bb_common_color_base);

			}else{
				txtTabName.setTextColor(getResources().getColor(R.color.core_blue));
				txtLineTab.setVisibility(INVISIBLE);
				// lnrTab.setBackgroundResource(R.color.bb_common_font_color_ccc);
			}
		}
		if(mListener != null) {
			mListener.onTabSelected(tabNameSelected);
		}
	}
}
