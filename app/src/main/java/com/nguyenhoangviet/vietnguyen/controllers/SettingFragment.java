package com.nguyenhoangviet.vietnguyen.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;

import com.activeandroid.query.Select;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.nguyenhoangviet.vietnguyen.controllers.Book.BookDetailFragment;
import com.nguyenhoangviet.vietnguyen.controllers.Task.TaskListFragment;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragment;
import com.nguyenhoangviet.vietnguyen.core.database.DBHelper;
import com.nguyenhoangviet.vietnguyen.models.MyModel;
import com.nguyenhoangviet.vietnguyen.models.Task;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingFragment extends MyFragment{

	// public static final int INTENT_REQUEST_CODE_SEND_EMAIL = 1001;

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
		buildGraph();
		buildContact();
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

	public void buildGraph(){
		GraphView graph = (GraphView)getView().findViewById(R.id.graph);
		LineGraphSeries<DataPoint> allTasksSeries = makeAllTasksSeries();
		LineGraphSeries<DataPoint> finishedTasksSeries = makeFinishedTasksSeries();
		// new LineGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(0, 1), new DataPoint(1, 5), new DataPoint(2, 3), new DataPoint(3, 2), new
		// DataPoint(4, 6)});
		// LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(new DataPoint[]{new DataPoint(0, 2), new DataPoint(1, 3), new
		// DataPoint(2, 19), new DataPoint(3, 3), new DataPoint(4, 6)});
		// graph.setScaleX(0.2f);
		// graph.setScaleY(1);
		graph.setTitle(getString(R.string.fragment_setting_graph_title));
		// graph.addSeries(allTasksSeries);
		graph.addSeries(finishedTasksSeries);
	}

	private LineGraphSeries<DataPoint> makeAllTasksSeries(){
		LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
		return series;
	}

	private LineGraphSeries<DataPoint> makeFinishedTasksSeries(){
		int dayNumm = 30;
		List<Task> tasks = getTasksRecently(dayNumm);
		Map<Date, ArrayList<Task>> map = getDateTasksMap(tasks);
		List<DataPoint> dataPoints = new ArrayList<DataPoint>();
		Calendar c = Calendar.getInstance();
		int i = 0;
		List<Date> keys = getKeys(dayNumm);
		for(Date key : keys){
			ArrayList<Task> dayTasks = map.get(key);
			DataPoint dataPoint;
			if(dayTasks != null){
				dataPoint = new DataPoint(i++, dayTasks.size());
			}else{
				dataPoint = new DataPoint(i++, 0);
			}
			dataPoints.add(dataPoint);
		}
		DataPoint dataPointsarray[] = dataPoints.toArray(new DataPoint[dataPoints.size()]);
		LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPointsarray);
		return series;
	}

	private Map<Date, ArrayList<Task>> getDateTasksMap(List<Task> tasks){
		Map<Date, ArrayList<Task>> map = new HashMap<Date, ArrayList<Task>>();
		for(Task task : tasks){
			Date mapKey = buildKey(task.date);
			if(map.containsKey(mapKey)){
				map.get(mapKey).add(task);
			}else{
				ArrayList<Task> tasksOnDate = new ArrayList<Task>();
				tasksOnDate.add(task);
				map.put(mapKey, tasksOnDate);
			}
		}

		return map;
	}

	private List<Date> getKeys(int day){
		List<Date> dates = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		for(int i = 0; i < day; i++){
			c.add(Calendar.DATE, 0 - i);
			Date key = buildKey(c.getTime());
			dates.add(key);
		}
		return dates;
	}

	private Date buildKey(Date date){
		Calendar origin = Calendar.getInstance();
		origin.setTime(date);
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, origin.get(Calendar.YEAR));
		c.set(Calendar.DAY_OF_MONTH, origin.get(Calendar.DAY_OF_MONTH));
		c.set(Calendar.MONTH, origin.get(Calendar.MONTH));
		return c.getTime();
	}

	// todo select in query, not all
	private List<Task> getTasksRecently(int day){
		List<Task> result = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 0 - day);
		List<Task> allTasks = new Select().from(Task.class).execute();
		for(Task task : allTasks){
			if(task.date.compareTo(c.getTime()) > 0){
				result.add(task);
			}
		}
		return result;
	}

	public void buildContact(){
		setOnClickFor(R.id.txt_fragment_setting_email, new View.OnClickListener() {

			@Override
			public void onClick(View v){
				chooseEmailActivity();
			}
		});
	}

	private void chooseEmailActivity(){
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("message/rfc822");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.fragment_setting_email)});
		emailIntent.putExtra(android.content.Intent.EXTRA_CC, getString(R.string.fragment_setting_email_cc));
		emailIntent.putExtra(android.content.Intent.EXTRA_BCC, "");
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.fragment_setting_email_subject));
		emailIntent.setType("text/html");

		startActivity(Intent.createChooser(emailIntent, getString(R.string.fragment_setting_email_choose_sender)));
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
