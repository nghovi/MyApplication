package com.nguyenhoangviet.vpcorp.controllers.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nguyenhoangviet.vpcorp.core.controller.DialogBuilder;
import com.nguyenhoangviet.vpcorp.core.controller.MyDialogFragment;
import com.nguyenhoangviet.vpcorp.vnote2.R;

/**
 * Created by viet on 1/18/2016.
 */
public class AddNewWordDialogFragment extends MyDialogFragment{

	public static final String						WORD_HINT1		= "AddNewWordDialogFragment_hint1";
	public static final String						WORD_HINT2		= "AddNewWordDialogFragment_hint2";
	public static final String						WORD_MESSAGE	= "AddNewWordDialogFragment_message";

	private DialogBuilder.OnDialogWithEdtDismiss	listener;

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	public AddNewWordDialogFragment(){
		// Empty constructor required for DialogFragment
	}

	public void setOnDialogWithEdtDismissListener(DialogBuilder.OnDialogWithEdtDismiss listener){
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.dialog_with_2edt, container);

		final EditText edt = (EditText)view.findViewById(R.id.edt_dialog_with_2edt_word);
		edt.setHint(getArguments().getString(WORD_HINT1));

		final EditText edt2 = (EditText)view.findViewById(R.id.edt_dialog_with_2edt_phrase);
		edt2.setHint(getArguments().getString(WORD_HINT2));

		Button btnDone = (Button)view.findViewById(R.id.btn_dialog_with_2edt_done);
		btnDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dismiss();
				listener.onClickDone(edt.getText().toString(), edt2.getText().toString());
			}
		});

		Button btnCancel = (Button)view.findViewById(R.id.btn_dialog_with_2edt_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickCancel();
			}
		});

		Button btnNextWord = (Button)view.findViewById(R.id.btn_dialog_with_2edt_next_word);
		btnNextWord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				listener.onClickDone(edt.getText().toString(), edt2.getText().toString());
				edt.setText("");
				edt2.setText("");
			}
		});

		Button btnNextPhrase = (Button)view.findViewById(R.id.btn_dialog_width_2edt_next_phrase);
		btnNextPhrase.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				listener.onClickDone(edt.getText().toString(), edt2.getText().toString());
				edt2.setText("");
			}
		});

		return view;
	}

	public void onClickCancel(){
		this.dismiss();
	}
}
