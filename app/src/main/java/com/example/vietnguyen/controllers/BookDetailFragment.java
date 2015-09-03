package com.example.vietnguyen.controllers;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.BookAdapter;
import com.google.gson.Gson;

public class BookDetailFragment extends MyFragment{
    private Book book;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_detail, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		Book newBook = this.book;
	}

	@Override
	public void onApiResponse(String url, JSONObject response){

	}

    public void setBook(Book book) {
        this.book = book;
    }
}
