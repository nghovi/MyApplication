package com.example.vietnguyen.controllers;

import java.util.Arrays;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.database.DBHelper;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;

public class SettingFragment extends MyFragment{

	private DBHelper		dbHelper;
	private ListView		listView;
	private final String	IMAGE_NAME	= "main_card1.png";

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		dbHelper = new DBHelper(getActivity());

		return view;
	}

	@Override
	public void buildLayout(){
		Switch swtPushNotification = (Switch)getView(R.id.switch_fragment_setting_push_notification);
		int showNotificationFlag = activity.getIntPreference(activity.PREF_PUSH_NOTIFICATION, 1);
		swtPushNotification.setChecked(showNotificationFlag == 1 ? true : false);
		swtPushNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b){
				int showNotificationFlag = b ? 1 : 0;
				activity.saveIntPreference(activity.PREF_PUSH_NOTIFICATION, showNotificationFlag);
			}
		});
		Switch swtShowMottos = (Switch)getView(R.id.switch_fragment_setting_push_notification);
		int showMottosFlag = activity.getIntPreference(activity.PREF_SHOW_MOTTOS, 1);
		swtShowMottos.setChecked(showMottosFlag == 1 ? true : false);
		swtShowMottos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b){
				int showMottosFlag = b ? 1 : 0;
				activity.saveIntPreference(activity.PREF_SHOW_MOTTOS, showMottosFlag);
			}
		});
	}
}
