package com.example.vietnguyen.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.BookListAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class BookSearchFragment extends MyFragment{

	private List<Book>			books;

	public static final String	KEY_BOOK_SEARCH_RESULT		= "book_search_result";
	public static final String	KEY_BOOK_SEARCH_FLAG		= "book_search_flag";
	public static final String	KEY_BOOK_SEARCH_LIST		= "book_list";
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
		String word = getEditText(R.id.edt_fragment_book_search_word).getText().toString();
		String name = getEditText(R.id.edt_fragment_book_search_name).getText().toString();
		String author = getEditText(R.id.edt_fragment_book_search_author).getText().toString();
		String comment = getEditText(R.id.edt_fragment_book_search_comment).getText().toString();

		books = new Select().from(Book.class).execute();
		Iterator<Book> ib = books.iterator();
		while(ib.hasNext()){
			Book book = ib.next();
			if(!MU.isEmpty(word) && !book.hasWord(word)){
				ib.remove();
				continue;
			}
			if(!MU.isEmpty(name) && !MU.checkMatch(book.name, name)){
				ib.remove();
				continue;
			}
			if(!MU.isEmpty(author) && !MU.checkMatch(book.author, author)){
				ib.remove();
				continue;
			}
			if(!MU.isEmpty(comment) && !MU.checkMatch(book.comment, comment)){
				ib.remove();
				continue;
			}
		}

		activity.backToFragment(BookListFragment.class, KEY_BOOK_SEARCH_RESULT, buildSearchResult(word, name, author, comment));
	}

	private Map<String, Object> buildSearchResult(String word, String name, String author, String comment){
		boolean hasFiltered = !MU.isEmpty(word) || !MU.isEmpty(name) || !MU.isEmpty(author) || !MU.isEmpty(comment);
		Map searchResult = new HashMap<String, Object>();
		searchResult.put(KEY_BOOK_SEARCH_FLAG, hasFiltered);
		searchResult.put(KEY_BOOK_SEARCH_LIST, books);
		searchResult.put(KEY_BOOK_SEARCH_CONDITION, buildSearchConditions(word, name, author, comment));
		return searchResult;
	}

	private Map<String, String> buildSearchConditions(String word, String name, String author, String comment){
		Map<String, String> conditions = new HashMap<String, String>();
		conditions.put(KEY_BOOK_SEARCH_WORD, word);
		conditions.put(KEY_BOOK_SEARCH_NAME, name);
		conditions.put(KEY_BOOK_SEARCH_AUTHOR, author);
		conditions.put(KEY_BOOK_SEARCH_COMMENT, comment);
		return conditions;
	}
}
