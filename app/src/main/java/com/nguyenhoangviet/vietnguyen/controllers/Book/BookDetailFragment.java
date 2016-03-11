package com.nguyenhoangviet.vietnguyen.controllers.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragment;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.network.UrlBuilder;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.models.Phrase;
import com.nguyenhoangviet.vietnguyen.models.Word;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

public class BookDetailFragment extends MyFragment{

	public static final String			WORD_SPEED_INTERVAL				= "WORD_SPEED_INTERVAL";
	public static final int				DEFAULT_WORD_SPEED_INTERVAL_MS	= 0;						// fastest
	public static final int				SLOWEST_WORD_SPEED_INTERVAL_MS	= 5000;					// slowest
	public static final String			BOOK_ID							= "BOOK_ID";				// slowest

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
		setOnClickFor(R.id.img_back, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				activity.backOneFragment();
			}
		});
		callApiGetBookDetail();
	}

	private void callApiGetBookDetail(){
		String bookId = (String)getUpdatedData(BOOK_ID, "");
		callApi(UrlBuilder.bookDetail(bookId), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onGetBookDetailSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});
	}

	private void onGetBookDetailSuccess(JSONObject response){
		book = MU.convertToModel(response.toString(), Book.class);
		buildLayoutAfterGetBook();
	}

	private void buildLayoutAfterGetBook(){
		buildCover();
		setTextFor(R.id.txt_fbd_author, book.author);
		setTextFor(R.id.txt_fbd_name, book.name);
		if(book.link != null){
			setTextFor(R.id.txt_fbd_link, book.link.url);
			setLinkFor(R.id.txt_fbd_link, book.link.url);
		}else{
			goneView(R.id.txt_fbd_link);
		}
		setTextFor(R.id.txt_fbd_comment, book.comment);
		setFoldAction(getView(R.id.lnr_fbd_comment), getImageView(R.id.img_fbd_fold), R.id.lnr_fbd_comment_content, null, null);
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

	private void buildCover(){
		MU.loadImage(activity, book.iconurl, AbstractBookFragment.getBookImageFileName(book), getImageView(R.id.img_book_detail_image));
	}

	private void buildTts(){
		stopTts();
		tts = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {

			@Override
			public void onInit(int status){
				if(status != TextToSpeech.ERROR){
					tts.setLanguage(Locale.US);
					myUtteranceProgressListener = new MyUtteranceProgressListener(tts, book.words, (long)activity.getIntPreference(WORD_SPEED_INTERVAL, DEFAULT_WORD_SPEED_INTERVAL_MS));
					tts.setOnUtteranceProgressListener(myUtteranceProgressListener);
				}
			}
		});
	}

	private void onDeleteIconClicked(){

		dlgBuilder.build2OptsDlgTopDown(getString(R.string.cancel), getString(R.string.delete), null, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				sendDeleteBook();
			}
		}).show();
	}

	private void sendDeleteBook(){
		callApi(UrlBuilder.deleteBook(book.id), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSendDeleteBookSuccess();
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});
	}

	private void onSendDeleteBookSuccess(){
		showAlertDialog("Info", "Delete book \"" + book.name + "\" successfully", getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which){
				onClickBackBtn();
			}
		});
	}

	private void gotoBookEditFragment(){
		BookEditFragment frg = new BookEditFragment();
		frg.setBook(book);
		activity.addFragment(frg);
	}

	private void buildWords(){
		if(book.words.size() > 0){
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
		for(final Word word : this.book.words){
			final View itemBookWord = inflater.inflate(R.layout.item_book_word, null);
			itemBookWord.setTag(word.id);
			setTextFor(itemBookWord, R.id.txt_item_book_word_edit_word, word.syllabus);
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

	private void speakWordOnly(Word word){
		List<Word> words = new ArrayList<Word>();
		words.add(word);
		myUtteranceProgressListener.setWords(words);
		setOnSpeakWordStartListener();
		myUtteranceProgressListener.setOnSpeakWordDoneListener(new MyUtteranceProgressListener.OnSpeakWordDoneListener() {

			@Override
			public void onSpeakWordDone(Word word){
				LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_book_detail_vocabulary_list);
				View itemBookWord = lnrVocabulary.findViewWithTag(word.id);
				setImageResourceFor(itemBookWord, R.id.img_item_book_word_speaker, R.drawable.ic_volume_mute_black_18dp);
			}
		});
		myUtteranceProgressListener.startSpeak();
	}

	// The accent like robot now, so don't like to use this function
	private void speakWordAndPhrase(Word word){
		List<Word> words = new ArrayList<Word>();
		words.add(word);
		// List<String> phrases = this.book.getPhrasesOfWord(word);
		// words.addAll(phrases);
		myUtteranceProgressListener.setWords(words);
		myUtteranceProgressListener.startSpeak();
	}

	private void speakAllWords(){
		myUtteranceProgressListener.setWords(book.words);
		myUtteranceProgressListener.setOnSpeakAllDoneListener(new MyUtteranceProgressListener.OnSpeakAllDoneListener() {

			@Override
			public void onSpeakAllDone(){
				setImageResourceFor(R.id.img_fragment_book_detail_speaker, R.drawable.ic_volume_mute_black_18dp);
			}
		});
		setOnSpeakWordStartListener();
		myUtteranceProgressListener.setOnSpeakWordDoneListener(new MyUtteranceProgressListener.OnSpeakWordDoneListener() {

			@Override
			public void onSpeakWordDone(Word word){
				LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_book_detail_vocabulary_list);
				View itemBookWord = lnrVocabulary.findViewWithTag(word.id);
				setImageResourceFor(itemBookWord, R.id.img_item_book_word_speaker, R.drawable.ic_volume_mute_black_18dp);
				// getView(itemBookWord, R.id.lnr_ibw).performClick();
			}
		});

		myUtteranceProgressListener.startSpeak();
	}

	private void setOnSpeakWordStartListener(){
		myUtteranceProgressListener.setOnSpeakWordStartListener(new MyUtteranceProgressListener.OnSpeakWordStartListener() {

			@Override
			public void onSpeakWordStart(Word word){
				if(getView() != null){
					LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_book_detail_vocabulary_list);
					View itemBookWord = lnrVocabulary.findViewWithTag(word.id);
					setImageResourceFor(itemBookWord, R.id.img_item_book_word_speaker, R.drawable.ic_volume_up_black_18dp);
				}
			}
		});
	}

	private void builPhrasesForWord(final Word word, View itemBookWord, LayoutInflater inflater){
		LinearLayout lnrPhrases = getLinearLayout(itemBookWord, R.id.lnr_ibw_phrases);
		lnrPhrases.removeAllViews();
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v){
				speakWordOnly(word);
			}
		};
		if(word.phrases.size() > 0){
			setFoldAction(getView(itemBookWord, R.id.lnr_ibw), getImageView(itemBookWord, R.id.img_ibw_fold), R.id.lnr_ibw_phrases, null, listener);
		}else{
			setOnClickFor(itemBookWord, R.id.lnr_ibw, listener);
			invisibleView(itemBookWord, R.id.img_ibw_fold);
		}
		for(Phrase phrase : word.phrases){
			View line = inflater.inflate(R.layout.item_phrase, null);
			setTextFor(line, R.id.txt_item_word_mark, getString(R.string.fragment_book_detail_phrase_start));
			setTextFor(line, R.id.txt_item_word_content, phrase.content);
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
