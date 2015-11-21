package com.example.vietnguyen.controllers;

import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;

public class DetailBookFragment extends AbstractBookFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_detail, container, false);
	}

	@Override
	protected void buildLayout(){
		book = (Book)getUpdatedData(KEY_UPDATED_BOOK, book);

		setOnClickFor(R.id.img_back, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				activity.backOneFragment();
			}
		});

		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_book_detail_main_content);
		JSONObject jsonObject = MU.buildJsonObjFromModel(book);
		MU.interpolate(lnrContent, jsonObject);
		MU.picassaLoadImage(book.iconUrl, getImageView(R.id.img_book_detail_image), activity);
		setFoldAction(getView(R.id.lnr_fbd_comment), getImageView(R.id.img_fbd_fold), R.id.scr_fbd_comment_content, null);
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
		EditBookFragment frg = new EditBookFragment();
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
		LinearLayout lnrPhrases = getLinearLayout(itemBookWord, R.id.lnr_ibw_phrases);
		lnrPhrases.removeAllViews();
		if(phrases.size() > 0){
			setFoldAction(getView(itemBookWord, R.id.lnr_ibw), getImageView(itemBookWord, R.id.img_ibw_fold), R.id.lnr_ibw_phrases, null);
		}else{
			invisibleView(itemBookWord, R.id.img_ibw_fold);
		}
		for(String phrase : phrases){
			View line = inflater.inflate(R.layout.item_phrase, null);
			setTextFor(line, R.id.txt_item_word_mark, Book.PHRASE_START_STR);
			setTextFor(line, R.id.txt_item_word_content, phrase);
			lnrPhrases.addView(line);
		}
	}
}
