package com.nguyenhoangviet.vietnguyen.controllers.Book;

import java.util.ArrayList;
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

	public static final String			WORD_SPEED_INTERVAL				= "WORD_SPEED_INTERVAL";
	public static final int				DEFAULT_WORD_SPEED_INTERVAL_MS	= 0;						// fastest
	public static final int				SLOWEST_WORD_SPEED_INTERVAL_MS	= 5000;					// slowest

	private Book						book;
	private TextToSpeech				tts;
	private MyUtteranceProgressListener	myUtteranceProgressListener;

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
			MU.loadImage(activity, book.iconUrl, AbstractBookFragment.getBookImageFileName(book), getImageView(R.id.img_book_detail_image));
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
		buildTts();
	}

	private void buildTts(){
		if(tts == null){
			tts = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {

				@Override
				public void onInit(int status){
					if(status != TextToSpeech.ERROR){
						tts.setLanguage(Locale.US);
						if(myUtteranceProgressListener == null){
							myUtteranceProgressListener = new MyUtteranceProgressListener(tts, getWordsForReading(), (long)activity.getIntPreference(WORD_SPEED_INTERVAL, DEFAULT_WORD_SPEED_INTERVAL_MS));
						}
						tts.setOnUtteranceProgressListener(myUtteranceProgressListener);
					}
				}
			});
		}
	}

	private List<String> getWordsForReading(){
		List<String> words = new ArrayList<String>();
		for(String word : book.getWords()){
			words.add(word);
		}
		return words;
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
		if(book.getWords().size() > 0){
			setOnClickFor(R.id.img_fragment_book_detail_speaker, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					speakAllWords();
				}
			});
		}else{
			goneView(R.id.img_fragment_book_detail_speaker);
		}

		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_book_detail_vocabulary_list);
		lnrVocabulary.removeAllViews();
		for(final String word : this.book.getWords()){
			View itemBookWord = inflater.inflate(R.layout.item_book_word, null);
			setTextFor(itemBookWord, R.id.txt_item_book_word_edit_word, word);
			setOnClickFor(itemBookWord, R.id.img_item_book_word_speaker, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					speakWordOnly(word);
				}
			});
			builPhrasesForWord(word, itemBookWord, inflater);
			lnrVocabulary.addView(itemBookWord);
		}
	}

	private void speakWordOnly(String word){
		if(!MU.isEmpty(word) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, "");
		}
	}

	// The accent like robot now, so don't like to use this function
	private void speakWordAndPhrase(String word){
		List<String> words = new ArrayList<String>();
		words.add(word);
		List<String> phrases = this.book.getPhrasesOfWord(word);
		words.addAll(phrases);
		myUtteranceProgressListener.setWords(words);
		myUtteranceProgressListener.startSpeak();
	}

	private void speakAllWords(){
		List<String> words = getWordsForReading();
		myUtteranceProgressListener.setWords(words);
		myUtteranceProgressListener.startSpeak();
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
		if(tts != null){
			tts.stop();
			tts.shutdown();
			tts = null;
		}
	}
}
