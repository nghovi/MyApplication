package com.example.vietnguyen.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.BookAdapter;
import com.google.gson.Gson;

public class BookDetailFragment extends MyFragment{

	private Book	book;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_detail, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();

		ImageView imgBack = getImageView(R.id.img_back);
		imgBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				activity.backOneFragment();
			}
		});

		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_book_detail_main_content);
		JSONObject jsonObject = MU.buildJsonObjFromModel(book);
		MU.interpolate(lnrContent, jsonObject);
		setLinkFor(R.id.txt_book_link, book.link);

		buildVocabulary();
	}

	private void buildVocabulary(){
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_vocabulary_list);
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		for(String phrase : this.book.vocabulary){
			View itemPhrase = inflater.inflate(R.layout.item_text_divider, null);
			TextView txtPhrase = (TextView)itemPhrase.findViewById(R.id.txt_content);
			txtPhrase.setText(phrase);
			lnrVocabulary.addView(itemPhrase);
		}
	}

	@Override
	public void onApiResponse(String url, JSONObject response){

	}

	public void setBook(Book book){
		this.book = book;
	}
}
