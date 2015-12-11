package com.example.vietnguyen.controllers.Note;

import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.vietnguyen.core.controller.MyFragmentWithList;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.adapters.MyArrayAdapter;
import com.example.vietnguyen.core.views.widgets.MyTextView;
import com.example.vietnguyen.models.MyModel;
import com.example.vietnguyen.models.Note;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.NoteListAdapter;

public class NoteListFragment extends MyFragmentWithList implements NoteListAdapter.OnCheckItemListener{

	private boolean	isSearching	= false;

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
		reloadNotes();
	}

	@Override
	protected void onClickItem(final MyModel model){
		activity.addFragment(new NoteDetailFragment(), NoteDetailFragment.BUNDLE_KEY_NOTE, (Note)model);
	}

	private void buildEditNoteFunction(){
		setOnClickFor(R.id.txt_fragment_note_list_edit, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(((TextView)view).getText().toString().equals("Edit")){
					onClickTextEdit();
				}else if(((TextView)view).getText().toString().equals("Done")){
					onClickTextDone();
				}
			}
		});
	}

	private void onClickTextEdit(){
		adapter.setMode(MyArrayAdapter.MODE_EDITING);
		adapter.notifyDataSetChanged();
		setTextFor(R.id.txt_fragment_note_list_edit, "Done");
		setTextFor(R.id.txt_fragment_note_list_add, "Delete All");
		goneView(R.id.img_fragment_note_list_search);
	}

	// Don't edit anymore
	private void onClickTextDone(){
		adapter.setMode(MyArrayAdapter.MODE_NORMAL);
		adapter.notifyDataSetChanged();
		setTextFor(R.id.txt_fragment_note_list_edit, "Edit");
		setTextFor(R.id.txt_fragment_note_list_add, "Add");
		visibleView(R.id.img_fragment_note_list_search);
		reloadNotes();
	}

	private void buildAddNoteFunction(){
		setOnClickFor(R.id.txt_fragment_note_list_add, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(((TextView)view).getText().toString().equals("Add")){
					onClickTextAdd();
				}else if(((TextView)view).getText().toString().contains("Delete")){
					onClickTextDelete();
				}
			}
		});
	}

	private void onClickTextAdd(){
		activity.addFragment(new NoteDetailFragment());
	}

	private void onClickTextDelete(){
		dlgBuilder.buildConfirmDlgTopDown("Cancel", "Delete", new View.OnClickListener() {

			@Override
			public void onClick(View view){
				confirmDeleteNotes();
			}
		}).show();
	}

	private void confirmDeleteNotes(){
		boolean isDeleteAll = getTextView(R.id.txt_fragment_note_list_add).getText().toString().equals("Delete All");
		for(int i = 0; i < models.size(); i++){
			View viewNote = listView.getChildAt(i);
			if(viewNote != null){
				CheckBox checkBox = (CheckBox)viewNote.findViewById(R.id.item_note_checkbox);
				if(checkBox.isChecked() || isDeleteAll){
					Note note = (Note)models.get(i);
					note.delete();
				}
			}
		}
		reloadNotes();
		onClickTextDone();
	}

	public void saveNewNotice(String message){
		if(!MU.isEmpty(message)){
			Date date = new Date();
			Note note = new Note(message, date);
			note.save();
			reloadNotes();
		}
	}

	public void saveOldNote(Note note, String message){
		if(!note.message.equals(message)){
			note.message = message;
			note.save();
			reloadNotes();
		}
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
			cancelSearching();
		}else{
			isSearching = true;
			goneView(R.id.txt_fragment_note_list_edit);
			goneView(R.id.txt_fragment_note_list_add);
			visibleView(R.id.edt_fragment_note_list_search);
			getEditText(R.id.edt_fragment_note_list_search).requestFocus();
			setImageResourceFor(R.id.img_fragment_note_list_search, R.drawable.nav_btn_search_active);
		}
	}

	private void cancelSearching(){
		isSearching = false;
		visibleView(R.id.txt_fragment_note_list_edit);
		visibleView(R.id.txt_fragment_note_list_add);
		goneView(R.id.edt_fragment_note_list_search);
		setImageResourceFor(R.id.img_fragment_note_list_search, R.drawable.nav_btn_search_inactive);
		MU.hideSofeKeyboard(activity);
		reloadNotes();
	}

	private void performSearch(String keyword){
		// todo why after back from note detail of search, still call perform Search ?
		if(getEditText(R.id.edt_fragment_note_list_search).getVisibility() != View.VISIBLE){
			return;
		}
		for(MyModel note : models){
			if(!((Note)note).message.contains(keyword)){
				adapter.removeDataItem(note);
			}else{
				adapter.checkToAddDataItem(note);
			}
		}
		adapter.notifyDataSetChanged();
	}

	private void reloadNotes(){
		models = new Select().from(Note.class).execute();
		adapter.updateDataWith(models);
	}

	@Override
	public void onChecked(Note checkedNote, int numItemChecked){
		if(numItemChecked > 0){
			setTextFor(R.id.txt_fragment_note_list_add, "Delete");
		}else{
			setTextFor(R.id.txt_fragment_note_list_add, "Delete All");
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