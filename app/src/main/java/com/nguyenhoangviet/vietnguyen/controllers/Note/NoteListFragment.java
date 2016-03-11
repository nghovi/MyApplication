package com.nguyenhoangviet.vietnguyen.controllers.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragmentWithList;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.network.UrlBuilder;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.core.views.adapters.MyArrayAdapter;
import com.nguyenhoangviet.vietnguyen.core.views.widgets.MyTextView;
import com.nguyenhoangviet.vietnguyen.models.MyModel;
import com.nguyenhoangviet.vietnguyen.models.Note;
import com.nguyenhoangviet.vietnguyen.myapplication.R;
import com.nguyenhoangviet.vietnguyen.views.widgets.notifications.adapters.adapters.NoteListAdapter;

import org.json.JSONObject;

public class NoteListFragment extends MyFragmentWithList implements NoteListAdapter.OnCheckItemListener{

	private boolean			isSearching	= false;
	protected List<Note>	models;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_note_list, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildEditNoteFunction();
		buildAddNoteFunction();
		buildSearchFunction();
		sendGetNotesApi();
	}

	@Override
	protected void onClickItem(final MyModel model){
		hideSofeKeyboard();
		activity.addFragment(new NoteDetailFragment(), NoteDetailFragment.BUNDLE_KEY_NOTE, (Note)model);
	}

	// // TODO: 3/7/2016 when empty note list, do not show EDIT Button
	private void buildEditNoteFunction(){
		setOnClickFor(R.id.txt_fragment_note_list_edit, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(((TextView)view).getText().toString().equals(getString(R.string.edit))){
					onClickTextEdit();
				}else if(((TextView)view).getText().toString().equals(getString(R.string.done))){
					onClickTextDone();
				}
			}
		});
	}

	private void onClickTextEdit(){
		adapter.setMode(MyArrayAdapter.MODE_EDITING);
		adapter.notifyDataSetChanged();
		setTextFor(R.id.txt_fragment_note_list_edit, getString(R.string.done));
		setTextFor(R.id.txt_fragment_note_list_add, getString(R.string.delete_all));
		goneView(R.id.img_fragment_note_list_search);
	}

	// Don't edit anymore
	private void onClickTextDone(){
		adapter.setMode(MyArrayAdapter.MODE_NORMAL);
		adapter.notifyDataSetChanged();
		setTextFor(R.id.txt_fragment_note_list_edit, "Edit");
		setTextFor(R.id.txt_fragment_note_list_add, "Add");
		visibleView(R.id.img_fragment_note_list_search);
		sendGetNotesApi();
	}

	private void buildAddNoteFunction(){
		setOnClickFor(R.id.txt_fragment_note_list_add, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(((TextView)view).getText().toString().equals(getString(R.string.add))){
					onClickTextAdd();
				}else if(((TextView)view).getText().toString().contains(getString(R.string.delete))){
					onClickTextDelete();
				}
			}
		});
	}

	private void onClickTextAdd(){
		activity.addFragment(new NoteDetailFragment());
	}

	private void onClickTextDelete(){
		dlgBuilder.buildConfirmDlgTopDown(getString(R.string.cancel), getString(R.string.delete), new View.OnClickListener() {

			@Override
			public void onClick(View view){
				confirmDeleteNotes();
			}
		}).show();
	}

	private void confirmDeleteNotes(){
		boolean isDeleteAll = getTextView(R.id.txt_fragment_note_list_add).getText().toString().equals(getString(R.string.delete_all));
		List<String> deletedNoteIds;
		deletedNoteIds = new ArrayList<>();
		for(int i = 0; i < models.size(); i++){
			View viewNote = listView.getChildAt(i);
			if(viewNote != null){
				CheckBox checkBox = (CheckBox)viewNote.findViewById(R.id.item_note_checkbox);
				if(checkBox.isChecked() || isDeleteAll){
					deletedNoteIds.add(models.get(i).id);
				}
			}
		}
		sendDeleteNotes(TextUtils.join(",", deletedNoteIds));
	}

	/**
	 * @param noteIds @String list of note id separated by comma
	 */
	public void sendDeleteNotes(String noteIds){
		callApi(UrlBuilder.deleteNote(noteIds), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSendDeleteNoteSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});
	}

	private void onSendDeleteNoteSuccess(JSONObject response){
		List<String> deletedNoteIds = MU.convertToModelList(response.optString("data"), String.class);
		Iterator<Note> in = models.iterator();
		while(in.hasNext()){
			Note note = in.next();
			if(deletedNoteIds.contains(note.id)){
				in.remove();
			}
		}
		adapter.updateDataWith(models);
	}

	private void buildSearchFunction(){
		setOnClickFor(R.id.img_fragment_note_list_search, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickSearchIcon();
			}
		});

		setAfterTextChangedListenerFor(R.id.edt_fragment_note_list_search, new MyTextView.OnAfterTextChangedListener() {

			@Override
			public void afterTextChanged(String before, String after){
				performSearch(after);
			}
		});
	}

	private void onClickSearchIcon(){
		if(isSearching){
			hideSofeKeyboard();
			cancelSearching();
			getMainActivity().footer.show();
		}else{
			isSearching = true;
			getMainActivity().footer.hide();
			goneView(R.id.txt_fragment_note_list_edit);
			goneView(R.id.lnr_fragment_note_list_add);
			visibleView(R.id.edt_fragment_note_list_search);
			getEditText(R.id.edt_fragment_note_list_search).requestFocus();
			showSofeKeyboard();
			setImageResourceFor(R.id.img_fragment_note_list_search, R.drawable.nav_btn_search_active);
		}
	}

	private void cancelSearching(){
		isSearching = false;
		visibleView(R.id.txt_fragment_note_list_edit);
		visibleView(R.id.lnr_fragment_note_list_add);
		goneView(R.id.edt_fragment_note_list_search);
		setImageResourceFor(R.id.img_fragment_note_list_search, R.drawable.nav_btn_search_inactive);
		hideSofeKeyboard();
		sendGetNotesApi();
	}

	private void performSearch(String keyword){
		// todo why after back from note detail of search, still call perform Search ?
		if(getEditText(R.id.edt_fragment_note_list_search).getVisibility() != View.VISIBLE){
			return;
		}
		for(MyModel note : models){
			if(!((Note)note).content.contains(keyword)){
				adapter.removeDataItem(note);
			}else{
				adapter.checkToAddDataItem(note);
			}
		}
		adapter.notifyDataSetChanged();
	}

	private void sendGetNotesApi(){
		callApi(UrlBuilder.noteList(null), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSendGetNotesApiSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});
	}

	private void onSendGetNotesApiSuccess(JSONObject response){
		models = MU.convertToModelList(response.optString("results"), Note.class);
		adapter.updateDataWith(models);
	}

	@Override
	public void onEmptyData(){
		super.onEmptyData();
		visibleView(R.id.txt_fragment_note_list_empty);
	}

	@Override
	public void onNotEmptyData(){
		super.onNotEmptyData();
		goneView(R.id.txt_fragment_note_list_empty);
	}

	@Override
	public void onChecked(Note checkedNote, int numItemChecked){
		if(numItemChecked > 0){
			setTextFor(R.id.txt_fragment_note_list_add, getString(R.string.delete));
		}else{
			setTextFor(R.id.txt_fragment_note_list_add, getString(R.string.delete_all));
		}
	}

	@Override
	public int getListViewId(){
		return R.id.lst_fragment_note_search_result_note;
	}

	@Override
	public void initAdapter(){
		adapter = new NoteListAdapter(activity, R.layout.item_note, this);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

}
