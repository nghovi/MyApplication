package com.nguyenhoangviet.vietnguyen.controllers.Book;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by viet on 1/4/2016.
 */
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

	public void setWords(List<String> words){
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
