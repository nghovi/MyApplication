package com.example.vietnguyen.views.widgets.notifications.adapters.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.adapters.MyArrayAdapter;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;

/**
 * Created by viet on 8/13/2015.
 */
public class BookListAdapter extends MyArrayAdapter<Book> {


	public BookListAdapter(Context context, int resourceId){
		super(context, resourceId, new ArrayList<Book>());
	}

	@Override
	protected void buildItemLayout(View convertView, Book book) {
		TextView txt1 = (TextView)convertView.findViewById(R.id.txt1);
		txt1.setText(book.name);
		TextView txt2 = (TextView)convertView.findViewById(R.id.txt2);
		txt2.setText(book.author);
		ImageView imgBookIcon = (ImageView)convertView.findViewById(R.id.img_icon);
		MU.picassaLoadImage(book.iconUrl, imgBookIcon, context);
	}
}
