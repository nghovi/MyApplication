package com.nguyenhoangviet.vietnguyen.controllers.Book;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.nguyenhoangviet.vietnguyen.models.Word;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by viet on 1/4/2016.
 */
public class MyUtteranceProgressListener extends UtteranceProgressListener{

	List<Word>							words;
	Iterator<Word>						iterator;
	private long						intervalMS;
	private TextToSpeech				tts;
	private final String				UTTERANCE_ID	= "UTTERANCE_ID";
	private OnSpeakAllDoneListener		onSpeakAllDoneListener;
	private OnSpeakWordDoneListener		onSpeakWordDoneListener;
	private OnSpeakWordStartListener	onSpeakWordStartListener;
	private Word						word;

	public MyUtteranceProgressListener(TextToSpeech tts, List<Word> words, long intervalMS){
		this.words = words;
		this.tts = tts;
		this.intervalMS = intervalMS;
		this.onSpeakAllDoneListener = new OnSpeakAllDoneListener() {

			@Override
			public void onSpeakAllDone(){

			}
		};
		this.onSpeakWordDoneListener = new OnSpeakWordDoneListener() {

			@Override
			public void onSpeakWordDone(Word word){

			}
		};
		this.onSpeakWordStartListener = new OnSpeakWordStartListener() {

			@Override
			public void onSpeakWordStart(Word word){

			}
		};
	}

	public interface OnSpeakAllDoneListener{

		public void onSpeakAllDone();
	}

	public interface OnSpeakWordDoneListener{

		public void onSpeakWordDone(Word word);
	}

	public interface OnSpeakWordStartListener{

		public void onSpeakWordStart(Word word);
	}

	public void setOnSpeakAllDoneListener(OnSpeakAllDoneListener listener){
		onSpeakAllDoneListener = listener;
	}

	public void setOnSpeakWordDoneListener(OnSpeakWordDoneListener listener){
		onSpeakWordDoneListener = listener;
	}

	public void setOnSpeakWordStartListener(OnSpeakWordStartListener listener){
		onSpeakWordStartListener = listener;
	}

	public void setWords(List<Word> words){
		this.words = words;
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
		if(UTTERANCE_ID.equals(s)){
			callOnSpeakWordDoneListener(word);
			if(iterator.hasNext()){
				final Word w = iterator.next();
				Timer timer = new Timer();

				timer.schedule(new TimerTask() {

					public void run(){
						speakWord(w);
					}
				}, intervalMS);
			}else{
				callOnSpeakAllDoneListener();
			}
		}
	}

	private void callOnSpeakAllDoneListener(){
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run(){
				if(tts != null) onSpeakAllDoneListener.onSpeakAllDone();
			}
		});
	}

	private void callOnSpeakWordStartListener(final Word w){
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run(){
				if(tts != null) onSpeakWordStartListener.onSpeakWordStart(w);
			}
		});
	}

	private void callOnSpeakWordDoneListener(final Word word){
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run(){
				if(tts != null) onSpeakWordDoneListener.onSpeakWordDone(word);
			}
		});
	}

	private void speakWord(Word word){
		this.word = word;
		Bundle bundle = new Bundle();
		bundle.putString(TextToSpeech.Engine.KEY_PARAM_PAN, "0");
		bundle.putString(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
		bundle.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			callOnSpeakWordStartListener(this.word);
			tts.speak(word.syllabus, TextToSpeech.QUEUE_FLUSH, bundle, UTTERANCE_ID);
		}
	}

	@Override
	public void onError(String s){

	}
}
