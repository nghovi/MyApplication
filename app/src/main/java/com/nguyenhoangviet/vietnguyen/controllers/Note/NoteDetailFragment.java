package com.nguyenhoangviet.vietnguyen.controllers.Note;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.controllers.FragmentOfMainActivity;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragment;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Note;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import org.json.JSONObject;

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
		note = (Note)this.getUpdatedData(BUNDLE_KEY_NOTE, new Note("", Note.TYPE_MEMORY));
		setTextFor(R.id.txt_fragment_note_detail_date, MU.getDateTimeForDisplaying(Calendar.getInstance().getTime()));
		setTextFor(R.id.edt_fragment_note_detail, note.content);
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
		if(!note.content.equals(getEditText(R.id.edt_fragment_note_detail).getText().toString())){
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

	// // TODO: 3/8/2016 call sendEditNote to save each 5s
	public void sendAddNewNote(){
		callPostApi(Const.ADD_NOTE, getJsonBuilder().add("content", note.content).add("notetype", note.notetype).getJsonObj(), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSendAddNewNoteSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});
	}

	private void onSendAddNewNoteSuccess(JSONObject response){
		note = MU.convertToModel(response.optString("data"), Note.class);
		activity.backOneFragment();
	}

	private void sendEditNotes(){
		callPostApi(Const.EDIT_NOTE, getJsonBuilder().add("id", note.id).add("content", note.content).add("notetype", note.notetype).getJsonObj(), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSendEditNoteSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});
	}

	private void onSendEditNoteSuccess(JSONObject response){
		note = MU.convertToModel(response.optString("data"), Note.class);
		activity.backOneFragment();
	}

	private void saveAndBack(){
		hideSofeKeyboard();
		note.content = getEditText(R.id.edt_fragment_note_detail).getText().toString();
		if(MU.isNotEmpty(note.content)){
			if(note.id == null){
				sendAddNewNote();
			}else{
				sendEditNotes();
			}
		}
	}

}
