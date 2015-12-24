package com.nguyenhoangviet.vietnguyen.controllers.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import com.activeandroid.query.Select;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragmentWithList;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.models.MyModel;
import com.nguyenhoangviet.vietnguyen.myapplication.R;
import com.nguyenhoangviet.vietnguyen.views.widgets.notifications.adapters.adapters.BookListAdapter;

import java.util.HashMap;
import java.util.Map;

public class BookListFragment extends MyFragmentWithList{

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

	@Override
	protected void onClickItem(MyModel model){
		gotoBookDetail((Book)model);
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
		searchConditions = (Map<String, Object>)getUpdatedData(BookSearchFragment.KEY_BOOK_SEARCH_CONDITION, new HashMap<String, Object>());
		if(searchConditions.size() > 0){
			setImageResourceFor(R.id.img_fragment_book_list_search, R.drawable.nav_btn_search_active);
			models = AbstractBookFragment.searchWithConditions(searchConditions);
			showBooks();
		}else{
			setImageResourceFor(R.id.img_fragment_book_list_search, R.drawable.nav_btn_search_inactive);
			reloadBook();
		}
		setOnClickFor(R.id.img_fragment_book_list_search, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickSearchIcon(searchConditions);
			}
		});

	}

	private void onClickSearchIcon(Map<String, Object> searchCondition){
		if(searchCondition != null){
			activity.addFragment(new BookSearchFragment(), BookSearchFragment.KEY_BOOK_SEARCH_CONDITION, searchCondition);
		}else{
			activity.addFragment(new BookSearchFragment());
		}
	}

	private void showBooks(){
		adapter.updateDataWith(models);
	}

	private void reloadBook(){
		models = new Select().from(Book.class).execute();
		adapter.updateDataWith(models);
	}

	public void gotoBookDetail(Book book){
		BookDetailFragment frg = new BookDetailFragment();
		activity.addFragment(frg, AbstractBookFragment.KEY_UPDATED_BOOK, book);
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
