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

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.DatePickerFragment;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;
import com.google.gson.Gson;

public class TaskAddFragment extends MyFragment{

	private Date	targetDate;
	private boolean	isEdit;
	private Task	task;
    private EditText edtName;
    private EditText edtDescription;
    private EditText edtComment;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.targetDate = new Date();
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
		if(isEdit){
			commitText = "Done";
            edtName = getEditText(R.id.txt_name);
            edtName.setText(this.task.name);
            edtDescription = getEditText(R.id.txt_description);
            edtDescription.setText(this.task.description);
		}
		txtCommit.setText(commitText);
		txtCommit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(isEdit){
					saveTask();
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
		JSONObject param = MU.buildJsonObj(Arrays.asList("name", txtName.getText().toString(), "description", txtDescription.getText().toString(), "date", targetDate.toString()));
		// postApi(Const.ADD_TASK, param);
		onApiResponse(Const.ADD_TASK, new JSONObject());
	}

	private void saveTask(){
		TextView txtName = getTextView(R.id.txt_name);
		TextView txtDescription = getTextView(R.id.txt_description);
		JSONObject param = MU.buildJsonObj(Arrays.asList("name", edtName.getText().toString(), "description", edtDescription.getText().toString(), "date", targetDate.toString()));
		// postApi(Const.SAVE_TASK, param);
		onApiResponse(Const.EDIT_TASK, new JSONObject());
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
		// todo: back and reload task list
        switch (url) {
            case Const.ADD_TASK:
                backToTaskList();
                break;
            case Const.EDIT_TASK:
                gotoTaskDetail();
                break;
        }
	}

    private void backToTaskList(){
        activity.backOneFragment();
    }

    public void gotoTaskDetail(){
		TaskDetailFragment frg = new TaskDetailFragment();
        this.task.name = edtName.getText().toString();
        this.task.description = edtDescription.getText().toString();
		backToTaskList();
	}

	public void setTargetDate(Date targetDate){
		this.targetDate = targetDate;
	}

	public Date getTargetDate(){
		return this.targetDate;
	}

	public void setEdit(boolean isEdit, Task task){
		this.isEdit = isEdit;
		this.task = task;
	}
}
