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
		// JSONObject jsonObject = MU.buildJsonObjFromModel(book);
		// MU.interpolate(lnrContent, jsonObject);
		setTextFor(R.id.edt_fbe_name, book.name);
		setTextFor(R.id.edt_fbe_link, book.link);
		setTextFor(R.id.edt_fbe_comment, book.comment);
		setTextFor(R.id.edt_fbe_image_url, book.iconUrl);
		setTextFor(R.id.edt_fbe_mood, book.mood);

		MU.picassaLoadImage(book.iconUrl, getImageView(R.id.img_fbe_image), activity);
		// setFoldAction(R.id.img_fbe_fold, R.id.scr_fbe_content);
		// setLinkFor(R.id.txt_book_link, book.link);
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
		for(final String word : this.book.getWords()){
			View itemBookWord = inflater.inflate(R.layout.item_book_word_edit, null);
			setFoldAction(itemBookWord, R.id.img_ibwe_fold, R.id.lnr_ibwe_foldable);
			setTextFor(itemBookWord, R.id.txt_ibwe_word, word);
			setOnClickFor(itemBookWord, R.id.lnr_ibwe_add_phrase, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					addPhrase(word);
				}
			});
			setOnClickFor(itemBookWord, R.id.img_ibwe_delete, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					deleteWord(word);
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
		for(final String phrase : phrases){
			View line = inflater.inflate(R.layout.item_phrase_edit, null);
			setTextFor(line, R.id.txt_ipe_phrase, phrase);
			setOnClickFor(line, R.id.img_ipe_delete, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					dlgBuilder.buildConfirmDlgTopDown("Cancel", "Delete", new View.OnClickListener() {

						@Override
						public void onClick(View view){
							deletePhrase(word, phrase);
						}
					}).show();
				}
			});
			lnrPhrases.addView(line);
		}
	}

	private void addWord(){
		dlgBuilder.buildDialogWithEdt(activity, "Enter new word", new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input){
				addWordForBook(input);
			}
		}).show();
	}

	private void addWordForBook(String newWord){
		book.addWordForBook(newWord);
		saveBookToServer();
	}

	private void deleteWord(final String word){
		dlgBuilder.buildConfirmDlgTopDown("Cancel", "Delete", new View.OnClickListener() {

			@Override
			public void onClick(View view){
				book.deleteWord(word);
				saveBookToServer();
			}
		}).show();
	}

	private void addPhrase(final String word){
		dlgBuilder.buildDialogWithEdt(activity, "Enter new phrase for " + word, new DialogBuilder.OnDialogWithEdtDismiss() {

			@Override
			public void onClickDone(String input){
				addPhraseForWord(word, input);
			}
		}).show();
	}

	private void addPhraseForWord(String word, String phrase){
		book.addPhraseForWord(word, phrase);
		saveBookToServer();
	}

	private void deletePhrase(String word, String phrase){
		book.deletePhraseForWord(word, phrase);
		saveBookToServer();
	}

	public void saveBookToServer(){
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("book", book.toString()));
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
