package com.example.vietnguyen.controllers;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

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

public class BookAddFragment extends MyFragment{

	private Book	book;
	private String	originBookStr;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		book = new Book();
		book.iconUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Sumerian_MS2272_2400BC.jpg/220px-Sumerian_MS2272_2400BC.jpg";
		book.link = "https://en.wikipedia.org/wiki/Book";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_add, container, false);
	}

	@Override
	protected void buildLayout() {
		super.buildLayout();
		setOnClickFor(R.id.img_fba_back, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				onBackBtnClicked();
			}
		});

		setOnClickFor(R.id.txt_fba_done, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				addBookToServer();
			}
		});

		setTextFor(R.id.edt_sbe_link, book.link);
		setTextFor(R.id.edt_sbe_icon_url, book.iconUrl);

		setFoldAction(getView(R.id.lnr_sbe_icon_url_selectable), getImageView(R.id.img_sbe_icon_url_fold_icon), R.id.edt_sbe_icon_url, null);
		setFoldAction(getView(R.id.lnr_sbe_comment_selectable), getImageView(R.id.img_sbe_fold), R.id.edt_sbe_comment, null);
		setFoldAction(getView(R.id.lnr_sbe_name_selectable), getImageView(R.id.img_sbe_name_fold_icon), R.id.edt_sbe_name, null);
		setFoldAction(getView(R.id.lnr_sbe_author_selectable), getImageView(R.id.img_sbe_author_fold_icon), R.id.edt_sbe_author, null);
		setFoldAction(getView(R.id.lnr_sbe_mood_selectable), getImageView(R.id.img_sbe_mood_fold_icon), R.id.edt_sbe_mood, null);
		setFoldAction(getView(R.id.lnr_sbe_link_selectable), getImageView(R.id.img_sbe_link_fold_icon), R.id.edt_sbe_link, null);
		setFoldAction(getView(R.id.lnr_sbe_name_selectable), getImageView(R.id.img_sbe_name_fold_icon), R.id.edt_sbe_name, null);

		addTextWatcherForBookImage();

		MU.picassaLoadImage(book.iconUrl, getImageView(R.id.img_sbe_image), activity);
		setOnClickFor(R.id.ico_sbe_add_vocabulary, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				addWord();
			}
		});

		buildVocabulary();
	}

	private void addTextWatcherForBookImage(){
		getEditText(R.id.edt_sbe_icon_url).addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void afterTextChanged(Editable editable){
				MU.picassaLoadImage(editable.toString(), getImageView(R.id.img_sbe_image), activity);
			}
		});
	}

	private void buildVocabulary(){
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_sbe_vocabulary_list);
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
		book = buildBookFromLayout();
		if(!book.hasSomeInfo()){
			activity.backToFragment(BookDetailFragment.class);
		}else{
			dlgBuilder.buildConfirmDlgTopDown("Continue", "Discard Changes", new View.OnClickListener() {

				@Override
				public void onClick(View view){
					activity.backOneFragment();
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
		buildVocabulary();
	}

	private void deleteWord(final String word){
		dlgBuilder.buildConfirmDlgTopDown("Cancel", "Delete", new View.OnClickListener() {

			@Override
			public void onClick(View view){
				book.deleteWord(word);
				buildVocabulary();
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
		buildVocabulary();
	}

	private void deletePhrase(String word, String phrase){
		book.deletePhraseForWord(word, phrase);
		buildVocabulary();
	}

	public void addBookToServer(){
		book = buildBookFromLayout();
		if(book.isReadyToSave()){
			JSONObject params = MU.buildJsonObj(Arrays.<String>asList("book", book.toString()));
			postApi(Const.ADD_BOOK, params, new Api.OnCallApiListener() {

				@Override
				public void onApiResponse(JSONObject response){
					showShortToast("Successfully saved new book");
					activity.backOneFragment();
				}

				@Override
				public void onApiError(String errorMsg){

				}
			});
		}else{
			showLongToast("Please fullfill information");
		}

	}

	private Book buildBookFromLayout(){
		Book b = new Book();
		b.setVocabulary(book.getVocabulary());
		b.id = book.id;

		b.name = getEditText(R.id.edt_sbe_name).getText().toString();
		b.comment = getEditText(R.id.edt_sbe_comment).getText().toString();
		b.author = getEditText(R.id.edt_sbe_author).getText().toString();
		b.mood = getEditText(R.id.edt_sbe_mood).getText().toString();
		b.iconUrl = getEditText(R.id.edt_sbe_icon_url).getText().toString();
		b.link = getEditText(R.id.edt_sbe_link).getText().toString();
		return b;
	}

	public void setBook(Book book){
		this.book = book;
		this.originBookStr = this.book.toString();
	}
}
