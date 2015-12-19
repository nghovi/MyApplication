package com.example.vietnguyen.controllers.Note;

import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vietnguyen.core.controller.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Note;
import com.example.vietnguyen.myapplication.R;

public class NoteDetailFragment extends MyFragment{

	private Note				note;
	public static final String	BUNDLE_KEY_NOTE	= "NOTE";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_note_detail, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		note = (Note)this.getUpdatedData(BUNDLE_KEY_NOTE, new Note("", new Date()));
		setTextFor(R.id.txt_fragment_note_detail_date, MU.getDateTimeForDisplaying(note.date));
		setTextFor(R.id.edt_fragment_note_detail, note.message);
		// if(MU.isEmpty(note.message)){
		// getEditText(R.id.edt_fragment_note_detail).requestFocus();
		// }
		setOnClickFor(R.id.txt_fragment_note_detail_done, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				saveAndBack();
			}
		});
	}

	@Override
	protected void onClickBackBtn(){
		if(!note.message.equals(getEditText(R.id.edt_fragment_note_detail).getText().toString())){
			dlgBuilder.build2OptsDlgTopDown("Save", "Cancel", new View.OnClickListener() {

				@Override
				public void onClick(View view){
					saveAndBack();
				}
			}, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					MU.hideSofeKeyboard(activity);
					activity.backOneFragment();
				}
			}).show();
		}else{
			MU.hideSofeKeyboard(activity);
			activity.backOneFragment();
		}
	}

	private void saveAndBack(){
		MU.hideSofeKeyboard(activity);
		note.message = getEditText(R.id.edt_fragment_note_detail).getText().toString();
		if (MU.isEmpty(note.message)) {
			note.delete();
		} else {
			note.date = new Date();
			note.save();
		}
		activity.backOneFragment();
	}

}
