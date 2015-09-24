package com.example.vietnguyen.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
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
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.TaskAdapter;
import com.google.gson.Gson;

public class TaskListFragment extends MyFragment{

	private Date						targetDate;
	private Map<Date, ArrayList<Task>>	map;
	private ArrayList<Task>				tasks;
	private TaskAdapter					taskAdapter;
	private ListView					lstTask;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildCalendarPicker();
        buildAddBtn();

		map = new HashMap<Date, ArrayList<Task>>();
		this.targetDate = new Date();
		// loadTasks(this.targetDate);
		tasks = (ArrayList<Task>)getDummy();
		map.put(this.targetDate, tasks);

		taskAdapter = new TaskAdapter(activity, R.layout.item_task, tasks);

		lstTask = (ListView)getView(R.id.lst_task);
		lstTask.setAdapter(taskAdapter);
		lstTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				Task Task = (Task)adapterView.getItemAtPosition(i);
				gotoTaskDetail(Task);
			}
		});
	}

	private List<Task> getDummy(){
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task(0, 1, 1, "Do mediation", "Do mediation at least 5m each day, so you can be relieved", new Date()));
		tasks.add(new Task(1, 2, 0, "Push a commit", "Do something to keep momentum for private project", new Date()));
		tasks.add(new Task(2, 3, 0, "Install ubuntu", "Install ubuntu on this lenovo so you can work with server side", new Date()));
		tasks.add(new Task(3, 4, 1, "Read an english article", "Remember that your ultimate goal now is merged into english culture", new Date()));
		tasks.add(new Task(4, 5, 0, "Relax 5m", "Keep your chin up, your face clean", new Date()));
		return tasks;
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
						txtDate.setText(i + "/" + i2 + "/" + i3);
						Calendar c = Calendar.getInstance();
						c.set(i, i2, i3);
						targetDate = c.getTime();
						setTargetDate(targetDate);
						ArrayList<Task> targetTasks = map.get(targetDate);
						if(targetTasks != null){ // data is already on map
							tasks = targetTasks;
							taskAdapter.notifyDataSetChanged();
						}else{
							loadTasks(targetDate);
						}
					}
				});
				datePicker.show(activity.getFragmentManager(), "datePicker");
			}
		});
	}

    private void buildAddBtn() {
        TextView txtAdd = getTextView(R.id.txt_add);
        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskAddFragment frg = new TaskAddFragment();
                frg.setEdit(false, null);
                activity.addFragment(frg);
            }
        });
    }

	private void loadTasks(Date targetDate){
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("date", targetDate.toString()));
		activity.getApi(Const.GET_TASK, params, this);
	}

	@Override
	public void onApiResponse(String url, JSONObject response){
		// todo: put tasks of targetDate into map
		// map.put("name", "demo");
		// map.put("fname", "fdemo");
		tasks = map.get(this.targetDate);
		taskAdapter.notifyDataSetChanged();
	}

	public void gotoTaskDetail(Task task){
		TaskDetailFragment frg = new TaskDetailFragment();
		frg.setTask(task);
		activity.addFragment(frg);
	}

	public void setTargetDate(Date targetDate){
		this.targetDate = targetDate;
	}

	public Date getTargetDate(){
		return this.targetDate;
	}
}
