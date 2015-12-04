package com.example.vietnguyen.controllers.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Note;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.NoteListAdapter;

public class NoteListFragment extends MyFragment implements NoteListAdapter.OnCheckItemListener{

	private List<Note>			notes;
	private NoteListAdapter		noteListAdapter;
	private ListView			lstNote;
	private Map<String, Object>	searchCondition;
	private boolean				isSearching	= false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_note_list, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildEditNoteFunction();
		buildAddNoteFunction();
		buildListNote();
		buildSearchFunction();
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
		noteListAdapter.isEditing = true;
		noteListAdapter.notifyDataSetChanged();
		setTextFor(R.id.txt_fragment_note_list_edit, "Done");
		setTextFor(R.id.txt_fragment_note_list_add, "Delete All");
		goneView(R.id.img_fragment_note_list_search);
	}

	// Don't edit anymore
	private void onClickTextDone(){
		noteListAdapter.isEditing = false;
		noteListAdapter.notifyDataSetChanged();
		setTextFor(R.id.txt_fragment_note_list_edit, "Edit");
		setTextFor(R.id.txt_fragment_note_list_add, "Add");
		visibleView(R.id.img_fragment_note_list_search);
		reloadNotes();
	}

	private void buildListNote(){
		lstNote = getListView(R.id.lst_fragment_note_search_result_note);
		lstNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				final Note note = (Note)adapterView.getItemAtPosition(i);
				dlgBuilder.buildDialogWithEdt(activity, "Enter message", note.message, new DialogBuilder.OnDialogWithEdtDismiss() {

					@Override
					public void onClickDone(String input1, String input2){
						saveOldNotice(note, input1);
					}
				}).show();
			}
		});
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
		dlgBuilder.buildDialogWithEdt(activity, "Enter message", null, new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input1, String input2){
				saveNewNotice(input1);
			}
		}).show();
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
		for(int i = 0; i < notes.size(); i++){
			View viewNote = lstNote.getChildAt(i);
			if(viewNote != null){
				CheckBox checkBox = (CheckBox)viewNote.findViewById(R.id.item_note_checkbox);
				if(checkBox.isChecked() || isDeleteAll){
					Note note = notes.get(i);
					note.delete();
					noteListAdapter.data.remove(note);
				}
			}
		}
		noteListAdapter.notifyDataSetChanged();
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

	public void saveOldNotice(Note note, String message){
		note.message = message;
		note.save();
		reloadNotes();
	}

	private void buildSearchFunction(){
		setOnClickFor(R.id.img_fragment_note_list_search, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickSearchIcon();
			}
		});

		getEditText(R.id.edt_fragment_note_list_search).setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
				if(actionId == EditorInfo.IME_ACTION_SEARCH){
					return true;
				}
				return false;
			}
		});
		getEditText(R.id.edt_fragment_note_list_search).addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void afterTextChanged(Editable editable){
				performSearch(editable.toString());
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
			setImageResourceFor(R.id.img_fragment_note_list_search, R.drawable.nav_btn_search_active);
		}
	}

	private void cancelSearching(){
		isSearching = false;
		visibleView(R.id.txt_fragment_note_list_edit);
		visibleView(R.id.txt_fragment_note_list_add);
		goneView(R.id.edt_fragment_note_list_search);
		setImageResourceFor(R.id.img_fragment_note_list_search, R.drawable.nav_btn_search_inactive);
		hideSofeKeyboard();
		reloadNotes();
	}

	private void hideSofeKeyboard(){
		View view = activity.getCurrentFocus();
		if(view != null){
			InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	private void performSearch(String keyword){
		for(Note note : notes){
			if(!note.message.contains(keyword)){
				noteListAdapter.data.remove(note);
			} else if (!noteListAdapter.data.contains(note)){
				noteListAdapter.data.add(note);
			}
		}
		noteListAdapter.notifyDataSetChanged();
	}

	private void reloadNotes(){
		notes = new Select().from(Note.class).execute();
		showNotes(notes);
	}

	private void showNotes(List<Note> showedNotes){
		if(showedNotes.size() > 0){
			lstNote.setVisibility(View.VISIBLE);
			if(noteListAdapter != null){
				noteListAdapter.data = showedNotes;
				noteListAdapter.notifyDataSetChanged();
			}else{
				List<Note> showedNotes //
				noteListAdapter = new NoteListAdapter(activity, R.layout.item_note, new List<Note>(showedNotes), this);
				lstNote.setAdapter(noteListAdapter);
			}
		}else{
			lstNote.setVisibility(View.GONE);
			// todo
			MU.log("You should be a reader");
		}
	}

	@Override
	public void onChecked(Note checkedNote, int numItemChecked){
		if(numItemChecked > 0){
			setTextFor(R.id.txt_fragment_note_list_add, "Delete");
		}else{
			setTextFor(R.id.txt_fragment_note_list_add, "Delete All");
		}
	}

	// public void gotoNoteDetail(Note note){
	// NoteDetailFragment frg = new NoteDetailFragment();
	// frg.setNote(note);
	// activity.addFragment(frg);
	// }
}
