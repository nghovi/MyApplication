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

public class TaskEditFragment extends AbstractTaskFragment{


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		targetDate = new Date();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task_edit, container, false);
	}


	@Override
	protected void prepareTask() {
		super.prepareTask();
		task = (Task) getUpdatedData(TaskDetailFragment.BUNDLE_KEY_TASK, new Task());
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildEditText();
	}

	protected void buildHeaderText() {
		TextView txtCommit = getTextView(R.id.txt_fragment_task_edit_done);
		txtCommit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateTask();
			}
		});
		addTextWatcher(txtCommit, Arrays.asList((View) getEditText(R.id.edt_share_task_edit_name), getEditText(R.id.edt_share_task_edit_description)));
	}

	@Override
	protected void onClickBackBtn(){
		activity.backOneFragment();
	}


	private void updateTask(){
		buildTaskFromLayout();
		JSONObject param = MU.buildJsonObj(Arrays.asList("task", task.toString()));
		postApi(Const.EDIT_TASK, param, new Api.OnCallApiListener() {

			@Override
			public void onApiResponse(JSONObject response) {
				showShortToast("Save task to server success");
				backToTaskList();
			}

			@Override
			public void onApiError(String errorMsg) {
				showShortToast("Save to server failed");
				backToTaskList();
			}
		});
	}


}
