package com.example.vietnguyen.controllers.Task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vietnguyen.Const;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

import org.json.JSONObject;

import java.util.Arrays;
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
	protected void buildLayout(){
		super.buildLayout();
		buildEditText();
	}

	@Override
	protected void prepareTask(){
		super.prepareTask();
		task = (Task)getUpdatedData(TaskDetailFragment.BUNDLE_KEY_TASK, new Task());
	}

	protected void buildHeaderText(){
		setOnClickFor(R.id.txt_fragment_task_edit_done, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
//				updateTask();
				MU.hideSofeKeyboard(activity);
				buildTaskFromLayout();
				task.save();
				makeAlarmForNotice();
				backToTaskList();
			}
		});
		addTextWatcher(getTextView(R.id.txt_fragment_task_edit_done), Arrays.asList((View)getEditText(R.id.edt_share_task_edit_name), getEditText(R.id.edt_share_task_edit_description)));
	}

	@Override
	protected void onClickBackBtn(){
		activity.backOneFragment();
	}

//	private void updateTask(){
//		buildTaskFromLayout();
//		JSONObject param = MU.buildJsonObj(Arrays.asList("task", task.toString()));
//		postApi(Const.EDIT_TASK, param, new Api.OnCallApiListener() {
//
//			@Override
//			public void onApiResponse(JSONObject response){
//				onUpdateTaskSuccess();
//			}
//
//			@Override
//			public void onApiError(String errorMsg){
//				onUpdateTaskError();
//			}
//		});
//	}
//
//	private void onUpdateTaskSuccess(){
//		showShortToast("Save task to server success");
//		task.isRemoteSaved = true;
//		task.save();
//		makeAlarmForNotice();
//		backToTaskList();
//	}
//
//	private void onUpdateTaskError(){
//		showShortToast("Save to server failed");
//		task.isRemoteSaved = false;
//		task.save();
//		makeAlarmForNotice();
//		backToTaskList();
//	}

}
