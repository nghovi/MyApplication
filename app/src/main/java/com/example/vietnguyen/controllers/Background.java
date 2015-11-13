package com.example.vietnguyen.controllers;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyActivity;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Motto;
import com.example.vietnguyen.models.Task;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
	public static TimerTask		timerTaskRemindTask;

	public static final long	SHOW_GOOD_SAY_PERIOD_MS	= 1 * 60 * 1000;	// 1 minute
	public static final long	REMIND_TASK_PERIOD_MS	= 5 * 60 * 1000;	// 5 minute
	public static final int		CMD_SHOW_GOOD_SAY		= 1001;
	public static final int		CMD_REMIND_TASK			= 1002;

	private DialogBuilder		dlgBuilder;
	private Dialog				dialogNotice;
	private List<Motto>			mottos;

	public Background(MyActivity activity){
		this.activity = activity;
		this.dlgBuilder = new DialogBuilder(this.activity);
		dialogNotice = dlgBuilder.buildDialogNotice(activity, MU.getDateForDisplaying(new Date()), "", (int)REMIND_TASK_PERIOD_MS / 2000);
	}

	@Override
	protected String doInBackground(Integer...integers){
		doBackground();
		return null;
	}

	@Override
	protected void onPostExecute(String result){
		// ShowWorkActualAsyncTask mShowTask = new ShowWorkActualAsyncTask(mActivity, mFragment, ShowWorkActualAsyncTask.WORK_ACTUAL_Z2);
		// mShowTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		// return;
	}

	public void doBackground(){
		loadMottoFromServer();
		createHandler();
		createShowGoodSayTask();
		createRemindTask();
		createTimer();
	}

	private void createHandler(){
		if(mHandler == null){
			mHandler = new Handler(Looper.getMainLooper()) {

				@Override
				public void handleMessage(Message message){
					switch(message.what){
					case CMD_SHOW_GOOD_SAY:
						showGoodSay(message);
						break;
					case CMD_REMIND_TASK:
						showRemindTask(message);
						break;
					default:
						break;
					}
					if(message.what == CMD_SHOW_GOOD_SAY){
					}
				}
			};
		}
	}

	private void createShowGoodSayTask(){
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
	}

	private void createRemindTask(){
		if(timerTaskRemindTask == null){
			timerTaskRemindTask = new TimerTask() {

				@Override
				public void run(){
					Message message = mHandler.obtainMessage(CMD_REMIND_TASK, "Do your task");
					message.sendToTarget();
				}
			};
		}
	}

	private void createTimer(){
		if(timer == null){
			timer = new Timer();
			timer.schedule(timerTaskShowGoodSay, 2000, SHOW_GOOD_SAY_PERIOD_MS);
			timer.schedule(timerTaskRemindTask, 1000, REMIND_TASK_PERIOD_MS);
		}
	}

	// //////////////////////////////////////////////////// GOOOD SAY //////////////////////////////////////////////////

	private String chooseRandomGoodSay(){
		mottos = new Select().from(Motto.class).execute();
		if(mottos.size() > 0){
			int randomIdx = (int)(Math.random() * mottos.size());
			return mottos.get(randomIdx).message;
		}
		return "";
	}

	private void showGoodSay(Message message){
		if(!dialogNotice.isShowing()){
			String motto = chooseRandomGoodSay();
			if(!MU.isEmpty(motto)){
				dlgBuilder.updateDialogNotice(dialogNotice, MU.getDateForDisplaying(new Date()), motto, (int)SHOW_GOOD_SAY_PERIOD_MS / 2000).show();
			}
		}
	}

	private void loadMottoFromServer(){
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("targetDate", new Date().toString()));
		activity.getApi(Const.GET_MOTTOS, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response){
				onSuccessLoadMottos(response);
			}

			@Override
			public void onApiError(String errorMsg){

			}
		});
	}

	private void onSuccessLoadMottos(JSONObject response){
		mottos = (ArrayList<Motto>)MU.convertToModelList(response.optString("data"), Motto.class);
		saveToLocal(mottos);
	}

	private void saveToLocal(List<Motto> mottos){
		new Delete().from(Motto.class).execute();
		for(Motto m : mottos){
			m.save();
		}
	}

	// ////////////////////////////////////////////////// Task //////////////////////////////////////////////////
	private void showRemindTask(Message message){
		loadUnFinishedTasksByDate(new Date());
	}

	private void loadUnFinishedTasksByDate(Date date){
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("targetDate", date.toString()));
		activity.getApi(Const.GET_UNFINISHED_TASKS, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response){
				onSuccessLoadTasksFromServer(response);
			}

			@Override
			public void onApiError(String errorMsg){

			}
		});
	}

	private void onSuccessLoadTasksFromServer(JSONObject response){
		List<Task> tasks = MU.convertToModelList(response.optString("data"), Task.class);
		showRandomUnfinishedTask(tasks);
	}

	private void showRandomUnfinishedTask(List<Task> tasks){
		if(tasks != null && tasks.size() > 0){
			int randomIdx = (int)(Math.random() * tasks.size());
			Task randomTask = tasks.get(randomIdx);
			if(!dialogNotice.isShowing()){
				dlgBuilder.updateDialogNotice(dialogNotice, MU.getDateForDisplaying(randomTask.date), randomTask.name, (int)REMIND_TASK_PERIOD_MS / 2000);
			}
		}
	}
}
