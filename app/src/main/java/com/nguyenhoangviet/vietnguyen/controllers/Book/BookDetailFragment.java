package com.nguyenhoangviet.vietnguyen.controllers.Book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
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
		buildTts();
	}

	public class MyUtteranceProgressListener extends UtteranceProgressListener{

		List<String>			words;
		Iterator<String>		iterator;
		private long			intervalMS;
		private TextToSpeech	tts;
		private final String	SPEAK_ALL	= "SPEAK_ALL";

		public MyUtteranceProgressListener(TextToSpeech tts, List<String> words, long intervalMS){
			this.words = words;
			this.tts = tts;
			this.intervalMS = intervalMS;
		}

		@Override
		public void onStart(String s){

		}

		public void startSpeak(){
			iterator = this.words.iterator();
			if(iterator.hasNext()){
				speakWord(iterator.next());
			}
		}

		@Override
		public void onDone(String s){
			if(SPEAK_ALL.equals(s) && iterator.hasNext()){
				final String word = iterator.next();
				Timer timer = new Timer();

				timer.schedule(new TimerTask() {

					public void run(){
						speakWord(word);
					}
				}, intervalMS);
			}
		}

		private void speakWord(String word){
			Bundle bundle = new Bundle();
			bundle.putString(TextToSpeech.Engine.KEY_PARAM_PAN, "0");
			bundle.putString(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
			bundle.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, SPEAK_ALL);
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
				tts.speak(word, TextToSpeech.QUEUE_FLUSH, bundle, SPEAK_ALL);
			}
		}

		@Override
		public void onError(String s){

		}
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
					speakWord(word);
				}
			});
			builPhrasesForWord(word, itemBookWord, inflater);
			lnrVocabulary.addView(itemBookWord);
		}
	}

	private void speakWord(String word){
		if(!MU.isEmpty(word) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, "");
		}
	}

	private void speakAllWords(){
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
