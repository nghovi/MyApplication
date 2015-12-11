package com.example.vietnguyen.controllers.Task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vietnguyen.Const;
import com.example.vietnguyen.controllers.Task.TaskDetailFragment;
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
			public void onClick(View view){
				saveTask();
			}
		});
	}

	private void saveTask(){
		// updateTask();
		MU.hideSofeKeyboard(activity);
		buildTaskFromLayout();
		makeAlarmForNotice();
		activity.backToFragment(TaskDetailFragment.class, TaskDetailFragment.BUNDLE_KEY_TASK, task);
	}

	@Override
	protected void onClickBackBtn(){
		if(!this.hasChangeData()){
			activity.backToFragment(TaskDetailFragment.class, TaskDetailFragment.BUNDLE_KEY_TASK, task);
		}else{
			dlgBuilder.build2OptsDlgTopDown("Discard", "Save changes", new View.OnClickListener() {

				@Override
				public void onClick(View view){
					activity.backToFragment(TaskDetailFragment.class, TaskDetailFragment.BUNDLE_KEY_TASK, task);
				}
			}, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					saveTask();
				}
			}).show();
		}
	}

	protected boolean hasChangeData(){
		return task.status != status || task.priority != priority || !task.name.equals(getTextView(R.id.edt_share_task_edit_name).getText().toString()) || !task.description.equals(getTextView(R.id.edt_share_task_edit_description).getText().toString()) || !task.comment.equals(getTextView(R.id.edt_share_task_edit_comment).getText().toString());
	}
	// private void updateTask(){
	// buildTaskFromLayout();
	// JSONObject param = MU.buildJsonObj(Arrays.asList("task", task.toString()));
	// postApi(Const.EDIT_TASK, param, new Api.OnCallApiListener() {
	//
	// @Override
	// public void onApiResponse(JSONObject response){
	// onUpdateTaskSuccess();
	// }
	//
	// @Override
	// public void onApiError(String errorMsg){
	// onUpdateTaskError();
	// }
	// });
	// }
	//
	// private void onUpdateTaskSuccess(){
	// showShortToast("Save task to server success");
	// task.isRemoteSaved = true;
	// task.save();
	// makeAlarmForNotice();
	// backToTaskList();
	// }
	//
	// private void onUpdateTaskError(){
	// showShortToast("Save to server failed");
	// task.isRemoteSaved = false;
	// task.save();
	// makeAlarmForNotice();
	// backToTaskList();
	// }

}
