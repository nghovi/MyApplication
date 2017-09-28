package com.nguyenhoangviet.vpcorp.controllers.Note;

import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenhoangviet.vpcorp.controllers.FragmentOfMainActivity;
import com.nguyenhoangviet.vpcorp.core.utils.MU;
import com.nguyenhoangviet.vpcorp.models.Note;
import com.nguyenhoangviet.vpcorp.myapplication.R;

public class NoteDetailFragment extends FragmentOfMainActivity{

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
			public void onClick(View view){
				saveAndBack();
			}
		});
		getMainActivity().footer.hide();
	}

	@Override
	protected void onClickBackBtn(){
		if(!note.message.equals(getEditText(R.id.edt_fragment_note_detail).getText().toString())){
			dlgBuilder.build2OptsDlgTopDown(getString(R.string.save), getString(R.string.cancel), new View.OnClickListener() {

				@Override
				public void onClick(View view){
					saveAndBack();
				}
			}, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					hideSofeKeyboard();
					activity.backOneFragment();
				}
			}).show();
		}else{
			hideSofeKeyboard();
			activity.backOneFragment();
		}
	}

	private void saveAndBack(){
		hideSofeKeyboard();
		note.message = getEditText(R.id.edt_fragment_note_detail).getText().toString();
		if(MU.isEmpty(note.message)){
//			note.delete();
		}else{
			note.date = new Date();
//			note.save();
		}
		activity.backOneFragment();
	}

}
