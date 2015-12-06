package com.example.vietnguyen.controllers.Book;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;

import java.util.List;

public class BookEditFragment extends AbstractBookFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_edit, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();

		setOnClickFor(R.id.img_icon_done, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				book.save();
//				saveThisBookAndStay();
			}
		});
		setOnClickFor(R.id.img_icon_delete, new View.OnClickListener() {

			@Override
			public void onClick(View view){
//				deleteThisBook();
			}
		});

		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_sbe_main_content);
		// JSONObject jsonObject = MU.buildJsonObjFromModel(book);
		// MU.interpolate(lnrContent, jsonObject);
		setTextFor(R.id.edt_sbe_name, book.name);
		setTextFor(R.id.edt_sbe_link, book.link);
		setTextFor(R.id.edt_sbe_author, book.author);
		setTextFor(R.id.edt_sbe_comment, book.comment);
		setTextFor(R.id.edt_sbe_icon_url, book.iconUrl);
		setTextFor(R.id.edt_sbe_mood, book.mood);

		addTextWatcherForBookImage();

		MU.picassaLoadImage(book.iconUrl, getImageView(R.id.img_sbe_image), activity);

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
				String url = editable.toString();
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
					showDialogForAddingPhrase(word);
				}
			});
			setOnClickFor(itemBookWordEdit, R.id.img_ibwe_delete, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					showDialogConfirmDeleteWord(word);
				}
			});
			builPhrasesForWord(word, itemBookWordEdit, inflater);
			lnrVocabulary.addView(itemBookWordEdit);
		}
	}

	private void builPhrasesForWord(final String word, View itemBookWord, LayoutInflater inflater){
		List<String> phrases = this.book.getWordUsage(word);
		setFoldAction(getView(itemBookWord, R.id.lnr_ibwe), getImageView(itemBookWord, R.id.img_ibwe_fold), R.id.lnr_ibwe_foldable, getView(itemBookWord, R.id.img_ibwe_delete));
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

	@Override
	protected void onClickBackBtn(){
		buildBookFromLayout();
		if(this.originBookStr.equals(book.toString())){
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
					activity.backToFragment(BookDetailFragment.class, BookDetailFragment.KEY_UPDATED_BOOK, book);
					saveThisBookToServerAndBack();
				}
			}).show();
		}
	}
}
