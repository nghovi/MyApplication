package com.nguyenhoangviet.vpcorp.controllers;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nguyenhoangviet.vpcorp.utils.GcmUtil;

/**
 * NotificationService
 */
public class NotificationService extends IntentService{

	private NotificationManager	mNotificationManager;

	public NotificationService(){
		super("NotificationService");
	}

	@Override
	protected void onHandleIntent(Intent intent){
		Bundle bundle = intent.getExtras();
		mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		String noticeId = bundle.getString(GcmUtil.BUNDLE_KEY_NOTICE_ID);
		Notification notification = GcmUtil.makeNotification(this, bundle);
		if(notification != null){
			mNotificationManager.notify(Integer.valueOf(noticeId), notification);
		}
	}
}
