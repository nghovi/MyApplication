package com.nguyenhoangviet.vietnguyen.utils;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.activeandroid.query.Select;
import com.nguyenhoangviet.vietnguyen.controllers.LocalBroadcastReceiver;
import com.nguyenhoangviet.vietnguyen.controllers.MainActivity;
import com.nguyenhoangviet.vietnguyen.core.controller.MyActivity;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Notice;
import com.nguyenhoangviet.vietnguyen.models.Task;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

/**
 * GcmUtil
 */
public class GcmUtil{

	public final static String	BUNDLE_KEY_NOTICE		= "NOTICE_STRING";
	public final static String	BUNDLE_KEY_NOTICE_ID	= "NOTICE_ID";
	public int					requestCode				= 1234;

	/**
	 * make notification, this function will be used for both local (with AlarmManager) and remote (with Gcm)
	 * for Service
	 */
	public static Notification makeNotification(Context context, Bundle bundle){
		String noticeId = "";
		if(bundle != null){
			noticeId = bundle.getString(BUNDLE_KEY_NOTICE_ID);
		}

		int pushNotificationFlag = context.getSharedPreferences(MyActivity.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE).getInt(MyActivity.PREF_PUSH_NOTIFICATION, 1);
		if(MU.isEmpty(noticeId) || pushNotificationFlag == 0){
			return null;
		}

		Notice notice = new Select().from(Notice.class).where("otherId = ?", noticeId).executeSingle();
		if(notice != null && notice.isDeleted == false){
			Intent intent = new Intent(context, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(BUNDLE_KEY_NOTICE, notice.toString());
			String notificationText = getNotificationText(notice);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setContentTitle(context.getString(R.string.app_name)).setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText)).setContentText(notificationText);
			mBuilder.setAutoCancel(true);
			mBuilder.setSmallIcon(R.drawable.ico_note_32_on);

			mBuilder.setTicker(notice.title);
			mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

			int requestID = (int)System.currentTimeMillis();
			PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

			mBuilder.setContentIntent(contentIntent);
			return mBuilder.build();
		}
		return null;
	}

	private static String getNotificationText(Notice notice){
		if(Notice.NOTICE_TYPE_TASK.equals(notice.type)){
			Task task = new Select().from(Task.class).where("otherId=?", notice.value).executeSingle();
			if(task != null){
				return task.name;
			}
		}
		return notice.title;
	}

	/**
	 * make notification//setup
	 */
	public static void makeLocalAlarm(Context context, Notice notice){

		if(notice != null && notice.isDeleted == false && !MU.isInThePast(notice.noticeDate)){

			ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

			Intent intent = new Intent(LocalBroadcastReceiver.ALARM_PACKAGE);
			// set data for intent
			Bundle bundle = new Bundle(); // will be past to Service -> call gcm util make Notification...
			bundle.putString(BUNDLE_KEY_NOTICE_ID, notice.getId().toString());
			intent.putExtras(bundle);
			PendingIntent pi = PendingIntent.getBroadcast(context, notice.getId().intValue(), intent, 0);
			AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, notice.noticeDate.getTime(), pi);
			intentArray.add(pi);
		}
	}
}
