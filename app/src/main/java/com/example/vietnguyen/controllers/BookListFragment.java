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
import java.util.Arrays;
import java.util.List;

public class BookListFragment extends MyFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		ArrayList<Book> books = new ArrayList<Book>();
        books.add(new Book(0, "abcd1", "Shacksepear", "Romeo and J1uliet", "The Titan's Curse", Arrays.asList("incre2dible", "what is2 it", "why23 not me"), "mood", "link"));
        books.add(new Book(1, "abcd2", "Shacksepear", "Romeo and Jul2iet", "The Lightning Thief", Arrays.asList("incredi12ble", "what 23is it", "why 2not me"), "mood2", "link2"));
        books.add(new Book(2, "abcd3", "Shacksepear", "Romeo and J3uliet", "The Sea Of Monster", Arrays.asList("incredi2ble", "what is 23it", "why n7ot me"), "mood3", "link3"));
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
