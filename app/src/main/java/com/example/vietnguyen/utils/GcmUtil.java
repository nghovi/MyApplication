package com.example.vietnguyen.utils;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.vietnguyen.controllers.MainActivity;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Notice;
import com.example.vietnguyen.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;

/**
 * GcmUtil
 */
public class GcmUtil{

	private static final int	PLAY_SERVICES_RESOLUTION_REQUEST	= 9000;

	public static String		PROPERTY_REG_ID						= "registration_id";

	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	// public static boolean checkPlayServices(Activity activity){
	// int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
	// if(resultCode != ConnectionResult.SUCCESS){
	// if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
	// GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
	// }else{
	// // Log.i(QWConst.TAG, "This device is not supported.");
	// activity.finish();
	// }
	// // activeButton.setFocusable(false);
	// // activeButton.setChecked(true);
	// return false;
	// }
	// return true;
	// }

	// public static void storeRegistrationId(Context context, String regId){
	// QkPreference preferences = new QkPreference(context);
	// preferences.set(PROPERTY_REG_ID, regId);
	// }

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	// public static SharedPreferences getGcmPreferences(Context context){
	// // This sample app persists the registration ID in shared preferences, but
	// // how you store the regID in your app is up to you.
	// return context.getSharedPreferences(NotificationSettingFragment.class.getSimpleName(), Context.MODE_PRIVATE);
	// }

	/**
	 * Gets the current registration ID for application on GCM service, if there is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 */
	// public static String getRegistrationId(Context context){
	// QkPreference preferences = new QkPreference(context);
	// String registrationId = preferences.get(PROPERTY_REG_ID);
	// return registrationId;
	// }

	/**
	 * make notification, this function will be used for both local (with AlarmManager) and remote (with Gcm)
	 */
	public static Notification makeNotification(Context context, String noticeString){

		Notice notice = MU.convertToModel(noticeString, Notice.class);
		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtra("noticeString", noticeString);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setContentTitle(context.getString(R.string.app_name)).setStyle(new NotificationCompat.BigTextStyle().bigText("Here is Title")).setContentText("Here is content");
		mBuilder.setAutoCancel(true);
		mBuilder.setSmallIcon(R.mipmap.ic_launcher);

		mBuilder.setTicker(notice.title);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

		int requestID = (int)System.currentTimeMillis();
		PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		mBuilder.setContentIntent(contentIntent);
		return mBuilder.build();
	}

	/**
	 * make notification//setup
	 */
	public static void makeLocalAlarm(Context context, Notice notice){

		ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

		Intent intent = new Intent(LocalBroadcastReceiver.ALARM_PACKAGE);
		// set data for intent
		Bundle bundle = new Bundle();
		bundle.putString(LocalBroadcastReceiver.ALARM_KEY_NOTICE, notice.toString());
		intent.putExtras(bundle);
		PendingIntent pi = PendingIntent.getBroadcast(context, 1234, intent, 0);
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		long timeNow = Calendar.getInstance().getTimeInMillis();
		alarmManager.set(AlarmManager.RTC_WAKEUP, timeNow + 10 * 1000, pi);
		intentArray.add(pi);
	}
}
