package com.nguyenhoangviet.vietnguyen.controllers.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.controllers.Book.BookSearchFragment;
import com.nguyenhoangviet.vietnguyen.controllers.Task.AbstractTaskFragment;
import com.nguyenhoangviet.vietnguyen.controllers.Task.TaskAddFragment;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragmentWithList;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.network.UrlBuilder;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.models.MyModel;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.myapplication.R;
import com.nguyenhoangviet.vietnguyen.views.widgets.notifications.adapters.adapters.BookListAdapter;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookListFragment extends MyFragmentWithList{

	protected List<Book>		models;

	private boolean				isSearching	= false;

	private Map<String, Object>	searchConditions;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_list, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildAddBookFunction();
		buildSearchFunction();
	}

	private void callApiGetBooks(){
		callApi(UrlBuilder.bookList(null), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onGetBooksSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});

	}

	private void onGetBooksSuccess(JSONObject response){
		models = MU.convertToModelList(response.optString("results"), Book.class);
		showBooks(models);
	}

	@Override
	protected void onClickItem(MyModel model){
		gotoBookDetail((Book)model);
	}

	private void buildAddBookFunction(){
		final TextView txtAdd = getTextView(R.id.txt_fragment_book_list_add);
		txtAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(txtAdd.getText().equals(getString(R.string.cancel))){
					isSearching = false;
					updateHeaderLayoutForSearching();
					callApiGetBooks();
				}else{
					activity.addFragment(new BookAddFragment());
				}
			}
		});
	}

	private void buildSearchFunction(){
		searchConditions = (Map<String, Object>)getUpdatedData(BookSearchFragment.KEY_BOOK_SEARCH_CONDITION, new HashMap<String, Object>());
		if(searchConditions.size() > 0){
			isSearching = true;
			callApiSearchBook();
		}else{
			isSearching = false;
			callApiGetBooks();
		}
		updateHeaderLayoutForSearching();
		setOnClickFor(R.id.img_fragment_book_list_search, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickSearchIcon(searchConditions);
			}
		});
	}

	private void callApiSearchBook(){
		JSONObject filterValues = getJsonBuilder().add("name", searchConditions.get(BookSearchFragment.KEY_BOOK_SEARCH_NAME)).add("author", searchConditions.get(BookSearchFragment.KEY_BOOK_SEARCH_AUTHOR)).add("comment", searchConditions.get(BookSearchFragment.KEY_BOOK_SEARCH_COMMENT)).getJsonObj();
		callApi(UrlBuilder.bookList(filterValues), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSearchBookSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});
	}

	private void onSearchBookSuccess(JSONObject response){
		models = MU.convertToModelList(response.optString("data"), Book.class);
		models = AbstractBookFragment.searchWithConditions(searchConditions, models);
		showBooks(models);
	}

	private void updateHeaderLayoutForSearching(){
		if(isSearching){
			setTextFor(R.id.txt_fragment_book_list_add, getString(R.string.cancel));
			setImageResourceFor(R.id.img_fragment_book_list_search, R.drawable.nav_btn_search_active);
		}else{
			setImageResourceFor(R.id.img_fragment_book_list_search, R.drawable.nav_btn_search_inactive);
			setTextFor(R.id.txt_fragment_book_list_add, getString(R.string.add));
		}
	}

	private void onClickSearchIcon(Map<String, Object> searchCondition){
		if(searchCondition != null){
			activity.addFragment(new BookSearchFragment(), BookSearchFragment.KEY_BOOK_SEARCH_CONDITION, searchCondition);
		}else{
			activity.addFragment(new BookSearchFragment());
		}
	}

	private void showBooks(List<Book> books){
		sortByName();
		adapter.updateDataWith(books);
	}

	private void sortByName(){
		// http://stackoverflow.com/questions/933447/how-do-you-cast-a-list-of-supertypes-to-a-list-of-subtypes
		Collections.sort((List<Book>)(List<?>)models, new Comparator<Book>() {

			@Override
			public int compare(Book b1, Book b2){
				return b1.name.compareTo(b2.name) > 0 ? 1 : -1;
			}
		});
	}

	public void gotoBookDetail(Book book){
		BookDetailFragment frg = new BookDetailFragment();
		activity.addFragment(frg, BookDetailFragment.BOOK_ID, book.id);
	}

	@Override
	public int getListViewId(){
		return R.id.lst_fragment_book_search_result_book;
	}

	@Override
	public void initAdapter(){
		adapter = new BookListAdapter(activity, R.layout.item_book);
	}

	@Override
	public void onEmptyData(){
		super.onEmptyData();
		visibleView(R.id.txt_fragment_book_list_empty);
	}

	@Override
	public void onNotEmptyData(){
		super.onNotEmptyData();
		goneView(R.id.txt_fragment_book_list_empty);
	}
}
