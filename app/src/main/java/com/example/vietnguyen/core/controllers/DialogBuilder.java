package com.example.vietnguyen.core.controllers;

import android.app.Activity;
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

import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by viet on 9/8/2015.
 */

public class DialogBuilder implements DialogInterface.OnDismissListener{

	private Activity	activity;
	private Dialog		dlg;

	public interface OnNumberPickerBtnOkClickListener{

		public void onClick(int selectedValue, String displayedValue);
	}

	public DialogBuilder(Activity activity){
		this.activity = activity;
	}

	public Dialog buildConfirmDlgTopDown(String cancel, String confirm, View.OnClickListener confirmListener){
		dlg = new Dialog(this.activity, R.style.dialog_slide_anim_top_down);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);// hidden the space grabbed by title
		dlg.setContentView(R.layout.dialog_topdown_2_options);

		TextView txtOption1 = (TextView)dlg.findViewById(R.id.txt_option1);
		txtOption1.setText(cancel);
		setListenerFor(txtOption1, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
			}
		});

		TextView txtOption2 = (TextView)dlg.findViewById(R.id.txt_option2);
		txtOption2.setText(confirm);
		setListenerFor(txtOption2, confirmListener);

		dlg.getWindow().getAttributes().gravity = Gravity.TOP | Gravity.LEFT;
		dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		return dlg;
	}

	public Dialog build2OptsDlgTopDown(String opt1, String opt2, View.OnClickListener listener1, View.OnClickListener listener2){
		dlg = new Dialog(this.activity, R.style.dialog_slide_anim_top_down);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);// hidden the space grabbed by title
		dlg.setContentView(R.layout.dialog_topdown_2_options);

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

	public Dialog build3OptsDlgTopDown(String opt1, String opt2, String opt3, final OnNumberPickerBtnOkClickListener listener){
		dlg = new Dialog(this.activity, R.style.dialog_slide_anim_top_down);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);// hidden the space grabbed by title
		dlg.setContentView(R.layout.dialog_topdown_3_options);

		View.OnClickListener myListener = new View.OnClickListener() {

			@Override
			public void onClick(View view){
				int option = (int)view.getTag();
				listener.onClick(option, ((TextView)view).getText().toString());
			}
		};

		TextView txtOption1 = (TextView)dlg.findViewById(R.id.txt_option1);
		txtOption1.setText(opt1);
		txtOption1.setTag(0);
		setListenerFor(txtOption1, myListener);

		TextView txtOption2 = (TextView)dlg.findViewById(R.id.txt_option2);
		txtOption2.setText(opt2);
		txtOption2.setTag(1);
		setListenerFor(txtOption2, myListener);

		TextView txtOption3 = (TextView)dlg.findViewById(R.id.txt_option3);
		txtOption3.setText(opt3);
		txtOption3.setTag(2);
		setListenerFor(txtOption3, myListener);

		dlg.getWindow().getAttributes().gravity = Gravity.TOP | Gravity.LEFT;
		dlg.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		return dlg;
	}

	public Dialog buildDialogNumberPicker(Context activity, String title, final String[] displayedValue, final OnNumberPickerBtnOkClickListener listener, int defaultValue){
		dlg = new Dialog(this.activity);
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

	public Dialog buildDialogNotice(Context activity, String title, String msg, int autoDissmissTime){
		dlg = new Dialog(activity);
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

	public Dialog updateDialogNotice(Dialog dlg, String title, String msg, int autoDismissTimeSec){
		dlg.setTitle(title);
		((TextView)dlg.findViewById(R.id.dlg_notice_msg)).setText(msg);
		setAutoDismiss(dlg, autoDismissTimeSec);
		return dlg;
	}

	public Dialog build2OptionsDialog(Context activity, String title, String msg, String right, final View.OnClickListener listenerRight){
		dlg = new Dialog(this.activity);
		// dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setTitle(title);
		dlg.setContentView(R.layout.dialog_2options);
		TextView txtMsg = (TextView)dlg.findViewById(R.id.txt_dialog_2_options_message);
		txtMsg.setText(msg);

		Button btnOk = (Button)dlg.findViewById(R.id.btn_dialog_2_options_left);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
			}
		});

		Button btnRight = (Button)dlg.findViewById(R.id.btn_dialog_2_options_right);
		btnRight.setText(right);
		btnRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
				listenerRight.onClick(view);
			}
		});
		return dlg;
	}

	public interface OnDialogWithEdtDismiss{

		public void onClickDone(String input1, String input2);
	}

	public Dialog buildDialogWithEdt(Context activity, String hint, String message, final OnDialogWithEdtDismiss listener){
		dlg = new Dialog(this.activity);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setContentView(R.layout.dialog_with_edt);

		final EditText edt = (EditText)dlg.findViewById(R.id.edt_dialog_with_edt);
		edt.setHint(hint);
		if(!MU.isEmpty(message)){
			edt.setText(message);
		}
		// edt.setMinLines(1);
		// edt.setMaxLines(9);

		TextView txtDone = (TextView)dlg.findViewById(R.id.txt_dialog_with_edt_done);
		txtDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
				listener.onClickDone(edt.getText().toString(), null);
			}
		});

		TextView txtCancel = (TextView)dlg.findViewById(R.id.txt_dialog_with_edt_cancel);
		txtCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
			}
		});

		return dlg;
	}

	public Dialog buildDialogWith2Edt(Context activity, String hint, String hint2, final OnDialogWithEdtDismiss listener){
		dlg = new Dialog(this.activity);
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setContentView(R.layout.dialog_with_2edt);

		final EditText edt = (EditText)dlg.findViewById(R.id.edt_dialog_with_2edt_word);
		edt.setHint(hint);

		final EditText edt2 = (EditText)dlg.findViewById(R.id.edt_dialog_with_2edt_phrase);
		edt2.setHint(hint2);

		TextView txtDone = (TextView)dlg.findViewById(R.id.txt_dialog_with_2edt_done);
		txtDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
				listener.onClickDone(edt.getText().toString(), edt2.getText().toString());
			}
		});

		TextView txtCancel = (TextView)dlg.findViewById(R.id.txt_dialog_with_2edt_cancel);
		txtCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
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
