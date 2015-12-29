package com.nguyenhoangviet.vietnguyen.views.widgets.notifications.adapters.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenhoangviet.vietnguyen.controllers.Book.AbstractBookFragment;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.core.views.adapters.MyArrayAdapter;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

/**
 * Created by viet on 8/13/2015.
 */
public class BookListAdapter extends MyArrayAdapter<Book>{

	public BookListAdapter(Context context, int resourceId){
		super(context, resourceId, new ArrayList<Book>());
	}

	@Override
	protected void buildItemLayout(View convertView, Book book){
		TextView txt1 = (TextView)convertView.findViewById(R.id.txt1);
		txt1.setText(book.name);
		TextView txt2 = (TextView)convertView.findViewById(R.id.txt2);
		txt2.setText(book.author);
		ImageView imgBookIcon = (ImageView)convertView.findViewById(R.id.item_book_img_icon);
		if(!MU.isEmpty(book.iconUrl)){
			MU.loadImage(context, book.iconUrl, AbstractBookFragment.getBookImageFileName(book), imgBookIcon);
		}
	}
}
