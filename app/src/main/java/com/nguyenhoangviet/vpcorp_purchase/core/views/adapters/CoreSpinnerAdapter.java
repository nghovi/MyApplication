package com.nguyenhoangviet.vpcorp_purchase.core.views.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nguyenhoangviet.vpcorp_purchase.core.model.SpinnerItemModel;
import com.nguyenhoangviet.vpcorp_purchase.myapplication.R;

/**
 * Created by TrungND on 10/23/2014.
 */
public class CoreSpinnerAdapter extends ArrayAdapter<SpinnerItemModel> {

	private final Activity context;
	private final List<SpinnerItemModel> spinnerItemModels;

	public CoreSpinnerAdapter(Activity context, List<SpinnerItemModel> spinnerItemModels){
		super(context, R.layout.item_spinner, R.id.txt_item_header_spinner_name, spinnerItemModels);
		this.context = context;
		this.spinnerItemModels = spinnerItemModels;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent){
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.item_spinner, null, true);
		SpinnerItemModel spinnerItemModel = getItem(position);
		TextView txtSpinnerItemName = (TextView)rowView.findViewById(R.id.txt_item_header_spinner_name);
		txtSpinnerItemName.setText(spinnerItemModel.getName());
		return rowView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.item_dropdown_spinner, null, true);
		SpinnerItemModel spinnerItemModel = getItem(position);
		TextView txtSpinnerItemName = (TextView)rowView.findViewById(R.id.txt_item_dropdown_header_spinner_name);
		txtSpinnerItemName.setText(spinnerItemModel.getName());
		return rowView;
	}
}
