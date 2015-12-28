package com.nguyenhoangviet.vietnguyen.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;

import com.nguyenhoangviet.vietnguyen.controllers.Book.BookDetailFragment;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragment;
import com.nguyenhoangviet.vietnguyen.core.database.DBHelper;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

public class SettingFragment extends MyFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		return view;
	}

	@Override
	public void buildLayout(){
		buildPushNotification();
		buildSeekBar();
	}

	private void buildPushNotification(){
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
	}

	private void buildSeekBar(){
		SeekBar seekBar = (SeekBar)getView(R.id.seek_bar_fragment_setting);
		seekBar.setMax(BookDetailFragment.SLOWEST_WORD_SPEED_INTERVAL_MS);
		int savedProgress = activity.getIntPreference(BookDetailFragment.WORD_SPEED_INTERVAL, BookDetailFragment.DEFAULT_WORD_SPEED_INTERVAL_MS);
		seekBar.setProgress(BookDetailFragment.SLOWEST_WORD_SPEED_INTERVAL_MS - savedProgress);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b){
				activity.saveIntPreference(BookDetailFragment.WORD_SPEED_INTERVAL, BookDetailFragment.SLOWEST_WORD_SPEED_INTERVAL_MS - i);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar){

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar){

			}
		});
	}

	public void builMottos(){
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
