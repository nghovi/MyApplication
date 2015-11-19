package com.example.vietnguyen.controllers;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.DatePickerFragment;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

public class TaskSearchFragment extends MyFragment{

	private List<Task>	tasks;
	private String		priority = Task.TASK_PRIORITIES_WITH_ANY[0];
	private int			taskStatus	= -1;
	private Date		targetDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task_search, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		setOnClickFor(R.id.txt_fragment_task_search_search, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickSearchText();
			}
		});

		buildPriority();
		buildStatus();
		buildCalendarPicker();
	}

	private void buildPriority(){
		final LinearLayout lnrPriority = getLinearLayout(R.id.lnr_fragment_task_search_priority);
		final DialogBuilder.OnNumberPickerBtnOkClickListener listener = new DialogBuilder.OnNumberPickerBtnOkClickListener() {

			@Override
			public void onClick(int selectedValue, String displayedValue){
				priority = displayedValue;
				setTextFor(R.id.txt_fragment_task_search_priority, String.valueOf(displayedValue));
			}
		};
		lnrPriority.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlgBuilder.buildDialogNumberPicker(activity, "Please choose priority", Task.TASK_PRIORITIES_WITH_ANY, listener, 0).show();
			}
		});
	}

	private void buildStatus(){
		setOnClickFor(R.id.lnr_fragment_task_search_status, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlgBuilder.build3OptsDlgTopDown("Any", "Finished", "Unfinished", new DialogBuilder.OnNumberPickerBtnOkClickListener() {

					@Override
					public void onClick(int selectedValue, String displayedValue){
						setTextFor(R.id.txt_fragment_task_search_status, displayedValue);
						taskStatus = selectedValue;
					}
				}).show();
			}
		});
	}

	private void buildCalendarPicker(){
		final TextView txtDate = getTextView(R.id.txt_fragment_task_search_date);
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

	private void onClickSearchText(){
		String text = getEditText(R.id.edt_fragment_task_search_text).getText().toString();

		tasks = new Select().from(Task.class).execute();
		Iterator<Task> ib = tasks.iterator();
		while(ib.hasNext()){
			Task task = ib.next();
			if(!MU.isEmpty(text) && !MU.checkMatch(task.name, text) && !MU.checkMatch(task.description, text) && !MU.checkMatch(task.comment, text)){
				ib.remove();
			}

			// taskStatus = 1 mean user selected "Any" at 3 options dialog
			if(taskStatus != -1 && taskStatus != 1 && taskStatus != task.status){
				ib.remove();
			}
			if(targetDate != null && !MU.isSameDay(targetDate, task.date)){
				ib.remove();
			}
			if(!Task.TASK_PRIORITIES_WITH_ANY[0].equals(priority) && !priority.equals(String.valueOf(task.priority))){
				ib.remove();
			}
		}

		activity.addFragment(new TaskSearchResultFragment(), TaskSearchResultFragment.KEY_TASK_SEARCH_RESULT, tasks);
	}

}
