package com.example.vietnguyen.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.BookListAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookSearchResultFragment extends MyFragment{

	private List<Book>	books;
	private BookListAdapter bookListAdapter;
	private ListView	lstBook;

	public static final String KEY_BOOK_SEARCH_RESULT = "book_list";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_search_result, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();

		books = (List<Book>)getUpdatedData(KEY_BOOK_SEARCH_RESULT, new ArrayList<Book>());
		bookListAdapter = new BookListAdapter(activity, R.layout.item_book, new ArrayList<Book>(books));
		lstBook = getListView(R.id.lst_fragment_book_search_result_book);
		lstBook.setAdapter(bookListAdapter);
		lstBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				Book book = (Book)adapterView.getItemAtPosition(i);
				gotoBookDetail(book);
			}
		});
	}

	public void gotoBookDetail(Book book){
		BookDetailFragment frg = new BookDetailFragment();
		frg.setBook(book);
		activity.addFragment(frg);
	}
}
