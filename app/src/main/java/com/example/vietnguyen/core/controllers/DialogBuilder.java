package com.example.vietnguyen.core.controllers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.vietnguyen.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

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
		Button btnOk = (Button)dlg.findViewById(R.id.btn_dialog_number_picker_ok);
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

	public Dialog buildDialogNotice(Context context, String title, String msg, int autoDissmissTime){
		dlg = new Dialog(this.context);
		dlg.setContentView(R.layout.dialog_notice);
		dlg.setTitle(title);
		TextView txtMsg = (TextView)dlg.findViewById(R.id.dlg_notice_msg);
		txtMsg.setText(msg);

		Button btnOk = (Button)dlg.findViewById(R.id.btn_dialog_notice_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
			}
		});

		setAutoDismiss(dlg, autoDissmissTime);
		return dlg;
	}

	public interface OnDialogWithEdtDismiss{

		public void onClickDone(String input);
	}

	public Dialog buildDialogWithEdt(Context context, String header, String hint, final OnDialogWithEdtDismiss listener){
		dlg = new Dialog(this.context);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setContentView(R.layout.dialog_with_edt);

		TextView txtHeader = (TextView)dlg.findViewById(R.id.txt_dialog_with_edt_header);
		txtHeader.setText(header);

		final EditText edt = (EditText)dlg.findViewById(R.id.edt_dialog_with_edt);
		edt.setHint(hint);
//		edt.setMinLines(1);
//		edt.setMaxLines(9);

		TextView txtDone = (TextView)dlg.findViewById(R.id.txt_dialog_with_edt_done);
		txtDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
				listener.onClickDone(edt.getText().toString());
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

	private void setAutoDismiss(final Dialog dlg, int sec){
		final Timer t = new Timer();
		t.schedule(new TimerTask() {

			public void run(){
				if(dlg.isShowing()){
					dlg.dismiss();
					t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
				}
			}
		}, sec * 1000);
	}

	@Override
	public void onDismiss(DialogInterface dialogInterface){

	}
}
