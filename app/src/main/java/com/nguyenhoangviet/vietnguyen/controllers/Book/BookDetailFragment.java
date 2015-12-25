package com.nguyenhoangviet.vietnguyen.controllers.Book;

import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nguyenhoangviet.vietnguyen.core.controller.MyFragment;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

public class BookDetailFragment extends MyFragment{

	private Book			book;
	private TextToSpeech	tts;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_detail, container, false);
	}

	@Override
	protected void buildLayout(){
		buildTts();
		book = (Book)getUpdatedData(AbstractBookFragment.KEY_UPDATED_BOOK, book);
		setOnClickFor(R.id.img_back, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				activity.backOneFragment();
			}
		});
		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_book_detail_main_content);
		JSONObject jsonObject = MU.buildJsonObjFromModel(book);
		MU.interpolate(lnrContent, jsonObject);
		if(!MU.isEmpty(book.iconUrl)){
			MU.picassaLoadImage(book.iconUrl, getImageView(R.id.img_book_detail_image), activity);
		}else{
			getImageView(R.id.img_book_detail_image).setImageResource(R.drawable.book_cover);
		}
		setFoldAction(getView(R.id.lnr_fbd_comment), getImageView(R.id.img_fbd_fold), R.id.lnr_fbd_comment_content, null);
		setLinkFor(R.id.txt_book_link, book.link);
		setOnClickFor(R.id.img_fragment_book_detail_delete, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onDeleteIconClicked();
			}
		});
		setOnClickFor(R.id.img_book_detail_edit, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				gotoBookEditFragment();
			}
		});
		buildWords();
	}

	private void buildTts(){
		if(tts == null){
			tts = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {

				@Override
				public void onInit(int status){
					if(status != TextToSpeech.ERROR){
						tts.setLanguage(Locale.US);
					}
				}
			});
		}
	}

	private void onDeleteIconClicked(){

		dlgBuilder.build2OptsDlgTopDown(getString(R.string.cancel), getString(R.string.delete), null, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				// sendDeleteTask();
				book.delete();
				onClickBackBtn();
			}
		}).show();
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
		for(final String word : this.book.getWords()){
			View itemBookWord = inflater.inflate(R.layout.item_book_word, null);
			setTextFor(itemBookWord, R.id.txt_item_book_word_edit_word, word);
			setOnClickFor(itemBookWord, R.id.img_item_book_word_speaker, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					speakWord(word);
				}
			});
			builPhrasesForWord(word, itemBookWord, inflater);
			lnrVocabulary.addView(itemBookWord);
		}
	}

	private void speakWord(String word){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, "");
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

	@Override
	public void onDestroy(){
		super.onDestroy();
		tts = null;
	}
}
