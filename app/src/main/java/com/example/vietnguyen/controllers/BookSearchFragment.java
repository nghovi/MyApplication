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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class BookSearchFragment extends MyFragment{

	private List<Book>	books;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_search, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		setOnClickFor(R.id.txt_fragment_book_search_search, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickSearchText();
			}
		});

	}

	private void onClickSearchText() {
		String word = getEditText(R.id.edt_fragment_book_search_word).getText().toString();
		String name = getEditText(R.id.edt_fragment_book_search_name).getText().toString();
		String  author = getEditText(R.id.edt_fragment_book_search_author).getText().toString();
		String comment = getEditText(R.id.edt_fragment_book_search_comment).getText().toString();

		books = new Select().from(Book.class).execute();
		Iterator<Book> ib = books.iterator();
		while(ib.hasNext()) {
			Book book = ib.next();
			if (!MU.isEmpty(word) && !book.hasWord(word)) {
				ib.remove();
			}
			if (!MU.isEmpty(name) && Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE).matcher(book.name).find()) {
				ib.remove();
			}
			if (!MU.isEmpty(author) && Pattern.compile(Pattern.quote(author), Pattern.CASE_INSENSITIVE).matcher(book.author).find()) {
				ib.remove();
			}
			if (!MU.isEmpty(comment) && Pattern.compile(Pattern.quote(comment), Pattern.CASE_INSENSITIVE).matcher(book.comment).find()) {
				ib.remove();
			}
		}

		activity.addFragment(new BookSearchResultFragment(), BookSearchResultFragment.KEY_BOOK_SEARCH_RESULT, books);
	}
}
