package com.example.vietnguyen.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
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
import java.util.List;

public class BookListFragment extends MyFragment{

	private List<Book>	books;
	private BookListAdapter bookListAdapter;
	private ListView	lstBook;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_list, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		setOnClickFor(R.id.txt_fbl_add, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				activity.addFragment(new BookAddFragment());
			}
		});
		lstBook = getListView(R.id.lst_fbl_book_list);
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
		books = new Select().from(Book.class).execute();

		bookListAdapter = new BookListAdapter(activity, R.layout.item_book, new ArrayList<Book>(books));
		lstBook.setAdapter(bookListAdapter);
	}

	private void loadBookFromServer(){
		JSONObject params = new JSONObject();
		getApi(Const.GET_BOOKS, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response){
				books = (ArrayList<Book>)MU.convertToModelList(response.optString("data"), Book.class);
				saveBookToLocal(books);
				loadBookFromLocal();
			}

			@Override
			public void onApiError(String errorMsg){
				showShortToast("Get books from server failed");
			}
		});
	}

	public void saveBookToLocal(List<Book> bookList){
		new Delete().from(Book.class).execute();
		for(Book b : bookList){
			b.save();
		}
	}

	public void gotoBookDetail(Book book){
		BookDetailFragment frg = new BookDetailFragment();
		frg.setBook(book);
		activity.addFragment(frg);
	}
}
