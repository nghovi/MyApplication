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
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.DatePickerFragment;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

public class TaskAddFragment extends AbstractTaskFragment{

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
		goneView(R.id.lnr_share_task_edit_status);
	}

	@Override
	protected void prepareTask() {
		task = new Task();
		targetDate = new Date();
		task.date = targetDate;
		task.lastupdated = targetDate;
		task.priority = Integer.parseInt(Task.TASK_PRIORITIES[0]);
	}

	protected void buildHeaderText() {
		TextView txtCommit = getTextView(R.id.txt_fragment_task_add_add);
		txtCommit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addNewTask();
			}
		});
		addTextWatcher(txtCommit, Arrays.asList((View) getEditText(R.id.edt_share_task_edit_name), getEditText(R.id.edt_share_task_edit_description)));
	}

	private void addNewTask(){
		buildTaskFromLayout();
		JSONObject param = MU.buildJsonObj(Arrays.asList("task", task.toString()));
		postApi(Const.ADD_TASK, param, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response) {
				showShortToast("Save new task to server success");
				backToTaskList();
			}

			@Override
			public void onApiError(String errorMsg) {
				showShortToast("Save new task to server failed");
				backToTaskList();
			}
		});
	}

}
