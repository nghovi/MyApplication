package com.example.vietnguyen.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.DatePickerFragment;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.TaskAdapter;

public class TaskListFragment extends MyFragment{

	private Date							targetDate;
	private Map<String, ArrayList<Task>>	map;
	private ArrayList<Task>					tasks;
	private ArrayList<Task>					showedTasks;
	private TaskAdapter						taskAdapter;
	private ListView						lstTask;

	public static final String				KEY_TARGET_DATE	= "targetDate";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task_list, container, false);

	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildCalendarPicker();
		buildAddBtn();

		targetDate = getUpdatedDate(KEY_TARGET_DATE, new Date());

		lstTask = (ListView)getView(R.id.lst_task);
		lstTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				Task Task = (Task)adapterView.getItemAtPosition(i);
				gotoTaskDetail(Task);
			}
		});
		if(!MU.isSameDay(targetDate, new Date())){
			TextView txtDate = getTextView(R.id.txt_date);
			txtDate.setText(MU.getDateForDisplaying(targetDate));
		}
		loadTasks(this.targetDate);
	}

	private void buildCalendarPicker(){
		final TextView txtDate = getTextView(R.id.txt_date);
		txtDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				DatePickerFragment datePicker = new DatePickerFragment();
				datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker datePicker, int i, int i2, int i3){
						Calendar c = Calendar.getInstance();
						c.set(i, i2, i3);
						targetDate = c.getTime();
						txtDate.setText(MU.getDateForDisplaying(targetDate));
						showTasks();
					}
				});
				datePicker.show(activity.getFragmentManager(), "datePicker");
			}
		});
	}

	private void buildAddBtn(){
		TextView txtAdd = getTextView(R.id.txt_add);
		txtAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				TaskAddFragment frg = new TaskAddFragment();
				frg.setEdit(false, null);
				activity.addFragment(frg);
			}
		});
	}

	private void loadTasks(Date targetDate){
		loadFromLocal(targetDate);
		showTasks();
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("targetDate", targetDate.toString()));
		activity.getApi(Const.GET_TASKS, params, this);
		// showTasks();
	}

	private void loadFromLocal(Date targetDate){
		tasks = new ArrayList<Task>();
		Iterator<Task> taskList = Task.findAll(Task.class);
		while(taskList.hasNext()){
			tasks.add(taskList.next());
		}
	}

	@Override
	public void onApiResponse(String url, JSONObject response){
		super.onApiResponse(url, response);
		// todo: back and reload task list
		switch(url){
		case Const.GET_TASKS:
			onSuccessLoadTasksFromServer(response);
		case Const.EDIT_TASK:
			break;
		}
	}

	@Override
	public void onApiError(String url, String errorMsg){
		super.onApiError(url, errorMsg);
		switch(url){
		case Const.GET_TASKS:
			onFailureLoadTasksFromServer();
		case Const.EDIT_TASK:
			break;
		}

	}

	public void onSuccessLoadTasksFromServer(JSONObject response){
		ArrayList<Task> loadedTasks = (ArrayList<Task>)MU.convertToModelList(response.optString("data"), Task.class);
		ArrayList<Task> needSaveToServer = new ArrayList<Task>();
		ArrayList<Task> needSaveToLocal = new ArrayList<Task>();

		boolean notSavedToServer = true;
		for(Task localTask : tasks){
			notSavedToServer = true;
			for(Task serverTask : loadedTasks){
				if(serverTask.getId() == localTask.getId()){
					notSavedToServer = false;
					long serverTimeInSec = serverTask.lastupdated.getTime() / 1000;
					long localTimeInSec = localTask.lastupdated.getTime() / 1000;
					if(serverTimeInSec > localTimeInSec){ // server task is newer
						tasks.remove(localTask);
						tasks.add(serverTask);
						needSaveToLocal.add(serverTask);
					}else if(serverTimeInSec < localTimeInSec){
						needSaveToServer.add(localTask);
					}
				}
			}
			if(notSavedToServer){
				needSaveToServer.add(localTask);
			}
		}

		// Chek new task from server
		Boolean isNew = true;
		for(Task serverTask : loadedTasks){
			isNew = true;
			for(Task localTask : tasks){
				if(localTask.getId() == serverTask.getId()){
					isNew = false;
					break;
				}
			}
			if(isNew){
				tasks.add(serverTask);
				needSaveToLocal.add(serverTask);
			}
		}

		saveToLocal(needSaveToLocal);
		saveToServer(needSaveToServer);
		showTasks();
	}

	// if error while loading from server, show local tasks only
	public void onFailureLoadTasksFromServer(){
		showTasks();
	}

	private void showTasks(){
		mapTasksToDate();
		this.showedTasks = map.get(buildKey(targetDate));
		if(this.showedTasks == null){
			this.showedTasks = new ArrayList<Task>();
		}
		this.taskAdapter = new TaskAdapter(activity, R.layout.item_task, this.showedTasks);
		lstTask.setAdapter(taskAdapter);
	}

	private void saveToLocal(ArrayList<Task> tasks){
		for(Task task : tasks){
			task.save();
		}
	}

	private void saveToServer(ArrayList<Task> tasks){
		for(Task task : tasks){
			JSONObject param = MU.buildJsonObj(Arrays.asList("task", task.toString()));
			postApi(Const.EDIT_TASK, param);
		}
	}

	private void mapTasksToDate(){
		map = new HashMap<String, ArrayList<Task>>();
		for(Task task : tasks){
			String mapKey = buildKey(task.date);
			if(map.containsKey(mapKey)){
				map.get(mapKey).add(task);
			}else{
				ArrayList<Task> tasksOnDate = new ArrayList<Task>();
				tasksOnDate.add(task);
				map.put(mapKey, tasksOnDate);
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
		frg.setTask(task);
		activity.addFragment(frg);
	}

	private String buildKey(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DATE);
	}
}
