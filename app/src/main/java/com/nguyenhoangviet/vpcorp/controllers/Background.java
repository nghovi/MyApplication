package com.nguyenhoangviet.vpcorp.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.nguyenhoangviet.vpcorp.Const;
import com.nguyenhoangviet.vpcorp.core.controller.DialogBuilder;
import com.nguyenhoangviet.vpcorp.core.controller.MyActivity;
import com.nguyenhoangviet.vpcorp.core.network.Api;
import com.nguyenhoangviet.vpcorp.core.utils.MU;
import com.nguyenhoangviet.vpcorp.models.Book;
import com.nguyenhoangviet.vpcorp.models.Motto;
import com.nguyenhoangviet.vpcorp.models.Notice;
import com.nguyenhoangviet.vpcorp.models.Task;
import com.nguyenhoangviet.vpcorp.utils.GcmUtil;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by viet on 10/26/2015.
 */
public class Background extends AsyncTask<Integer, String, String>{

	// Use static variable is not recommended -> android Unable to add window -
	public Timer				timer;
	public Handler				mHandler;
	public MyActivity			activity;
	public TimerTask			timerTaskShowGoodSay;
	// public static TimerTask timerTaskRemindTask;

	public static final long	SHOW_GOOD_SAY_PERIOD_MS	= 10 * 60 * 1000;	//
	// 10 minute
	public static final long	REMIND_TASK_PERIOD_MS	= 5 * 60 * 1000;	// 5
	// minute
	public static final int		CMD_SHOW_GOOD_SAY		= 1001;
	// public static final int CMD_REMIND_TASK = 1002;

	private DialogBuilder		dlgBuilder;
	private Dialog				dialogNotice;
	private List<Motto>			mottos;

	public Background(MyActivity activity){
		this.activity = activity;
		this.dlgBuilder = new DialogBuilder(this.activity);
		dialogNotice = dlgBuilder.buildDialogNotice(this.activity, MU.getDateForDisplaying(new Date()), "", (int)REMIND_TASK_PERIOD_MS / 2000);
	}

	@Override
	protected String doInBackground(Integer...integers){
		doOneTimeTasks();
		// startScheduledTasks();
		return null;
	}

	@Override
	protected void onPostExecute(String result){
		// ShowWorkActualAsyncTask mShowTask = new ShowWorkActualAsyncTask
		// (mActivity, mFragment, ShowWorkActualAsyncTask.WORK_ACTUAL_Z2);
		// mShowTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		// return;
	}

	public void doOneTimeTasks(){
		// loadMottoFromServer();
		// saveUnsavedTaskToServer();
		// saveUnsavedTaskToLocal();// for new Device
		// deleteTaskToLocal();// for another Device
		// saveUnsavedBookToServer();
		// saveUnsavedBookToLocal();// for new Device
		 deleteOverdueNotices();
		// saveUnsavedNoticeToServer();
		// saveUnsavedNoticeToLocal();
		 setupAlarm();
	}

	public void startScheduledTasks(){
		if(timer == null){
			timer = new Timer();
			createHandler();
			createTaskShowGoodSayTask();
			timer.schedule(timerTaskShowGoodSay, 2000, SHOW_GOOD_SAY_PERIOD_MS);
			// timer.schedule(timerTaskRemindTask, 1000, REMIND_TASK_PERIOD_MS);
		}
	}

	public void stopScheduledTasks(){
		timerTaskShowGoodSay.cancel();
		timerTaskShowGoodSay = null;
		timer.cancel();
		timer = null;
	}

	private void setupAlarm(){
		List<Notice> notices = Notice.getOnGoingNotices();
		for(Notice notice : notices){
			GcmUtil.makeLocalAlarm(activity, notice);
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

	private void saveUnsavedTaskToServer(){
		List<Task> unsavedToRemoteTasks = Task.getUnSavedToRemote(Task.class);
		for(final Task task : unsavedToRemoteTasks){
			JSONObject param = MU.buildJsonObj(Arrays.asList("task", task.toString()));
			activity.postApi(Const.EDIT_TASK, param, new Api.OnCallApiListener() {

				@Override
				public void onApiResponse(JSONObject response){
					task.isRemoteSaved = true;
					task.id = response.optString("data");
					task.save();
					MU.log("Background sync task, saved task successfully " + "for" + task.getId());
				}

				@Override
				public void onApiError(String errorMsg){
					MU.log("Background sync task, saved task FAILED for" + task.getId());
				}
			});
		}
	}

	private void saveUnsavedTaskToLocal(){
		JSONObject params = new JSONObject();
		activity.getApi(Const.GET_TASKS, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response){
				List<Task> tasksFromServer = MU.convertToModelList(response.optString("data"), Task.class);
				for(Task t : tasksFromServer){
					Task local = new Select().from(Task.class).where("id = " + "?", t.id).executeSingle();
					if(local == null){
						t.isRemoteSaved = true;
						t.save();
						MU.log("Save task " + t.id + " into local database");
					}
				}
			}

			@Override
			public void onApiError(String errorMsg){
				MU.log("saveUnsavedTaskToLocal Failed");
			}
		});
	}

	// For new device but same account + save memory
	private void deleteTaskToLocal(){
		JSONObject params = new JSONObject();
		activity.getApi(Const.GET_DELETED_TASKS, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response){
				List<Task> tasksFromServer = MU.convertToModelList(response.optString("data"), Task.class);
				for(Task t : tasksFromServer){
					Task local = new Select().from(Task.class).where("id = " + "?", t.id).executeSingle();
					if(local != null){
						MU.log("Delete task at Local " + local.id);
						local.delete();
					}
				}
			}

			@Override
			public void onApiError(String errorMsg){
				MU.log("deleteTaskToLocal Failed");
			}
		});
	}

	private void saveUnsavedBookToServer(){
		List<Book> unsavedToRemoteBooks = Book.getAllUndeleted(Book.class);
		for(final Book book : unsavedToRemoteBooks){
			JSONObject param = MU.buildJsonObj(Arrays.asList("book", book.toString()));
			activity.postApi(Const.EDIT_BOOK, param, new Api.OnCallApiListener() {

				@Override
				public void onApiResponse(JSONObject response){
					book.isRemoteSaved = true;
					book.id = response.optString("data");
					book.save();
					MU.log("Background sync book, saved book successfully " + "for" + book.getId());
				}

				@Override
				public void onApiError(String errorMsg){
					MU.log("Background sync book, saved book FAILED for" + book.getId());
				}
			});
		}
	}

	private void saveUnsavedBookToLocal(){
		JSONObject params = new JSONObject();
		activity.getApi(Const.GET_BOOKS, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response){
				List<Book> booksFromServer = MU.convertToModelList(response.optString("data"), Book.class);
				for(Book t : booksFromServer){
					Book local = new Select().from(Book.class).where("id = " + "?", t.id).executeSingle();
					if(local == null){
						t.isRemoteSaved = true;
						t.save();
						MU.log("Save Book " + t.id + " into local database");
					}
				}
			}

			@Override
			public void onApiError(String errorMsg){
				MU.log("saveUnsavedBookToLocal Failed");
			}
		});
	}

	private void deleteOverdueNotices(){
		Notice.deleteOverdueNotices();
	}

	private void saveUnsavedNoticeToServer(){
		List<Notice> unsavedToRemoteNotices = Notice.getUnSavedToRemote();
		for(final Notice notice : unsavedToRemoteNotices){
			JSONObject param = MU.buildJsonObj(Arrays.asList("notice", notice.toString()));
			activity.postApi(Const.EDIT_NOTICE, param, new Api.OnCallApiListener() {

				@Override
				public void onApiResponse(JSONObject response){
					notice.isRemoteSaved = true;
					notice.id = response.optString("data");
					notice.save();
					MU.log("Background sync notice to server, saved notice " + "successfully for" + notice.getId());
				}

				@Override
				public void onApiError(String errorMsg){
					MU.log("Background sync notice to server, saved notice " + "FAILED for" + notice.getId());
				}
			});
		}
	}

	private void saveUnsavedNoticeToLocal(){
		JSONObject params = new JSONObject();
		activity.getApi(Const.GET_NOTICES, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response){
				List<Notice> noticesFromServer = MU.convertToModelList(response.optString("data"), Notice.class);
				for(Notice notice : noticesFromServer){
					Notice local = new Select().from(Notice.class).where("id " + "= ?", notice.id).executeSingle();
					if(local == null){
						notice.isRemoteSaved = true;
						notice.save();
						MU.log("Save Notice " + notice.id + " into local " + "database");
					}
				}
			}

			@Override
			public void onApiError(String errorMsg){
				MU.log("saveUnsavedNoticeToLocal Failed");
			}
		});
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
					// case CMD_REMIND_TASK:
					// showRemindTask(message);
					// break;
					default:
						break;
					}
					if(message.what == CMD_SHOW_GOOD_SAY){
					}
				}
			};
		}
	}

	private void createTaskShowGoodSayTask(){
		if(timerTaskShowGoodSay == null){
			timerTaskShowGoodSay = new TimerTask() {

				@Override
				public void run(){
					// And this is how you call it from the worker thread:
					// MU.log("create showGoodSay, hahah");
					Message message = mHandler.obtainMessage(CMD_SHOW_GOOD_SAY, "Be Yourself*");
					message.sendToTarget();
				}
			};
		}
	}

	// private void createRemindTask(){
	// if(timerTaskRemindTask == null){
	// timerTaskRemindTask = new TimerTask() {
	//
	// @Override
	// public void run(){
	// Message message = mHandler.obtainMessage(CMD_REMIND_TASK, "Do your
	// task");
	// message.sendToTarget();
	// }
	// };
	// }
	// }

	// //////////////////////////////////////////////////// GOOOD SAY
	// ////////////////////////////////////////////////

	private String chooseRandomGoodSay(){
		mottos = new Select().from(Motto.class).execute();
		if(mottos.size() > 0){
			int randomIdx = (int)(Math.random() * mottos.size());
			MU.log("*******Show random motto " + randomIdx + " of total " + mottos.size());
			return mottos.get(randomIdx).message;
		}
		return "";
	}

	private void showGoodSay(Message message){
		if(!dialogNotice.isShowing() && activity.getIntPreference(activity.PREF_SHOW_MOTTOS, 1) == 1){
			String motto = chooseRandomGoodSay();
			if(!MU.isEmpty(motto)){
				dlgBuilder.updateDialogNotice(dialogNotice, MU.getDateForDisplaying(new Date()), motto, (int)SHOW_GOOD_SAY_PERIOD_MS / 2000).show();
			}
		}
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

	// ////////////////////////////////////////////////// Task
	// ////////////////////////////////////////////////
	// private void showRemindTask(Message message){
	// loadUnFinishedTasksByDate(new Date());
	// }

	// private void loadUnFinishedTasksByDate(Date date){
	// JSONObject params = MU.buildJsonObj(Arrays.<String>asList
	// ("targetDate", date.toString()));
	// activity.getApi(Const.GET_UNFINISHED_TASKS, params, new Api
	// .OnCallApiListener() {
	//
	// @Override
	// public void onApiResponse(JSONObject response){
	// onSuccessLoadTasksFromServer(response);
	// }
	//
	// @Override
	// public void onApiError(String errorMsg){
	//
	// }
	// });
	// }

	// private void onSuccessLoadTasksFromServer(JSONObject response){
	// List<Task> tasks = MU.convertToModelList(response.optString("data"),
	// Task.class);
	// showRandomUnfinishedTask(tasks);
	// }

	// private void showRandomUnfinishedTask(List<Task> tasks){
	// if(tasks != null && tasks.size() > 0){
	// int randomIdx = (int)(Math.random() * tasks.size());
	// Task randomTask = tasks.get(randomIdx);
	// if(!dialogNotice.isShowing()){
	// dlgBuilder.updateDialogNotice(dialogNotice, MU.getDateForDisplaying
	// (randomTask.date), randomTask.name, (int)REMIND_TASK_PERIOD_MS / 2000);
	// }
	// }
	// }

}
