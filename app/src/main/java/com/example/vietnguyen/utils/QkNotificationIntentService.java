package com.example.vietnguyen.utils;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * QkNotificationIntentService
 */
public class QkNotificationIntentService extends IntentService{

	public final static int		NOTIFICATION_ID	= 1;

	private NotificationManager	mNotificationManager;

	public QkNotificationIntentService(){
		super("QkLocalIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent){
		Bundle bundle = intent.getExtras();
		String response = bundle.getString(LocalBroadcastReceiver.ALARM_KEY);
//		NotificationModel model = CCJsonUtil.convertToModel(response, NotificationModel.class);
		mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = GcmUtil.makeNotification(this);
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}
}
