package com.nguyenhoangviet.vietnguyen.controllers.Task;

import java.util.Arrays;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.network.UrlBuilder;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Task;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import org.json.JSONObject;

public class TaskAddFragment extends AbstractTaskFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task_add, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		goneView(R.id.lnr_share_task_edit_status);
		getMainActivity().footer.hide();
	}

	@Override
	String[] getPriorities(){
		return Task.TASK_PRIORITIES_REAL;
	}

	@Override
	protected void prepareTask(){
		task = new Task();
		task.date = targetDate;
		task.lastupdated = targetDate;
		task.priority = Task.TASK_PRIORITY_HIGH;
	}

	protected void buildHeaderText(){
		TextView txtCommit = getTextView(R.id.txt_fragment_task_add_add);
		txtCommit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				addNewTask();
			}
		});
		addTextWatcher(txtCommit, Arrays.asList((View)getEditText(R.id.edt_share_task_edit_name), getEditText(R.id.edt_share_task_edit_description)));
	}

	private void addNewTask(){
		hideSofeKeyboard();
		buildTaskFromLayout();
		callApiAddNewTask();
	}

	private void callApiAddNewTask(){
		callApi(UrlBuilder.addTask(task), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onAddNewTaskSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});

	}

	private void onAddNewTaskSuccess(JSONObject response){
		backToTaskList();
	}

}
