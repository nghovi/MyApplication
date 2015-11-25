package com.example.vietnguyen.controllers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vietnguyen.core.controllers.DateTimePicker;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.DatePickerFragment;
import com.example.vietnguyen.models.Notice;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AbstractTaskFragment extends MyFragment{

	protected Date			targetDate;
	protected Task			task;
	protected List<Notice>	savedNotices	= new ArrayList<Notice>();

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
		buildAddReminds();
	}

	protected void builPriority(){
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

	protected void buildStatus(){
		setTextStatus(task.status);
		setOnClickFor(R.id.txt_share_task_edit_status, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				showTaskStatusChoosingDialog();
			}
		});
	}

	protected void buildEditText(){
		EditText edtName = getEditText(R.id.edt_share_task_edit_name);
		EditText edtDescription = getEditText(R.id.edt_share_task_edit_description);
		EditText edtComment = getEditText(R.id.edt_share_task_edit_comment);
		edtName.setText(this.task.name);
		edtDescription.setText(this.task.description);
		edtComment.setText(this.task.comment);
	}

	protected void prepareTask(){
	}

	protected void buildHeaderText(){
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

	protected void buildAddReminds(){
		setOnClickFor(R.id.img_share_task_edit_add_remind, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				onClickAddRemind();
			}
		});
	}

	private void onClickAddRemind(){
		final DateTimePicker dateTimePicker = new DateTimePicker(activity);
		dateTimePicker.setDateTimeListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				addRemind(dateTimePicker.getDateTime());
			}
		});
	}

	private void addRemind(Calendar c){
		Notice newNotice = new Notice(Notice.NOTICE_TYPE_TASK, "Task reminder", task.name, task.id, c.getTime());
		newNotice.save(); // todo can create redundant notices in database if user cancel adding task.
		savedNotices.add(newNotice);
		task.addNoticeIdWithoutSave(String.valueOf(newNotice.getId()));
	}

	protected void deleteUnUsedNotices(){
		for(Notice notice : savedNotices){
			notice.delete();
		}
	}


	protected static List<Task> searchWithConditions(Map<String, Object> conditions){
		String text = (String)conditions.get(TaskSearchFragment.KEY_TASK_SEARCH_TEXT);
		String priority = (String)conditions.get(TaskSearchFragment.KEY_TASK_SEARCH_PRIORITY);
		int taskStatus = (int)conditions.get(TaskSearchFragment.KEY_TASK_SEARCH_STATUS);

		List<Task> tasks = Task.getAllUndeleted(Task.class);
		Iterator<Task> ib = tasks.iterator();
		while(ib.hasNext()){
			Task task = ib.next();
			if(!MU.isEmpty(text) && !MU.checkMatch(task.name, text) && !MU.checkMatch(task.description, text) && !MU.checkMatch(task.comment, text)){
				ib.remove();
				continue;
			}

			// taskStatus = 1 mean user selected "Any" at 3 options dialog
			if(taskStatus != Task.STATUS_ANY && taskStatus != task.status){
				ib.remove();
				continue;
			}

			if(!Task.TASK_PRIORITIES_WITH_ANY[0].equals(priority) && !priority.equals(String.valueOf(task.priority))){
				ib.remove();
				continue;
			}
		}

		return tasks;
	}

	protected void backToTaskList(){
		activity.backToFragment(TaskListFragment.class, TaskListFragment.KEY_TARGET_DATE, targetDate);
	}
}
