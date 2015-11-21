package com.example.vietnguyen.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.DatePickerFragment;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.TaskAdapter;

public class TaskListFragment extends MyFragment{

	private Date							targetDate;
	private Map<String, ArrayList<Task>>	map;
	private List<Task>						tasks;
	private ArrayList<Task>					showedTasks;
	private TaskAdapter						taskAdapter;
	private ListView						lstTask;
	private Map<String, String>				searchCondition;

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
		buildListTask();
		tasks = new ArrayList<Task>();
	}

	private void buildListTask() {
		lstTask = (ListView)getView(R.id.lst_task);
		lstTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Task Task = (Task) adapterView.getItemAtPosition(i);
				gotoTaskDetail(Task);
			}
		});
	}

	private void buildTargetDate() {
		targetDate = getUpdatedDate(KEY_TARGET_DATE, new Date());
		if(!MU.isSameDay(targetDate, new Date())) {
			setTextFor(R.id.txt_fragment_task_list_date, MU.getDateForDisplaying(targetDate));
		}
		buildCalendarPicker();
	}

	private void buildSearchFunction() {
		setOnClickFor(R.id.img_fragment_task_list_search, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (searchCondition != null) {
					activity.addFragment(new TaskSearchFragment(), TaskSearchFragment.KEY_TASK_SEARCH_CONDITION, searchCondition);
				} else {
					activity.addFragment(new TaskSearchFragment());
				}
			}
		});
		Map<String, Object> searchResult = (Map<String, Object>)getUpdatedData(TaskSearchFragment.KEY_TASK_SEARCH_RESULT, new HashMap<String, Object>());
		if(searchResult.containsKey(TaskSearchFragment.KEY_TASK_SEARCH_FLAG) && (boolean)searchResult.get(TaskSearchFragment.KEY_TASK_SEARCH_FLAG)){
			setImageResourceFor(R.id.img_fragment_task_list_search, R.drawable.nav_btn_search_active);
			tasks = (List<Task>)searchResult.get(TaskSearchFragment.KEY_TASK_SEARCH_LIST);
			searchCondition = (Map<String, String>)searchResult.get(TaskSearchFragment.KEY_TASK_SEARCH_CONDITION);
			invisibleView(R.id.txt_fragment_task_list_date);
			showTasks(true);
		}else{
			searchCondition = null;
			setImageResourceFor(R.id.img_fragment_task_list_search, R.drawable.nav_btn_search_inactive);
			loadTasksFromServer(this.targetDate);
		}
	}



	private void loadTasksFromLocal(){
		tasks = new Select().from(Task.class).execute();
	}

	private void buildCalendarPicker(){
		final TextView txtDate = getTextView(R.id.txt_fragment_task_list_date);
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
						showTasks(false);
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
				activity.addFragment(frg);
			}
		});
	}

	private void loadTasksFromServer(Date targetDate){
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("targetDate", targetDate.toString()));
		getApi(Const.GET_TASKS, params, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response){
				onSuccessLoadTasksFromServer(response);
			}

			@Override
			public void onApiError(String errorMsg){
				onFailureLoadTasksFromServer();
			}
		});
	}

	public void onSuccessLoadTasksFromServer(JSONObject response){
		tasks = MU.convertToModelList(response.optString("data"), Task.class);
		saveTaskToLocal(tasks);
		showTasks(false);
	}

	public void saveTaskToLocal(List<Task> taskList){
		new Delete().from(Task.class).execute();
		for(Task task : taskList){
			task.save();
		}
	}

	// if error while loading from server, show local tasks only
	public void onFailureLoadTasksFromServer(){
		goneView(lstTask);
		setTextFor(R.id.task_list_empty_txt, "Check your network");
		visibleView(getTextView(R.id.task_list_empty_txt));
	}

	private void showTasks(boolean isFiltered){
		if(isFiltered){
			this.showedTasks = new ArrayList<Task>();
			this.showedTasks.addAll(tasks);
		}else{
			loadTasksFromLocal();
			mapTasksToDate();
			this.showedTasks = map.get(buildKey(targetDate));
		}

		View txtEmpty = getView().findViewById(R.id.task_list_empty_txt);
		if(txtEmpty != null){
			if(this.showedTasks == null || this.showedTasks.size() == 0){
				goneView(lstTask);
				visibleView(getTextView(R.id.task_list_empty_txt));
			}else{
				visibleView(lstTask);
				goneView(getTextView(R.id.task_list_empty_txt));
				this.taskAdapter = new TaskAdapter(activity, R.layout.item_task, this.showedTasks, isFiltered);
				lstTask.setAdapter(taskAdapter);
			}
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
