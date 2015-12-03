package com.example.vietnguyen.utils;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * AlarmReceiver
 *
 * @author TrungND
 */
public class LocalBroadcastReceiver extends WakefulBroadcastReceiver {

	public static final String	ALARM_PACKAGE		= "com.example.vietnguyen.utils.LocalBroadcastReceiver";

	@Override
	public void onReceive(Context context, Intent intent){
//		 Explicitly specify that GcmIntentService will handle the intent.
		ComponentName comp = new ComponentName(context.getPackageName(), QkNotificationIntentService.class.getName());
		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, (intent.setComponent(comp)));
//		setResultCode(Activity.RESULT_OK);
	}


	public void cancelAlarm(Context context, Integer notificationId){
		Intent intent = new Intent(context, LocalBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, notificationId, intent, 0);
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}
