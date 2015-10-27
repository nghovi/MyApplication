package com.example.vietnguyen.controllers;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.example.vietnguyen.core.controllers.MyActivity;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by viet on 10/26/2015.
 */
public class Background extends AsyncTask<Integer, String, String>{

	public static Timer			timer;
	public static Handler		mHandler;
	public MyActivity			activity;
	public static TimerTask		timerTaskShowGoodSay;
	public static final long	SHOW_GOOD_SAY_PERIOD	= 60 * 1000;	// milisec
	public static final int		CMD_SHOW_GOOD_SAY		= 1234;

	public Background(MyActivity activity){
		this.activity = activity;
	}

	public void showGoodSay(){
		if(mHandler == null){
			mHandler = new Handler(Looper.getMainLooper()) {

				@Override
				public void handleMessage(Message message){
					if(message.what == CMD_SHOW_GOOD_SAY){
						Toast.makeText(activity, String.valueOf(message.obj), Toast.LENGTH_LONG).show();
					}
					// This is where you do your work in the UI thread.
					// Your worker tells you in the message what to do.
				}
			};
		}
		if(timerTaskShowGoodSay == null){
			timerTaskShowGoodSay = new TimerTask() {

				@Override
				public void run(){
					// And this is how you call it from the worker thread:
					Message message = mHandler.obtainMessage(CMD_SHOW_GOOD_SAY, "Be Yourself*");
					message.sendToTarget();
				}
			};
		}

		if(timer == null){
			timer = new Timer();
			timer.schedule(timerTaskShowGoodSay, 2000, SHOW_GOOD_SAY_PERIOD);
		}

	}

	@Override
	protected String doInBackground(Integer...integers){
		showGoodSay();
		return null;
	}

	@Override
	protected void onPostExecute(String result){
		// ShowWorkActualAsyncTask mShowTask = new ShowWorkActualAsyncTask(mActivity, mFragment, ShowWorkActualAsyncTask.WORK_ACTUAL_Z2);
		// mShowTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		// return;
	}
}
