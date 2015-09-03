package com.example.vietnguyen.controllers;

import android.app.Fragment;
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
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.BookAdapter;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends MyFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		ArrayList<Book> books = new ArrayList<Book>();
        books.add(new Book(0, "abcd1234", "Shacksepear", "Romeo and Juliet", "good book"));
        books.add(new Book(1, "abcd1234", "Shacksepear", "Romeo and Juliet", "good book"));
        books.add(new Book(2, "abcd1234", "Shacksepear", "Romeo and Juliet", "good book"));
		BookAdapter adapter = new BookAdapter(activity, R.layout.item_book, books);

        ListView lstBook = (ListView)getView(R.id.lst_book);
        lstBook.setAdapter(adapter);
        lstBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = (Book) adapterView.getItemAtPosition(i);
                gotoBookDetail(book);
            }
        });

        Gson gson = new Gson();
        MU.log(gson.toJson(books).toString());
	}

	@Override
	public void onApiResponse(String url, JSONObject response){

	}

    public void gotoBookDetail(Book book) {
        BookDetailFragment frg = new BookDetailFragment();
        frg.setBook(book);
        activity.addFragment(frg);
    }
}
