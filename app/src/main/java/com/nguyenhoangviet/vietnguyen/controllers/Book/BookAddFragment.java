package com.nguyenhoangviet.vietnguyen.controllers.Book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.models.Link;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

public class BookAddFragment extends AbstractBookFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		book = new Book();
		book.iconurl = getString(R.string.fragment_abstract_book_default_link);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_add, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		getMainActivity().footer.hide();
		invisibleView(R.id.lnr_sbe_part2);
	}

	@Override
	protected void buildBookInfo(){
		setOnClickFor(R.id.txt_fba_done, this);
		// setTextFor(R.id.edt_sbe_link, book.link);
		setTextFor(R.id.edt_sbe_icon_url, book.iconurl);
	}

	@Override
	protected void buildVocabulary(){

	}

	@Override
	protected void onClickBackBtn(){
		if(!hasChangeData()){
			activity.backToFragment(BookListFragment.class);
		}else{
			dlgBuilder.buildConfirmDlgTopDown(getString(R.string.continue_str), getString(R.string.discard), new View.OnClickListener() {

				@Override
				public void onClick(View view){
					activity.backOneFragment();
				}
			}).show();
		}
	}

	@Override
	protected boolean hasChangeData(){
		book.name = getEditText(R.id.edt_sbe_name).getText().toString();
		book.comment = getEditText(R.id.edt_sbe_comment).getText().toString();
		book.author = getEditText(R.id.edt_sbe_author).getText().toString();
		book.iconurl = getEditText(R.id.edt_sbe_icon_url).getText().toString();
		book.link = new Link();
		book.link.url = getEditText(R.id.edt_sbe_link).getText().toString();
		return !(MU.isEmpty(book.name) && MU.isEmpty(book.author) && MU.isEmpty(book.comment) && MU.isEmpty(book.iconurl) && MU.isEmpty(book.link.url));
	}
}
