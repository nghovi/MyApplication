package com.example.vietnguyen.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.DialogBuilder;
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
		setOnClickFor(R.id.ico_book_detail_add_vocabulary, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				addWord();
			}
		});

		buildVocabulary();
	}

	private void buildVocabulary(){
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_vocabulary_list);
		lnrVocabulary.removeAllViews();
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		for(String word : this.book.getVocabularyList()){
			View itemBookWord = inflater.inflate(R.layout.item_book_word, null);
			setTextFor(itemBookWord, R.id.txt_content, word);
			builPhrasesForWord(word, itemBookWord, inflater);
			lnrVocabulary.addView(itemBookWord);
		}
	}

	private void builPhrasesForWord(final String word, View itemBookWord, LayoutInflater inflater){
		List<String> phrases = this.book.getWordUsage(word);
		LinearLayout lnrPhrases = getLinearLayout(itemBookWord, R.id.item_book_word_lnr_phrases);
		lnrPhrases.removeAllViews();
		setOnClickFor(itemBookWord, R.id.ico_book_word_phrase_add, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				addPhrase(word);
			}
		});
		if(phrases.size() > 0){
			visibleView(lnrPhrases);
		}
		for(String phrase : phrases){
			View line = inflater.inflate(R.layout.item_word, null);
			setTextFor(line, R.id.txt_item_word_mark, Book.PHRASE_START_STR);
			setTextFor(line, R.id.txt_item_word_content, phrase);
			lnrPhrases.addView(line);
		}
	}

	private void addWord(){
		dlgBuilder.buildDialogWithEdt(activity, "Add word", "Enter new word", new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input){
				addWordForBook(book, input);
			}
		}).show();
	}

	private void addPhrase(final String word){
		dlgBuilder.buildDialogWithEdt(activity, "Add usage for" + word, "Enter new phrase", new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input){
				addPhraseForWord(book, word, input);
			}
		}).show();
	}

	private void addWordForBook(Book b, String newWord){
		if("".equals(newWord)){
			return;
		}
		if(b.vocabulary == null || b.vocabulary.equals("")){
			b.vocabulary = newWord;
		}else{
			boolean hasOtherWord = b.vocabulary.length() > 0;
			if(hasOtherWord){
				b.vocabulary = newWord + Book.VOCABULARY_DELIMITER + b.vocabulary;
			}else{
				b.vocabulary = newWord;
			}
		}
		saveBookToServer(b);
	}

	private void addPhraseForWord(Book b, String word, String newPhrase){
		if("".equals(word) || "".equals(newPhrase)){
			return;
		}
		if(b.vocabulary.indexOf(word) == 0){ // first word
			if(b.vocabulary.indexOf(word + Book.VOCABULARY_USAGE_DELIMITER_OPEN) == 0){// already has phrases
				int startIdx = b.vocabulary.indexOf(word + Book.VOCABULARY_USAGE_DELIMITER_OPEN) + word.length() + Book.VOCABULARY_USAGE_DELIMITER_OPEN.length();
				b.vocabulary = b.vocabulary.substring(0, startIdx) + newPhrase + Book.PHRASE_DELIMITER + b.vocabulary.substring(startIdx);
			}else{
				int startIdx = b.vocabulary.indexOf(word) + word.length();
				b.vocabulary = b.vocabulary.substring(0, startIdx) + Book.VOCABULARY_USAGE_DELIMITER_OPEN + newPhrase + Book.VOCABULARY_USAGE_DELIMITER_CLOSE + b.vocabulary.substring(startIdx);
			}
		}else{
			int startIdx = b.vocabulary.indexOf(Book.VOCABULARY_DELIMITER + word + Book.VOCABULARY_USAGE_DELIMITER_OPEN);
			if(startIdx > 0){ // already has phrase
				startIdx = startIdx + Book.VOCABULARY_DELIMITER.length() + word.length() + Book.VOCABULARY_USAGE_DELIMITER_OPEN.length();
				b.vocabulary = b.vocabulary.substring(0, startIdx) + newPhrase + Book.PHRASE_DELIMITER + b.vocabulary.substring(startIdx);
			}else{ // empty
				startIdx = b.vocabulary.indexOf(Book.VOCABULARY_DELIMITER + word) + Book.VOCABULARY_DELIMITER.length() + word.length();
				b.vocabulary = b.vocabulary.substring(0, startIdx) + Book.VOCABULARY_USAGE_DELIMITER_OPEN + newPhrase + Book.VOCABULARY_USAGE_DELIMITER_CLOSE + b.vocabulary.substring(startIdx);
			}
		}
		saveBookToServer(b);
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
