package com.nguyenhoangviet.vietnguyen.controllers.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.nguyenhoangviet.vietnguyen.core.controller.MyFragmentWithList;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.core.views.widgets.DatePickerFragment;
import com.nguyenhoangviet.vietnguyen.models.MyModel;
import com.nguyenhoangviet.vietnguyen.models.Task;
import com.nguyenhoangviet.vietnguyen.myapplication.R;
import com.nguyenhoangviet.vietnguyen.views.widgets.notifications.adapters.adapters.TaskListAdapter;

public class TaskListFragment extends MyFragmentWithList{

	private Date							targetDate;
	private Map<String, ArrayList<Task>>	map;
	private ArrayList<Task>					showedTasks;
	private Map<String, Object>				searchConditions;

	public static final String				KEY_TARGET_DATE	= "targetDate";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task_list, container, false);

	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildTargetDate();
		buildAddBtn();
		buildSearchFunction();
	}

	@Override
	protected void onClickItem(MyModel model){
		gotoTaskDetail((Task)model);
	}

	private void buildTargetDate(){
		targetDate = getUpdatedDate(KEY_TARGET_DATE, new Date());
		if(!MU.isSameDay(targetDate, new Date())){
			setTextFor(R.id.txt_fragment_task_list_date, MU.getDateForDisplaying(targetDate));
		}
		buildCalendarPicker();
	}

	private void buildSearchFunction(){
		searchConditions = (Map<String, Object>)getUpdatedData(TaskSearchFragment.KEY_TASK_SEARCH_CONDITION, new HashMap<String, Object>());
		if(searchConditions.size() > 0){
			setImageResourceFor(R.id.img_fragment_task_list_search, R.drawable.nav_btn_search_active);
			invisibleView(R.id.txt_fragment_task_list_date);
			models = AbstractTaskFragment.searchWithConditions(searchConditions);
			showTasks();
		}else{
			setImageResourceFor(R.id.img_fragment_task_list_search, R.drawable.nav_btn_search_inactive);
			reloadDailyTasks();
		}
		setOnClickFor(R.id.img_fragment_task_list_search, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				activity.addFragment(new TaskSearchFragment(), TaskSearchFragment.KEY_TASK_SEARCH_CONDITION, searchConditions);
			}
		});
	}

	private void loadTasksFromLocal(){
		models = Task.getAllUndeleted(Task.class);
	}

	private void buildCalendarPicker(){
		setOnClickFor(R.id.txt_fragment_task_list_date, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				DatePickerFragment datePicker = new DatePickerFragment();
				datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker datePicker, int i, int i2, int i3){
						Calendar c = Calendar.getInstance();
						c.set(i, i2, i3);
						targetDate = c.getTime();
						setTextFor(R.id.txt_fragment_task_list_date, MU.getDateForDisplaying(targetDate));
						reloadDailyTasks();
					}
				});
				datePicker.show(activity.getFragmentManager(), "");
			}
		});
	}

	private void buildAddBtn(){
		TextView txtAdd = getTextView(R.id.txt_fragment_task_list_add);
		txtAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				TaskAddFragment frg = new TaskAddFragment();
				activity.addFragment(frg);
			}
		});
	}

	private void showTasks(){
		adapter.updateDataWith(models);
	}

	public void reloadDailyTasks(){
		loadTasksFromLocal();
		mapTasksToDate();
		this.showedTasks = map.get(buildKey(targetDate));
		adapter.updateDataWith(this.showedTasks);
	}

	private void mapTasksToDate(){
		map = new HashMap<String, ArrayList<Task>>();
		for(MyModel model : models){
			Task task = (Task)model;
			String mapKey = buildKey(task.date);
			if(map.containsKey(mapKey)){
				map.get(mapKey).add(task);
			}else{
				ArrayList<Task> modelsOnDate = new ArrayList<Task>();
				modelsOnDate.add(task);
				map.put(mapKey, modelsOnDate);
			}
		}
		sortTaskByPriority();
	}

	private void sortTaskByPriority(){
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			ArrayList<Task> taskListByDay = (ArrayList<Task>)pair.getValue();
			Collections.sort(taskListByDay, new Comparator<Task>() {

				@Override
				public int compare(Task t1, Task t2){
					return t1.priority > t2.priority ? 1 : -1;
				}
			});
		}
	}

	public void gotoTaskDetail(Task task){
		TaskDetailFragment frg = new TaskDetailFragment();
		activity.addFragment(frg, TaskDetailFragment.BUNDLE_KEY_TASK, task);
	}

	private String buildKey(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DATE);
	}

	@Override
	public int getListViewId(){
		return R.id.lst_fragment_task_list_task;
	}

	@Override
	public void initAdapter(){
		adapter = new TaskListAdapter(this.activity, R.layout.item_task);
	}

	@Override
	public void onEmptyData(){
		super.onEmptyData();
		visibleView(R.id.txt_fragment_task_list_empty);
	}

	@Override
	public void onNotEmptyData(){
		super.onNotEmptyData();
		goneView(R.id.txt_fragment_task_list_empty);
	}
}
