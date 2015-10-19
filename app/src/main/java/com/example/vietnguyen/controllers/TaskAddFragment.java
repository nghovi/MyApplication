package com.example.vietnguyen.controllers;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.DatePickerFragment;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

public class TaskAddFragment extends MyFragment{

	private Date		targetDate;
	private boolean		isEdit;
	private Task		task;
	private EditText	edtName;
	private EditText	edtDescription;
	private EditText	edtComment;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		targetDate = new Date();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task_add, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildCalendarPicker();
		TextView txtCommit = getTextView(R.id.txt_add);
		String commitText = "Add";
		edtName = getEditText(R.id.txt_name);
		edtDescription = getEditText(R.id.txt_description);
		if(isEdit){
			commitText = "Done";
			edtName.setText(this.task.name);
			edtDescription.setText(this.task.description);
			TextView txtDate = getTextView(R.id.txt_date);
			targetDate = task.date;
			txtDate.setText(targetDate.toString());
		}else{
			txtCommit.setVisibility(View.INVISIBLE);
			MU.addTextWatcher(txtCommit, Arrays.asList((View)edtName, edtDescription));
		}
		txtCommit.setText(commitText);
		txtCommit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(isEdit){
					updateTask();
				}else{
					addNewTask();
				}
			}
		});
		ImageView imgBack = getImageView(R.id.img_back);
		imgBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				backToTaskList();
			}
		});
	}

	private void addNewTask(){
		TextView txtName = getTextView(R.id.txt_name);
		TextView txtDescription = getTextView(R.id.txt_description);
		Task newTask = new Task(1, Task.STATUS_UNFINISHED, txtName.getText().toString(), txtDescription.getText().toString(), targetDate, targetDate);
		Toast.makeText(activity, "Save to local", Toast.LENGTH_SHORT).show();
		newTask.save();
		JSONObject param = MU.buildJsonObj(Arrays.asList("task", newTask.toString()));
		postApi(Const.ADD_TASK, param);
	}

	private void updateTask(){
		TextView txtName = getTextView(R.id.txt_name);
		TextView txtDescription = getTextView(R.id.txt_description);
		task.name = txtName.getText().toString();
		task.description = txtDescription.getText().toString();
		task.date = targetDate;
		task.save();
		Toast.makeText(activity, "Save to local", Toast.LENGTH_SHORT).show();
		JSONObject param = MU.buildJsonObj(Arrays.asList("task", task.toString()));
		postApi(Const.EDIT_TASK, param);
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
					}
				});
				datePicker.show(activity.getFragmentManager(), "datePicker");
			}
		});
	}

	@Override
	public void onApiResponse(String url, JSONObject response){
		super.onApiResponse(url, response);
		switch(url){
		case Const.ADD_TASK:
			Toast.makeText(activity, "Save new task to server success", Toast.LENGTH_SHORT).show();
			backToTaskList();
			break;
		case Const.EDIT_TASK:
			Toast.makeText(activity, "Save task to server success", Toast.LENGTH_SHORT).show();
			backToTaskList();
			break;
		}
	}

	@Override
	public void onApiError(String url, String errorMsg){
		switch(url){
		case Const.ADD_TASK:
			Toast.makeText(activity, "Save new task to server failed", Toast.LENGTH_SHORT).show();
			backToTaskList();
			break;
		case Const.EDIT_TASK:
			Toast.makeText(activity, "Save to server failed", Toast.LENGTH_SHORT).show();
			backToTaskList();
			break;
		}
	}

	private void backToTaskList(){
		activity.backToFragment(TaskListFragment.class, TaskListFragment.KEY_TARGET_DATE, targetDate);
	}

	public void setEdit(boolean isEdit, Task task){
		this.isEdit = isEdit;
		this.task = task;
	}
}
