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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListFragment extends MyFragment{

	private List<Book>			books;
	private BookListAdapter		bookListAdapter;
	private ListView			lstBook;
	private Map<String, String>	searchCondition;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_list, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildAddBookFunction();
		buildListBook();
		buildSearchFunction();
	}

	private void buildListBook() {
		lstBook = getListView(R.id.lst_fragment_book_search_result_book);
		lstBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Book book = (Book) adapterView.getItemAtPosition(i);
				gotoBookDetail(book);
			}
		});
	}

	private void buildAddBookFunction() {
		setOnClickFor(R.id.txt_fragment_book_list_add, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				activity.addFragment(new AddBookFragment());
			}
		});
	}

	private void buildSearchFunction() {
		setOnClickFor(R.id.img_fragment_book_list_search, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				onClickSearchIcon();
			}
		});
		Map<String, Object> searchResult = (Map<String, Object>)getUpdatedData(BookSearchFragment.KEY_BOOK_SEARCH_RESULT, new HashMap<String, Object>());
		if(searchResult.containsKey(BookSearchFragment.KEY_BOOK_SEARCH_FLAG) && (boolean)searchResult.get(BookSearchFragment.KEY_BOOK_SEARCH_FLAG)){
			setImageResourceFor(R.id.img_fragment_book_list_search, R.drawable.nav_btn_search_active);
			books = (List<Book>)searchResult.get(BookSearchFragment.KEY_BOOK_SEARCH_LIST);
			searchCondition = (Map<String, String>)searchResult.get(BookSearchFragment.KEY_BOOK_SEARCH_CONDITION);
			showBooks();
		}else{
			searchCondition = null;
			setImageResourceFor(R.id.img_fragment_book_list_search, R.drawable.nav_btn_search_inactive);
			loadBookFromLocal();
			loadBookFromServer();
		}
	}

	private void onClickSearchIcon(){
		if(searchCondition != null){
			activity.addFragment(new BookSearchFragment(), BookSearchFragment.KEY_BOOK_SEARCH_CONDITION, searchCondition);
		}else{
			activity.addFragment(new BookSearchFragment());
		}
	}

	private void loadBookFromLocal(){
		books = new Select().from(Book.class).execute();
		showBooks();
	}

	private void showBooks(){
		ArrayList bookArrays = new ArrayList<Book>(books);
		if (bookArrays.size() > 0) {
			bookListAdapter = new BookListAdapter(activity, R.layout.item_book, new ArrayList<Book>(books));
			lstBook.setAdapter(bookListAdapter);
		} else {
			//todo
			MU.log("You should be a reader");
		}
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
		DetailBookFragment frg = new DetailBookFragment();
		frg.setBook(book);
		activity.addFragment(frg);
	}
}
