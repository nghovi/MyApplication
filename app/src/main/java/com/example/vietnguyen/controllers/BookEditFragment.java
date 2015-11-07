package com.example.vietnguyen.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class BookEditFragment extends MyFragment{

	private Book	book;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_edit, container, false);
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

		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_fbe_main_content);
//		JSONObject jsonObject = MU.buildJsonObjFromModel(book);
//		MU.interpolate(lnrContent, jsonObject);
		setTextFor(R.id.edt_fbe_name, book.name);
		setTextFor(R.id.edt_fbe_link, book.link);
		setTextFor(R.id.edt_fbe_comment, book.comment);
		setTextFor(R.id.edt_fbe_image_url, book.iconUrl);
		setTextFor(R.id.edt_fbe_mood, book.mood);

		MU.picassaLoadImage(book.iconUrl, getImageView(R.id.img_fbe_image), activity);
//		setFoldAction(R.id.img_fbe_fold, R.id.scr_fbe_content);
//		setLinkFor(R.id.txt_book_link, book.link);
		setOnClickFor(R.id.ico_fbe_add_vocabulary, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				addWord();
			}
		});

		buildVocabulary();
	}

	private void buildVocabulary(){
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_fbe_vocabulary_list);
		lnrVocabulary.removeAllViews();
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		for(final String word : this.book.getVocabularyList()){
			View itemBookWord = inflater.inflate(R.layout.item_book_word_edit, null);
			setTextFor(itemBookWord, R.id.txt_ibwe_word, word);
			setOnClickFor(itemBookWord, R.id.img_ibwe_phrase_add, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					addPhrase(word);
				}
			});
			setOnClickFor(itemBookWord, R.id.img_ibwe_delete, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					deleteWord();
				}
			});
			setOnClickFor(itemBookWord, R.id.img_ibwe_edit, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//todo edit word
				}
			});
			builPhrasesForWord(word, itemBookWord, inflater);
			lnrVocabulary.addView(itemBookWord);
		}
	}

	private void builPhrasesForWord(final String word, View itemBookWord, LayoutInflater inflater){
		List<String> phrases = this.book.getWordUsage(word);
		LinearLayout lnrPhrases = getLinearLayout(itemBookWord, R.id.lnr_ibwe_phrases);
		lnrPhrases.removeAllViews();
		if(phrases.size() > 0){
			visibleView(lnrPhrases);
		}
		for(String phrase : phrases){
			View line = inflater.inflate(R.layout.item_phrase_edit, null);
			setTextFor(line, R.id.txt_ipe_phrase, phrase);
			setOnClickFor(line, R.id.img_ipe_delete, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//todo delete phrase
				}
			});
			setOnClickFor(line, R.id.img_ipe_edit, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//todo edit phrase
				}
			});
			lnrPhrases.addView(line);
		}
	}

	private void addWord(){
		dlgBuilder.buildDialogWithEdt(activity, "Enter new word", new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input){
				addWordForBook(book, input);
			}
		}).show();
	}

	private void deleteWord(){
		dlgBuilder.buildConfirmDlgTopDown("Cancel", "Delete", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//todo ...
			}
		}).show();
	}

	private void addPhrase(final String word){
		dlgBuilder.buildDialogWithEdt(activity, "Enter new phrase for " + word, new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input){
				addPhraseForWord(book, word, input);
			}
		}).show();
	}

	private void addWordForBook(Book b, String newWord){
		b.addWordForBook(newWord);
		saveBookToServer(b);
	}

	private void addPhraseForWord(Book b, String word, String newPhrase){
		b.addPhraseForWord(word, newPhrase);
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
