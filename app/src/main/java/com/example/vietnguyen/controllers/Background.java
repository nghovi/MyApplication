package com.example.vietnguyen.controllers;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyActivity;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Task;

import org.json.JSONObject;

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

	public static final long	SHOW_GOOD_SAY_PERIOD	= 60 * 1000;
	public static final long	REMIND_TASK_PERIOD_MS		= 5 * 60 * 1000; // 5 minute
	public static final int		CMD_SHOW_GOOD_SAY		= 1001;
	public static final int		CMD_REMIND_TASK			= 1002;

	private DialogBuilder		dlgBuilder;

	public Background(MyActivity activity){
		this.activity = activity;
		this.dlgBuilder = new DialogBuilder(this.activity);
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

		if(timerTaskRemindTask == null){
			timerTaskRemindTask = new TimerTask() {

				@Override
				public void run(){
					Message message = mHandler.obtainMessage(CMD_REMIND_TASK, "Do your task");
					message.sendToTarget();
				}
			};
		}

		if(timer == null){
			timer = new Timer();
			timer.schedule(timerTaskShowGoodSay, 2000, SHOW_GOOD_SAY_PERIOD);
			timer.schedule(timerTaskRemindTask, 1000, REMIND_TASK_PERIOD_MS);
		}

	}

	private void showGoodSay(Message message){
		Toast.makeText(activity, String.valueOf(message.obj), Toast.LENGTH_LONG).show();
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
			dlgBuilder.buildDialogNotice(activity, MU.getDateForDisplaying(randomTask.date), randomTask.name, (int)REMIND_TASK_PERIOD_MS / 2000).show();
		}
	}
}
