package com.example.vietnguyen.utils;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.activeandroid.query.Select;
import com.example.vietnguyen.controllers.LocalBroadcastReceiver;
import com.example.vietnguyen.controllers.MainActivity;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Notice;
import com.example.vietnguyen.myapplication.R;

/**
 * GcmUtil
 */
public class GcmUtil{

	public final static String	BUNDLE_KEY_NOTICE		= "NOTICE_STRING";
	public final static String	BUNDLE_KEY_NOTICE_ID	= "NOTICE_ID";

	/**
	 * make notification, this function will be used for both local (with AlarmManager) and remote (with Gcm)
	 * for Service
	 */
	public static Notification makeNotification(Context context, Bundle bundle){
		String noticeId = "";
		if(bundle != null){
			noticeId = bundle.getString(BUNDLE_KEY_NOTICE_ID);
		}

		if(MU.isEmpty(noticeId)){
			return null;
		}

		Notice notice = new Select().from(Notice.class).where("id = ?", noticeId).executeSingle();
		if(notice != null && notice.isDeleted == false){
			Intent intent = new Intent(context, MainActivity.class);
			intent.putExtra(BUNDLE_KEY_NOTICE, notice.toString());
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setContentTitle(context.getString(R.string.app_name)).setStyle(new NotificationCompat.BigTextStyle().bigText(notice.title)).setContentText(notice.message);
			mBuilder.setAutoCancel(true);
			mBuilder.setSmallIcon(R.mipmap.ic_launcher);

			mBuilder.setTicker(notice.title);
			mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

			int requestID = (int)System.currentTimeMillis();
			PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

			mBuilder.setContentIntent(contentIntent);
			return mBuilder.build();
		}
		return null;
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
			PendingIntent pi = PendingIntent.getBroadcast(context, 1234, intent, 0);
			AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, notice.noticeDate.getTime(), pi);
			intentArray.add(pi);
		}
	}
}
