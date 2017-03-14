package com.nguyenhoangviet.vpcorp_purchase.controllers.Note;

import java.util.Date;

import com.activeandroid.query.Select;
import com.nguyenhoangviet.vpcorp_purchase.core.controller.MyFragmentWithList;
import com.nguyenhoangviet.vpcorp_purchase.core.utils.MU;
import com.nguyenhoangviet.vpcorp_purchase.core.views.adapters.MyArrayAdapter;
import com.nguyenhoangviet.vpcorp_purchase.core.views.widgets.MyTextView;
import com.nguyenhoangviet.vpcorp_purchase.models.MyModel;
import com.nguyenhoangviet.vpcorp_purchase.models.Note;
import com.nguyenhoangviet.vpcorp_purchase.myapplication.R;
import com.nguyenhoangviet.vpcorp_purchase.views.widgets.notifications.adapters.adapters.NoteListAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

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
		loadAds();
	}

	@Override
	protected void buildHeader(){
		// // TODO: 3/3/2017 not good name class HeaderFooter, WithList maybe interface is better
	}

	@Override
	protected void onClickItem(final MyModel model){
		hideSofeKeyboard();
		activity.addFragment(new NoteDetailFragment(), NoteDetailFragment.BUNDLE_KEY_NOTE, (Note)model);
	}

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
		reloadNotes();
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
