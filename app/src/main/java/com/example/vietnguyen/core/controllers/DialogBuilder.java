package com.example.vietnguyen.core.controllers;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;

/**
 * Created by viet on 9/8/2015.
 */

public class DialogBuilder{

	private Context	context;
	private Dialog	dlg;

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

	private void setListenerFor(View v, final View.OnClickListener listener){
		v.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlg.dismiss();
				if(listener != null) listener.onClick(view);
			}
		});
	}

	public void dismiss(){
		this.dlg.dismiss();
	}
}
