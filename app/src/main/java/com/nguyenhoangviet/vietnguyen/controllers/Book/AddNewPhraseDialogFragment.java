package com.nguyenhoangviet.vietnguyen.controllers.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nguyenhoangviet.vietnguyen.core.controller.DialogBuilder;
import com.nguyenhoangviet.vietnguyen.core.controller.MyDialogFragment;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

/**
 * Created by viet on 1/18/2016.
 */
public class AddNewPhraseDialogFragment extends MyDialogFragment{

	public static final String						HINT	= "AddNewPhraseDialogFragment_hint";
	public static final String						MESSAGE	= "AddNewPhraseDialogFragment_message";

	private DialogBuilder.OnDialogWithEdtDismiss	listener;

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	public AddNewPhraseDialogFragment(){
		// Empty constructor required for DialogFragment
	}

	public void setOnDialogWithEdtDismissListener(DialogBuilder.OnDialogWithEdtDismiss listener){
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		View view = inflater.inflate(R.layout.dialog_with_edt, container);
		final EditText edt = (EditText)view.findViewById(R.id.edt_dialog_with_edt);
		edt.setHint(getArguments().getString(HINT));
		String message = getArguments().getString(MESSAGE);
		if(!MU.isEmpty(message)){
			edt.setText(message);
		}
		edt.setMinLines(1);
		edt.setMaxLines(9);

		Button btnDone = (Button) view.findViewById(R.id.btn_dialog_with_edt_done);
		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dismiss();
				listener.onClickDone(edt.getText().toString(), null);
			}
		});

		Button btnCancel = (Button) view.findViewById(R.id.btn_dialog_with_edt_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickCancel();
			}
		});

		Button txtContinue = (Button) view.findViewById(R.id.btn_dialog_with_edt_continue);
		txtContinue.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				listener.onClickDone(edt.getText().toString(), null);
				edt.setText("");
			}
		});

		return view;
	}

	public void onClickCancel(){
		this.dismiss();
	}
}
