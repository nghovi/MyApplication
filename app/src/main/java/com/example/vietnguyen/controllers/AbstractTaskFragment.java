package com.example.vietnguyen.controllers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.DatePickerFragment;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AbstractTaskFragment extends MyFragment{

	protected Date	targetDate;
	protected Task	task;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		targetDate = new Date();
	}


	@Override
	protected void buildLayout(){
		super.buildLayout();
		prepareTask();
		buildHeaderText();
		buildCalendarPicker();
		builPriority();
		buildStatus();
		buildEditText();
	}

	protected void builPriority() {
		final TextView txtPriority = getTextView(R.id.txt_share_task_edit_priority);
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
			public void onClick(View view) {
				dlgBuilder.buildDialogNumberPicker(activity, "Please choose priority", Task.TASK_PRIORITIES, listener, Arrays.asList(Task.TASK_PRIORITIES).indexOf(String.valueOf(task.priority))).show();
			}
		});
	}

	protected void buildStatus() {
		setTextStatus(task.status);
		setOnClickFor(R.id.txt_share_task_edit_status, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				showTaskStatusChoosingDialog();
			}
		});
	}

	protected void buildEditText() {
		EditText edtName = getEditText(R.id.edt_share_task_edit_name);
		EditText edtDescription = getEditText(R.id.edt_share_task_edit_description);
		EditText edtComment = getEditText(R.id.edt_share_task_edit_comment);
		edtName.setText(this.task.name);
		edtDescription.setText(this.task.description);
		edtComment.setText(this.task.comment);
	}

	protected void prepareTask() {
	}

	protected void buildHeaderText() {
	}

	@Override
	protected void onClickBackBtn(){
		backToTaskList();
	}

	protected void showTaskStatusChoosingDialog(){
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
		if(status == Task.STATUS_FINISHED){
			setTextFor(R.id.txt_share_task_edit_status, "Done");
		}else{
			setTextFor(R.id.txt_share_task_edit_status, "Not Done");
		}
	}

	protected void buildTaskFromLayout(){
		EditText edtName = getEditText(R.id.edt_share_task_edit_name);
		EditText edtDescription = getEditText(R.id.edt_share_task_edit_description);
		EditText edtComment = getEditText(R.id.edt_share_task_edit_comment);
		TextView txtPriority = getTextView(R.id.txt_share_task_edit_priority);

		task.name = edtName.getText().toString();
		task.description = edtDescription.getText().toString();
		task.comment = edtComment.getText().toString();
		task.priority = Integer.parseInt(txtPriority.getText().toString());
		task.date = targetDate;
		task.lastupdated = targetDate;
	}

	protected void buildCalendarPicker(){
		final TextView txtDate = getTextView(R.id.txt_share_task_edit_date);
		txtDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				DatePickerFragment datePicker = new DatePickerFragment();
				datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
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

	protected void backToTaskList(){
		activity.backToFragment(TaskListFragment.class, TaskListFragment.KEY_TARGET_DATE, targetDate);
	}
}
