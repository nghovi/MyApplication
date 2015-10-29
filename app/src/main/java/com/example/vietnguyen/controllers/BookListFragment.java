package com.example.vietnguyen.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.BookAdapter;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class BookListFragment extends MyFragment{

	private ArrayList<Book>	books;
	private BookAdapter		bookAdapter;
	private ListView		lstBook;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_list, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		// ArrayList<Book> books = new ArrayList<Book>();
		// books.add(new Book(0, "abcd1", "Shacksepear", "Romeo and J1uliet", "The Titan's Curse", Arrays.asList("incre2dible", "what is2 it",
		// "why23 not me"), "mood", "link"));
		// books.add(new Book(1, "abcd2", "Shacksepear", "Romeo and Jul2iet", "The Lightning Thief", Arrays.asList("incredi12ble", "what 23is it",
		// "why 2not me"), "mood2", "link2"));
		// books.add(new Book(2, "abcd3", "Shacksepear", "Romeo and J3uliet", "The Sea Of Monster", Arrays.asList("incredi2ble", "what is 23it",
		// "why n7ot me"), "mood3", "link3"));
		lstBook = getListView(R.id.lst_book);
		lstBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				Book book = (Book)adapterView.getItemAtPosition(i);
				gotoBookDetail(book);
			}
		});
		loadBookFromLocal();
		loadBookFromServer();
	}

	private void loadBookFromLocal(){
		books = new ArrayList<Book>();
		Iterator<Book> bookIterator = Book.findAll(Book.class);
		while(bookIterator.hasNext()){
			books.add(bookIterator.next());
		}
		bookAdapter = new BookAdapter(activity, R.layout.item_book, books);
		lstBook.setAdapter(bookAdapter);
	}

	private void loadBookFromServer(){
		JSONObject params = new JSONObject();
		getApi(Const.GET_BOOKS, params);
	}

	@Override
	public void onApiResponse(String url, JSONObject response){
		super.onApiResponse(url, response);
		switch(url){
		case Const.GET_BOOKS:
			books = (ArrayList<Book>)MU.convertToModelList(response.optString("data"), Book.class);
			saveBookToLocal(books);
			loadBookFromLocal();
			break;
		default:
			break;
		}
	}

	public void saveBookToLocal(ArrayList<Book> bookList){
		Book.deleteAll(Book.class);
		for(Book b : bookList){
			b.setId(null); // todo why have to setId = null, Task doesn't need it
			b.save();
		}
	}

	@Override
	public void onApiError(String url, String errorMsg){
		super.onApiError(url, errorMsg);
		switch(url){
		case Const.GET_BOOKS:
			showShortToast("Get books from server failed");
			break;
		}
	}

	public void gotoBookDetail(Book book){
		BookDetailFragment frg = new BookDetailFragment();
		frg.setBook(book);
		activity.addFragment(frg);
	}
}
