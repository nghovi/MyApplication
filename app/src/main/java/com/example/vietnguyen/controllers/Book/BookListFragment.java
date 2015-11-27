package com.example.vietnguyen.controllers.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.BookListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListFragment extends MyFragment{

	private List<Book>			books;
	private BookListAdapter		bookListAdapter;
	private ListView			lstBook;
	private Map<String, Object>	searchCondition;

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

	private void buildListBook(){
		lstBook = getListView(R.id.lst_fragment_book_search_result_book);
		lstBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				Book book = (Book)adapterView.getItemAtPosition(i);
				gotoBookDetail(book);
			}
		});
	}

	private void buildAddBookFunction(){
		setOnClickFor(R.id.txt_fragment_book_list_add, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				activity.addFragment(new BookAddFragment());
			}
		});
	}

	private void buildSearchFunction(){
		setOnClickFor(R.id.img_fragment_book_list_search, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickSearchIcon();
			}
		});
		Map<String, Object> searchResult = (Map<String, Object>)getUpdatedData(BookSearchFragment.KEY_BOOK_SEARCH_RESULT, new HashMap<String, Object>());
		if(searchResult.containsKey(BookSearchFragment.KEY_BOOK_SEARCH_FLAG) && (boolean)searchResult.get(BookSearchFragment.KEY_BOOK_SEARCH_FLAG)){
			setImageResourceFor(R.id.img_fragment_book_list_search, R.drawable.nav_btn_search_active);
			searchCondition = (Map<String, Object>)searchResult.get(BookSearchFragment.KEY_BOOK_SEARCH_CONDITION);
		}else if(searchResult.containsKey(BookSearchFragment.KEY_BOOK_SEARCH_FLAG) && !(boolean)searchResult.get(BookSearchFragment.KEY_BOOK_SEARCH_FLAG)){
			searchCondition = null;
			setImageResourceFor(R.id.img_fragment_book_list_search, R.drawable.nav_btn_search_inactive);
		}

		if(searchCondition != null){
			books = AbstractBookFragment.searchWithConditions(searchCondition);
		}else{
			books = Book.getAllUndeleted(Book.class);
		}
		showBooks();
	}

	private void onClickSearchIcon(){
		if(searchCondition != null){
			activity.addFragment(new BookSearchFragment(), BookSearchFragment.KEY_BOOK_SEARCH_CONDITION, searchCondition);
		}else{
			activity.addFragment(new BookSearchFragment());
		}
	}

	private void showBooks(){
		ArrayList bookArrays = new ArrayList<Book>(books);
		if(bookArrays.size() > 0){
			bookListAdapter = new BookListAdapter(activity, R.layout.item_book, new ArrayList<Book>(books));
			lstBook.setAdapter(bookListAdapter);
		}else{
			// todo
			MU.log("You should be a reader");
		}
	}

	public void gotoBookDetail(Book book){
		BookDetailFragment frg = new BookDetailFragment();
		frg.setBook(book);
		activity.addFragment(frg);
	}
}
