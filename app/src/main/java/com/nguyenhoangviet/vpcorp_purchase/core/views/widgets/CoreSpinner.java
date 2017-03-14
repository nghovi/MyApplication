package com.nguyenhoangviet.vpcorp_purchase.core.views.widgets;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.nguyenhoangviet.vpcorp_purchase.core.model.SpinnerItemModel;
import com.nguyenhoangviet.vpcorp_purchase.core.views.adapters.CoreSpinnerAdapter;

/**
 * Created by TrungND on 02/06/2015.
 */
public class CoreSpinner extends Spinner{

	private CoreSpinnerAdapter			adapter;
	private List<SpinnerItemModel>		spinnerItemModels;

	public Map<String, LinearLayout>	mTabs;

	public interface OnSpinnerItemSelected{

		public void onSpinnerItemSelected(SpinnerItemModel spinnerItemModel);
	}

	public CoreSpinner(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public CoreSpinner(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	public CoreSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public CoreSpinner(Context context){
		super(context);
	}

	public void initSpinner(Activity context, List<SpinnerItemModel> spinnerItemModels, final OnSpinnerItemSelected listener){
		this.spinnerItemModels = spinnerItemModels;
		adapter = new CoreSpinnerAdapter(context, spinnerItemModels);
		this.setAdapter(adapter);
		this.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				SpinnerItemModel spinnerItemModel = adapter.getItem(position);
				listener.onSpinnerItemSelected(spinnerItemModel);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent){

			}
		});
	}
}
