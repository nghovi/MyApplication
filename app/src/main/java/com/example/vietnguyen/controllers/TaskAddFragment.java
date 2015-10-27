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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.DatePickerFragment;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

public class TaskAddFragment extends MyFragment{

	private Date	targetDate;
	private boolean	isEdit;
	private Task	task;

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
		LinearLayout lnrTaskStatus = getLinearLayout(R.id.lnr_task_status);

		if(isEdit){
			commitText = "Done";
			TextView txtDate = getTextView(R.id.txt_date);
			targetDate = task.date;
			txtDate.setText(MU.getDateForDisplaying(targetDate));
		}else{
			task = new Task();
			targetDate = new Date();
			task.date = targetDate;
			task.lastupdated = targetDate;
			task.priority = Integer.parseInt(Task.TASK_PRIORITIES[0]);
			lnrTaskStatus.setVisibility(View.GONE);
			txtCommit.setVisibility(View.INVISIBLE);
		}

		EditText edtName = getEditText(R.id.txt_name);
		EditText edtDescription = getEditText(R.id.txt_description);
		EditText edtComment = getEditText(R.id.txt_comment);
		MU.addTextWatcher(txtCommit, Arrays.asList((View)edtName, edtDescription));
		edtName.setText(this.task.name);
		edtDescription.setText(this.task.description);
		edtComment.setText(this.task.comment);
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

		final TextView txtPriority = getTextView(R.id.txt_priority);
		txtPriority.setText(String.valueOf(task.priority));
		final DialogBuilder.OnNumberPickerBtnOkClickListener listener = new DialogBuilder.OnNumberPickerBtnOkClickListener() {

			@Override
			public void onClick(int selectedValue, String displayedValue){
				task.priority = Integer.valueOf(displayedValue);
				txtPriority.setText(String.valueOf(displayedValue));
			}
		};
		txtPriority.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlgBuilder.buildDialogNumberPicker(activity, "Please choose priority", Task.TASK_PRIORITIES, listener, Arrays.asList(Task.TASK_PRIORITIES).indexOf(String.valueOf(task.priority))).show();
			}
		});

		TextView txtTaskStatus = getTextView(R.id.txt_task_status);
		setTextStatus(task.status);
		setOnClickFor(R.id.txt_task_status, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				showTaskStatusChoosingDialog();
			}
		});
	}

	private void showTaskStatusChoosingDialog(){
		String option1 = "Mark as unfinished";
		String option2 = "Mark as finished";
		dlgBuilder.build2OptsDlgTopDown(option1, option2, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				task.setStatus(Task.STATUS_UNFINISHED);
				setTextStatus(Task.STATUS_UNFINISHED);
			}
		}, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				task.setStatus(Task.STATUS_FINISHED);
				setTextStatus(Task.STATUS_FINISHED);
			}
		}).show();
	}

	private void setTextStatus(int status){
		TextView txtTaskStatus = getTextView(R.id.txt_task_status);
		if(status == Task.STATUS_FINISHED){
			txtTaskStatus.setText("Done");
		}else{
			txtTaskStatus.setText("Not Done");
		}
	}

	private void addNewTask(){
		buildTaskFromLayout();
		task.save();
		Toast.makeText(activity, "Save to local", Toast.LENGTH_SHORT).show();
		JSONObject param = MU.buildJsonObj(Arrays.asList("task", task.toString()));
		postApi(Const.ADD_TASK, param);
	}

	private void buildTaskFromLayout(){
		EditText edtName = getEditText(R.id.txt_name);
		EditText edtDescription = getEditText(R.id.txt_description);
		EditText edtComment = getEditText(R.id.txt_comment);
		TextView txtPriority = getTextView(R.id.txt_priority);

		task.name = edtName.getText().toString();
		task.description = edtDescription.getText().toString();
		task.comment = edtComment.getText().toString();
		task.priority = Integer.parseInt(txtPriority.getText().toString());
		task.date = targetDate;
		task.lastupdated = targetDate;
	}

	private void updateTask(){
		buildTaskFromLayout();
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
						Calendar c = Calendar.getInstance();
						c.set(i, i2, i3);
						targetDate = c.getTime();
						txtDate.setText(MU.getDateForDisplaying(targetDate));
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
