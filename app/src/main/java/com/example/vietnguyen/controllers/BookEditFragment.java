package com.example.vietnguyen.controllers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class BookEditFragment extends MyFragment{

	private Book	book;
	private String originBookStr;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_edit, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		setOnClickFor(R.id.img_back, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onBackBtnClicked();
			}
		});

		setOnClickFor(R.id.img_icon_done, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				saveBookToServer();
			}
		});

		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_fbe_main_content);
		// JSONObject jsonObject = MU.buildJsonObjFromModel(book);
		// MU.interpolate(lnrContent, jsonObject);
		setTextFor(R.id.edt_fbe_name, book.name);
		setTextFor(R.id.edt_fbe_link, book.link);
		setTextFor(R.id.edt_fbe_author, book.author);
		setTextFor(R.id.edt_fbe_comment, book.comment);
		setFoldAction(getView(R.id.lnr_fbe_icon_url_selectable), getImageView(R.id.img_fbe_icon_url_fold_icon), R.id.edt_fbe_icon_url, null);
		setFoldAction(getView(R.id.lnr_fbe_comment_selectable), getImageView(R.id.img_fbe_fold), R.id.edt_fbe_comment, null);
		setFoldAction(getView(R.id.lnr_fbe_name_selectable), getImageView(R.id.img_fbe_name_fold_icon), R.id.edt_fbe_name, null);
		setFoldAction(getView(R.id.lnr_fbe_author_selectable), getImageView(R.id.img_fbe_author_fold_icon), R.id.edt_fbe_author, null);
		setFoldAction(getView(R.id.lnr_fbe_mood_selectable), getImageView(R.id.img_fbe_mood_fold_icon), R.id.edt_fbe_mood, null);
		setFoldAction(getView(R.id.lnr_fbe_link_selectable), getImageView(R.id.img_fbe_link_fold_icon), R.id.edt_fbe_link, null);
		setFoldAction(getView(R.id.lnr_fbe_name_selectable), getImageView(R.id.img_fbe_name_fold_icon), R.id.edt_fbe_name, null);

		addTextWatcherForBookImage();

		setTextFor(R.id.edt_fbe_icon_url, book.iconUrl);
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

	private void addTextWatcherForBookImage(){
		getEditText(R.id.edt_fbe_icon_url).addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void afterTextChanged(Editable editable){
				MU.picassaLoadImage(editable.toString(), getImageView(R.id.img_fbe_image), activity);
			}
		});
	}

	private void buildVocabulary(){
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_fbe_vocabulary_list);
		lnrVocabulary.removeAllViews();
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		for(final String word : this.book.getWords()){
			View itemBookWordEdit = inflater.inflate(R.layout.item_book_word_edit, null);

			setTextFor(itemBookWordEdit, R.id.txt_ibwe_word, word);
			setOnClickFor(itemBookWordEdit, R.id.lnr_ibwe_add_phrase, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					addPhrase(word);
				}
			});
			setOnClickFor(itemBookWordEdit, R.id.img_ibwe_delete, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					deleteWord(word);
				}
			});
			builPhrasesForWord(word, itemBookWordEdit, inflater);
			lnrVocabulary.addView(itemBookWordEdit);
		}
	}

	private void builPhrasesForWord(final String word, View itemBookWord, LayoutInflater inflater){
		List<String> phrases = this.book.getWordUsage(word);
		setFoldAction(getView(itemBookWord, R.id.lnr_ibwe), getImageView(itemBookWord, R.id.img_ibwe_fold), R.id.lnr_ibwe_foldable, getView(itemBookWord, R.id.img_ibwe_delete));
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

	// /////////////////////////////////////////////////////////////////////////////////////////////////

	private void onBackBtnClicked(){
		final Book newBook = buildBookFromLayout();
		if(this.originBookStr.equals(newBook.toString())){
			activity.backToFragment(BookDetailFragment.class, BookDetailFragment.KEY_UPDATED_BOOK, book);
		}else{
			dlgBuilder.build2OptsDlgTopDown("Discard", "Save changes", new View.OnClickListener() {

				@Override
				public void onClick(View view){
					activity.backOneFragment();
				}
			}, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					saveBookToServerAndBack(newBook);
				}
			}).show();
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
		book = buildBookFromLayout();
		originBookStr = book.toString();
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("book", book.toString()));
		postApi(Const.EDIT_BOOK, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response){
				buildLayout();
				showShortToast("Saved book");
			}

			@Override
			public void onApiError(String errorMsg){

			}
		});
	}

	public void saveBookToServerAndBack(final Book newBook){
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("book", newBook.toString()));
		postApi(Const.EDIT_BOOK, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response){
				book = newBook;
				originBookStr = book.toString();
				activity.backToFragment(BookDetailFragment.class, BookDetailFragment.KEY_UPDATED_BOOK, book);
			}

			@Override
			public void onApiError(String errorMsg){
				int a = 1;
			}
		});
	}

	private Book buildBookFromLayout(){
		Book b = new Book();
		b.setVocabulary(book.getVocabulary());
		b.id = book.id;

		b.name = getEditText(R.id.edt_fbe_name).getText().toString();
		b.comment = getEditText(R.id.edt_fbe_comment).getText().toString();
		b.author = getEditText(R.id.edt_fbe_author).getText().toString();
		b.mood = getEditText(R.id.edt_fbe_mood).getText().toString();
		b.iconUrl = getEditText(R.id.edt_fbe_icon_url).getText().toString();
		b.link = getEditText(R.id.edt_fbe_link).getText().toString();
		return b;
	}

	public void setBook(Book book){
		this.book = book;
		this.originBookStr = this.book.toString();
	}
}
