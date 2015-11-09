package com.example.vietnguyen.controllers;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;

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
		MU.picassaLoadImage(book.iconUrl, getImageView(R.id.img_book_detail_image), activity);
		setFoldAction(R.id.img_book_detail_fold, R.id.scr_book_detail_content);
		setLinkFor(R.id.txt_book_link, book.link);
		setOnClickFor(R.id.img_book_detail_edit, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				gotoBookEditFragment();
			}
		});
		buildWords();
	}

	private void gotoBookEditFragment(){
		BookEditFragment frg = new BookEditFragment();
		frg.setBook(book);
		activity.addFragment(frg);
	}

	private void buildWords(){
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_book_detail_vocabulary_list);
		lnrVocabulary.removeAllViews();
		for(String word : this.book.getWords()){
			View itemBookWord = inflater.inflate(R.layout.item_book_word, null);
			setTextFor(itemBookWord, R.id.txt_item_book_word_edit_word, word);
			builPhrasesForWord(word, itemBookWord, inflater);
			lnrVocabulary.addView(itemBookWord);
		}
	}

	private void builPhrasesForWord(final String word, View itemBookWord, LayoutInflater inflater){
		List<String> phrases = this.book.getPhrasesOfWord(word);
		LinearLayout lnrPhrases = getLinearLayout(itemBookWord, R.id.lnr_item_book_word_phrases);
		lnrPhrases.removeAllViews();
		if(phrases.size() > 0){
			setFoldAction(itemBookWord, R.id.img_item_book_word_fold, R.id.lnr_item_book_word_phrases);
		}
		for(String phrase : phrases){
			View line = inflater.inflate(R.layout.item_phrase, null);
			setTextFor(line, R.id.txt_item_word_mark, Book.PHRASE_START_STR);
			setTextFor(line, R.id.txt_item_word_content, phrase);
			lnrPhrases.addView(line);
		}
	}

	public void saveBookToServer(Book b){
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("book", b.toString()));
		postApi(Const.EDIT_BOOK, params);
	}

	@Override
	public void onApiResponse(String url, JSONObject response){
		buildLayout();
	}

	@Override
	public void onApiError(String url, String errorMsg){
		buildLayout();
	}

	public void setBook(Book book){
		this.book = book;
	}
}
