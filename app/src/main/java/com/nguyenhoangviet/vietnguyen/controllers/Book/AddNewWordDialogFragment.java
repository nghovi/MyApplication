package com.nguyenhoangviet.vietnguyen.controllers.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nguyenhoangviet.vietnguyen.core.controller.DialogBuilder;
import com.nguyenhoangviet.vietnguyen.core.controller.MyDialogFragment;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

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

		TextView txtDone = (TextView)view.findViewById(R.id.txt_dialog_with_2edt_done);
		txtDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dismiss();
				listener.onClickDone(edt.getText().toString(), edt2.getText().toString());
			}
		});

		TextView txtCancel = (TextView)view.findViewById(R.id.txt_dialog_with_2edt_cancel);
		txtCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickCancel();
			}
		});

		TextView txtNextWord = (TextView)view.findViewById(R.id.txt_dialog_with_2edt_next_word);
		txtNextWord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				listener.onClickDone(edt.getText().toString(), edt2.getText().toString());
				edt.setText("");
				edt2.setText("");
			}
		});

		TextView txtNextPhrase = (TextView)view.findViewById(R.id.dialog_width_2edt_next_phrase);
		txtNextPhrase.setOnClickListener(new View.OnClickListener() {

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
