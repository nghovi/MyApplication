package com.nguyenhoangviet.vpcorp.controllers.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.nguyenhoangviet.vpcorp.core.controller.MyFragmentWithHeaderFooter;
import com.nguyenhoangviet.vpcorp.core.utils.MU;
import com.nguyenhoangviet.vpcorp.models.Book;
import com.nguyenhoangviet.vpcorp.vnote2.R;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class BookDetailFragment extends MyFragmentWithHeaderFooter{

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
	protected boolean hasBackButton(){
		return true;
	}

	@Override
	public void onBackImgClicked(){
		onClickBackBtn();
	}

	@Override
	protected String getHeaderTitle(){
		return getString(R.string.fragment_abstract_book_book_detail);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		book = (Book)getUpdatedData(AbstractBookFragment.KEY_UPDATED_BOOK, book);
		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_book_detail_main_content);
		JSONObject jsonObject = MU.buildJsonObjFromModel(book);
		MU.interpolate(lnrContent, jsonObject);
		buildCover();
		setFoldAction(getView(R.id.lnr_fbd_comment), getImageView(R.id.img_fbd_fold), R.id.lnr_fbd_comment_content, null, null);
		setLinkFor(R.id.txt_book_link, book.link);
		setOnClickFor(R.id.img_fragment_book_detail_delete, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onDeleteIconClicked();
			}
		});

		buildWords();
		buildTts();
		loadAds();
	}

	@Override
	public void onRightImgClicked(){
		gotoBookEditFragment();
	}

	private void buildCover(){
		MU.loadImage(activity, book.iconUrl, AbstractBookFragment.getBookImageFileName(book), getImageView(R.id.img_book_detail_image));
	}

	private void buildTts(){
		stopTts();
		tts = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {

			@Override
			public void onInit(int status){
				if(status != TextToSpeech.ERROR){
					tts.setLanguage(Locale.US);
					myUtteranceProgressListener = new MyUtteranceProgressListener(tts, getWordsForReading(), (long)activity.getIntPreference(WORD_SPEED_INTERVAL, DEFAULT_WORD_SPEED_INTERVAL_MS));
					tts.setOnUtteranceProgressListener(myUtteranceProgressListener);
				}
			}
		});
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
					setImageResourceFor(R.id.img_fragment_book_detail_speaker, R.drawable.ic_volume_up_black_18dp);
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
			final View itemBookWord = inflater.inflate(R.layout.item_book_word, null);
			itemBookWord.setTag(word);
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
		List<String> words = new ArrayList<String>();
		words.add(word);
		myUtteranceProgressListener.setWords(words);
		setOnSpeakWordStartListener();
		myUtteranceProgressListener.setOnSpeakWordDoneListener(new MyUtteranceProgressListener.OnSpeakWordDoneListener() {

			@Override
			public void onSpeakWordDone(String word){
				LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_book_detail_vocabulary_list);
				View itemBookWord = lnrVocabulary.findViewWithTag(word);
				setImageResourceFor(itemBookWord, R.id.img_item_book_word_speaker, R.drawable.ic_volume_mute_black_18dp);
			}
		});
		myUtteranceProgressListener.startSpeak();
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
		myUtteranceProgressListener.setOnSpeakAllDoneListener(new MyUtteranceProgressListener.OnSpeakAllDoneListener() {

			@Override
			public void onSpeakAllDone(){
				setImageResourceFor(R.id.img_fragment_book_detail_speaker, R.drawable.ic_volume_mute_black_18dp);
			}
		});
		setOnSpeakWordStartListener();
		myUtteranceProgressListener.setOnSpeakWordDoneListener(new MyUtteranceProgressListener.OnSpeakWordDoneListener() {

			@Override
			public void onSpeakWordDone(String word){
				LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_book_detail_vocabulary_list);
				View itemBookWord = lnrVocabulary.findViewWithTag(word);
				setImageResourceFor(itemBookWord, R.id.img_item_book_word_speaker, R.drawable.ic_volume_mute_black_18dp);
				// getView(itemBookWord, R.id.lnr_ibw).performClick();
			}
		});

		myUtteranceProgressListener.startSpeak();
	}

	private void setOnSpeakWordStartListener(){
		myUtteranceProgressListener.setOnSpeakWordStartListener(new MyUtteranceProgressListener.OnSpeakWordStartListener() {

			@Override
			public void onSpeakWordStart(String word){
				if(getView() != null){
					LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_book_detail_vocabulary_list);
					View itemBookWord = lnrVocabulary.findViewWithTag(word);
					setImageResourceFor(itemBookWord, R.id.img_item_book_word_speaker, R.drawable.ic_volume_up_black_18dp);
				}
			}
		});
	}

	private void builPhrasesForWord(final String word, View itemBookWord, LayoutInflater inflater){
		List<String> phrases = this.book.getPhrasesOfWord(word);
		LinearLayout lnrPhrases = getLinearLayout(itemBookWord, R.id.lnr_ibw_phrases);
		lnrPhrases.removeAllViews();
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v){
				speakWordOnly(word);
			}
		};
		if(phrases.size() > 0){
			setFoldAction(getView(itemBookWord, R.id.lnr_ibw), getImageView(itemBookWord, R.id.img_ibw_fold), R.id.lnr_ibw_phrases, null, listener);
		}else{
			setOnClickFor(itemBookWord, R.id.lnr_ibw, listener);
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
	public void onDestroyView(){
		stopTts();
		super.onDestroyView();
	}

	private void stopTts(){
		if(tts != null){
			tts.stop();
			tts.shutdown();
			tts = null;
		}
	}

	@Override
	public void onDestroy(){

		super.onDestroy();
	}
}
