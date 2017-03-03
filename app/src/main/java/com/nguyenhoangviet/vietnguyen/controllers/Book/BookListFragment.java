package com.nguyenhoangviet.vietnguyen.controllers.Book;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.activeandroid.query.Select;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragmentWithList;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.models.MyModel;
import com.nguyenhoangviet.vietnguyen.myapplication.R;
import com.nguyenhoangviet.vietnguyen.views.widgets.notifications.adapters
		.adapters.BookListAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BookListFragment extends MyFragmentWithList{

	private Map<String, Object>	searchConditions;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_list, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildSearchFunction();
		loadAds();
	}

	@Override
	protected int getRightImageId(){
		return R.drawable.nav_btn_search_inactive;
	}

	@Override
	protected String getHeaderTitle(){
		return getString(R.string.fragment_book_list_title);
	}

	@Override
	public void onRightImgClicked(){
		onClickSearchIcon(searchConditions);
	}

	@Override
	public void onLeftImgClicked(){
		activity.addFragment(new BookAddFragment());
	}

	@Override
	public int getLeftImageId(){
		return R.drawable.cl_action_add;
	}

	@Override
	protected void onClickItem(MyModel model){
		gotoBookDetail((Book)model);
	}

	private void buildSearchFunction(){
		searchConditions = (Map<String, Object>)getUpdatedData(BookSearchFragment.KEY_BOOK_SEARCH_CONDITION, new HashMap<String, Object>());
		if(searchConditions.size() > 0){
			setImageResourceFor(R.id.img_vnote_header_right, R.drawable.nav_btn_search_active);
			models = AbstractBookFragment.searchWithConditions(searchConditions);
			showBooks();
		}else{
			setImageResourceFor(R.id.img_vnote_header_right, R.drawable.nav_btn_search_inactive);
			reloadBook();
		}
	}

	private void onClickSearchIcon(Map<String, Object> searchCondition){
		if(searchCondition != null){
			activity.addFragment(new BookSearchFragment(), BookSearchFragment.KEY_BOOK_SEARCH_CONDITION, searchCondition);
		}else{
			activity.addFragment(new BookSearchFragment());
		}
	}

	private void showBooks(){
		sortByName();
		adapter.updateDataWith(models);
	}

	private void reloadBook(){
		models = new Select().from(Book.class).execute();
		sortByName();
		adapter.updateDataWith(models);
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
