package com.example.vietnguyen.controllers.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.activeandroid.query.Select;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.MyTextView;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BookSearchFragment extends MyFragment{

	public static final String	KEY_BOOK_SEARCH_WORD		= "book_search_by_word";
	public static final String	KEY_BOOK_SEARCH_NAME		= "book_search_by_name";
	public static final String	KEY_BOOK_SEARCH_AUTHOR		= "book_search_by_author";
	public static final String	KEY_BOOK_SEARCH_COMMENT		= "book_search_by_comment";
	public static final String	KEY_BOOK_SEARCH_CONDITION	= "book_search_condition";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_search, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildPreCondition();
		buildOnCancelSearch();
		buildOnClickSearch();
	}

	private void buildOnClickSearch(){
		setOnClickFor(R.id.txt_fragment_book_search_search, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickSearchText();
			}
		});
		MyTextView.OnKeyboardBtnPressed listener = new MyTextView.OnKeyboardBtnPressed() {

			@Override
			public void onPress(String text){
				onClickSearchText();
			}
		};
		setOnEditorActionFor(R.id.edt_fragment_book_search_word, EditorInfo.IME_ACTION_SEARCH, listener);
		setOnEditorActionFor(R.id.edt_fragment_book_search_author, EditorInfo.IME_ACTION_SEARCH, listener);
		setOnEditorActionFor(R.id.edt_fragment_book_search_name, EditorInfo.IME_ACTION_SEARCH, listener);
		setOnEditorActionFor(R.id.edt_fragment_book_search_comment, EditorInfo.IME_ACTION_SEARCH, listener);
	}

	private void buildOnCancelSearch(){
		setOnClickFor(R.id.txt_fragment_book_search_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				setTextFor(R.id.edt_fragment_book_search_word, "");
				setTextFor(R.id.edt_fragment_book_search_name, "");
				setTextFor(R.id.edt_fragment_book_search_author, "");
				setTextFor(R.id.edt_fragment_book_search_comment, "");
				onClickSearchText();
			}
		});
	}

	private void buildPreCondition(){
		Map<String, String> conditions = (Map<String, String>)getUpdatedData(KEY_BOOK_SEARCH_CONDITION, new HashMap<String, String>());
		if(conditions.containsKey(KEY_BOOK_SEARCH_WORD)){
			setTextFor(R.id.edt_fragment_book_search_word, conditions.get(KEY_BOOK_SEARCH_WORD));
		}
		if(conditions.containsKey(KEY_BOOK_SEARCH_NAME)){
			setTextFor(R.id.edt_fragment_book_search_name, conditions.get(KEY_BOOK_SEARCH_NAME));
		}
		if(conditions.containsKey(KEY_BOOK_SEARCH_AUTHOR)){
			setTextFor(R.id.edt_fragment_book_search_author, conditions.get(KEY_BOOK_SEARCH_AUTHOR));
		}
		if(conditions.containsKey(KEY_BOOK_SEARCH_COMMENT)){
			setTextFor(R.id.edt_fragment_book_search_comment, conditions.get(KEY_BOOK_SEARCH_COMMENT));
		}
	}

	public static List<Book> searchWord(String word){
		List<Book> books = new Select().from(Book.class).execute();
		Iterator<Book> ib = books.iterator();
		while(ib.hasNext()){
			Book book = ib.next();
			if(!MU.isEmpty(word) && !book.hasWord(word)){
				ib.remove();
			}
		}
		return books;
	}

	private void onClickSearchText(){
		MU.hideSofeKeyboard(activity);
		String word = getEditText(R.id.edt_fragment_book_search_word).getText().toString();
		String name = getEditText(R.id.edt_fragment_book_search_name).getText().toString();
		String author = getEditText(R.id.edt_fragment_book_search_author).getText().toString();
		String comment = getEditText(R.id.edt_fragment_book_search_comment).getText().toString();
		activity.backToFragment(BookListFragment.class, KEY_BOOK_SEARCH_CONDITION, buildSearchConditions(word, name, author, comment));
	}

	private Map<String, Object> buildSearchConditions(String word, String name, String author, String comment){
		Map<String, Object> conditions = new HashMap<String, Object>();
		if(MU.isEmpty(word) && MU.isEmpty(name) && MU.isEmpty(author) && MU.isEmpty(comment)){
			return conditions;
		}
		conditions.put(KEY_BOOK_SEARCH_WORD, word);
		conditions.put(KEY_BOOK_SEARCH_NAME, name);
		conditions.put(KEY_BOOK_SEARCH_AUTHOR, author);
		conditions.put(KEY_BOOK_SEARCH_COMMENT, comment);

		return conditions;
	}
}
