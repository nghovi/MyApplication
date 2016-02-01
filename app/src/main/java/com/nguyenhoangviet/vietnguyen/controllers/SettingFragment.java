package com.nguyenhoangviet.vietnguyen.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.activeandroid.query.Select;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.nguyenhoangviet.vietnguyen.controllers.Book.BookDetailFragment;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragment;
import com.nguyenhoangviet.vietnguyen.models.Task;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingFragment extends MyFragment{

	// public static final int INTENT_REQUEST_CODE_SEND_EMAIL = 1001;

	private int	taskUnfinishedNum	= 0;
	private int	taskFinishedNum		= 0;

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
		final int dayBefore = 30;
		final int dayAfter = 10;
		GraphView graph = (GraphView)getView().findViewById(R.id.graph);
		BarGraphSeries<DataPoint> unfinishedTasksSeries = makeUnfinishedTasksSeries(dayBefore, dayAfter);
		unfinishedTasksSeries.setColor(Color.BLACK);
		unfinishedTasksSeries.setTitle(getString(R.string.fragment_setting_graph_unfinished_task_title));
		BarGraphSeries<DataPoint> finishedTasksSeries = makeFinishedTasksSeries(dayBefore, dayAfter);
		finishedTasksSeries.setTitle(getString(R.string.fragment_setting_graph_finished_task_title));
		finishedTasksSeries.setColor(Color.GRAY);

		graph.addSeries(unfinishedTasksSeries);
		graph.addSeries(finishedTasksSeries);
		graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {

			@Override
			public String formatLabel(double value, boolean isValueX){
				if(isValueX){
					// show normal x values
					return super.formatLabel(value - dayBefore, isValueX);
				}else{
					// show currency for y values
					return super.formatLabel(value, isValueX) + " T";
				}
			}
		});
		graph.getLegendRenderer().setVisible(true);
		graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
		graph.setTitle(getString(R.string.fragment_setting_graph_title, taskFinishedNum, taskFinishedNum + taskUnfinishedNum));
	}

	private BarGraphSeries<DataPoint> makeUnfinishedTasksSeries(int dayBefore, int dayAfter){
		List<Task> tasks = getUnfinishedTasksRecently(dayBefore, dayAfter);
		this.taskUnfinishedNum = tasks.size();
		DataPoint dataPointsarray[] = makeTasksDataPoints(tasks, dayBefore, dayAfter);
		BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPointsarray);
		return series;
	}

	private BarGraphSeries<DataPoint> makeFinishedTasksSeries(int dayBefore, int dayAfter){
		List<Task> tasks = getFinishedTasksRecently(dayBefore, dayAfter);
		this.taskFinishedNum = tasks.size();
		DataPoint dataPointsarray[] = makeTasksDataPoints(tasks, dayBefore, dayAfter);
		BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPointsarray);
		return series;
	}

	private DataPoint[] makeTasksDataPoints(List<Task> tasks, int dayBefore, int dayAfter){
		Map<Date, ArrayList<Task>> map = getDateTasksMap(tasks);
		List<DataPoint> dataPoints = new ArrayList<DataPoint>();
		Calendar c = Calendar.getInstance();
		int i = 0;
		List<Date> keys = getKeys(dayBefore, dayAfter);
		for(Date key : keys){
			ArrayList<Task> tasksOnDate = map.get(key);
			DataPoint dataPoint;
			if(tasksOnDate != null){
				dataPoint = new DataPoint(i++, tasksOnDate.size());
			}else{
				dataPoint = new DataPoint(i++, 0);
			}
			dataPoints.add(dataPoint);
		}
		return dataPoints.toArray(new DataPoint[dataPoints.size()]);
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

	private void sortMapByDate(List<Date> dates){

		Collections.sort(dates, new Comparator<Date>() {

			@Override
			public int compare(Date d1, Date d2){
				return d1.compareTo(d2);
			}
		});
	}

	private List<Date> getKeys(int dayBefore, int dayAfter){
		List<Date> dates = new ArrayList<>();
		Calendar c;
		for(int i = 1; i <= dayBefore; i++){
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 0 - i);
			Date key = buildKey(c.getTime());
			dates.add(key);
		}

		for(int i = 0; i < dayAfter; i++){
			c = Calendar.getInstance();
			c.add(Calendar.DATE, i);
			Date key = buildKey(c.getTime());
			dates.add(key);
		}
		sortMapByDate(dates);
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

	private List<Task> getUnfinishedTasksRecently(int dayBefore, int dayAfter){
		return getTasksRecently(dayBefore, dayAfter, Task.STATUS_UNFINISHED);
	}

	private List<Task> getFinishedTasksRecently(int dayBefore, int dayAfter){
		return getTasksRecently(dayBefore, dayAfter, Task.STATUS_FINISHED);
	}

	private List<Task> getTasksRecently(int dayBefore, int dayAfter, int taskStatus){
		List<Task> result = new ArrayList<>();
		Calendar Before = Calendar.getInstance();
		Before.add(Calendar.DATE, 0 - dayBefore);

		Calendar cAfter = Calendar.getInstance();
		cAfter.add(Calendar.DATE, dayAfter);

		List<Task> allTasks = new Select().from(Task.class).execute();
		for(Task task : allTasks){
			if(task.status == taskStatus && task.date.compareTo(Before.getTime()) > 0 && task.date.compareTo(cAfter.getTime()) < 0){
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
