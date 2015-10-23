package com.example.vietnguyen.core.controllers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;

/**
 * Created by viet on 9/8/2015.
 */

public class DialogBuilder implements DialogInterface.OnDismissListener{

	private Context	context;
	private Dialog	dlg;

	public interface OnNumberPickerBtnOkClickListener{

		public void onClick(int selectedValue, String displayedValue);
	}

	public DialogBuilder(Context context){
		this.context = context;
	}

	public Dialog build2OptsDlgTopDown(String opt1, String opt2, View.OnClickListener listener1, View.OnClickListener listener2){
		dlg = new Dialog(this.context, R.style.dialog_slide_anim_top_down);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);// hidden the space grabbed by title
		dlg.setContentView(R.layout.dialog_2_options);

		TextView txtOption1 = (TextView)dlg.findViewById(R.id.txt_option1);
		txtOption1.setText(opt1);
		setListenerFor(txtOption1, listener1);

		TextView txtOption2 = (TextView)dlg.findViewById(R.id.txt_option2);
		txtOption2.setText(opt2);
		setListenerFor(txtOption2, listener2);

		dlg.getWindow().getAttributes().gravity = Gravity.TOP | Gravity.LEFT;
		dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		return dlg;
	}

	public Dialog buildDialogNumberPicker(Context context, String title, final String[] displayedValue, final OnNumberPickerBtnOkClickListener listener, int defaultValue){
		dlg = new Dialog(this.context);
		dlg.setContentView(R.layout.dialog_number_picker);
		dlg.setTitle(title);
		final NumberPicker numberPicker = (NumberPicker)dlg.findViewById(R.id.dlg_number_picker);
		numberPicker.setDisplayedValues(displayedValue);
		numberPicker.setWrapSelectorWheel(true);
		numberPicker.setMinValue(0);
		numberPicker.setMaxValue(displayedValue.length - 1);
		numberPicker.setValue(defaultValue);
		Button btnOk = (Button)dlg.findViewById(R.id.dlg_number_picker_btn_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
				int selectedValue = numberPicker.getValue();
				listener.onClick(selectedValue, displayedValue[selectedValue]);
			}
		});
		return dlg;
	}

	private void setListenerFor(View v, final View.OnClickListener listener){
		v.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
				if(listener != null) listener.onClick(view);
			}
		});
	}

	@Override
	public void onDismiss(DialogInterface dialogInterface){

	}
}
