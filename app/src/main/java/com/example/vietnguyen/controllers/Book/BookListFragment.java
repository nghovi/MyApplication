package com.example.vietnguyen.controllers.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.example.vietnguyen.controllers.Book.AbstractBookFragment;
import com.example.vietnguyen.controllers.Book.BookSearchFragment;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.controllers.MyFragmentWithList;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.models.MyModel;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.BookListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListFragment extends MyFragmentWithList{

	private List<Book>			books;
	private Map<String, Object>	searchCondition;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_list, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildAddBookFunction();
		buildSearchFunction();
		reloadBook();
	}

	@Override
	protected void onClickItem(MyModel model) {
		gotoBookDetail((Book) model);
	}

	private void buildAddBookFunction(){
		setOnClickFor(R.id.txt_fragment_book_list_add, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				activity.addFragment(new BookAddFragment());
			}
		});
	}

	private void buildSearchFunction(){
		setOnClickFor(R.id.img_fragment_book_list_search, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				onClickSearchIcon();
			}
		});

		Map<String, Object> searchCondition = (Map<String, Object>)getUpdatedData(BookSearchFragment.KEY_BOOK_SEARCH_CONDITION, new HashMap<String, Object>());
		if(searchCondition != null){
			setImageResourceFor(R.id.img_fragment_book_list_search, R.drawable.nav_btn_search_active);
			models = AbstractBookFragment.searchWithConditions(searchCondition);
			showBooks();
		}else {
			setImageResourceFor(R.id.img_fragment_book_list_search, R.drawable.nav_btn_search_inactive);
			reloadBook();
		}
	}

	private void onClickSearchIcon(){
		if(searchCondition != null){
			activity.addFragment(new BookSearchFragment(), BookSearchFragment.KEY_BOOK_SEARCH_CONDITION, searchCondition);
		}else{
			activity.addFragment(new BookSearchFragment());
		}
	}

	private void showBooks() {
		adapter.updateDataWith(books);
	}

	private void reloadBook() {
		books = new Select().from(Book.class).execute();
		adapter.updateDataWith(books);
	}

	public void gotoBookDetail(Book book){
		BookDetailFragment frg = new BookDetailFragment();
		frg.setBook(book);
		activity.addFragment(frg);
	}

	@Override
	public int getListViewId() {
		return R.id.lst_fragment_book_search_result_book;
	}

	@Override
	public void initAdapter() {
		adapter = new BookListAdapter(activity, R.layout.item_book);
	}
}
