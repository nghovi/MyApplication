package com.nguyenhoangviet.vietnguyen.controllers.Task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Task;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import org.json.JSONObject;

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
		getMainActivity().footer.hide();
	}

	@Override
	String[] getPriorities(){
		return Task.TASK_PRIORITIES;
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
		hideSofeKeyboard();
		buildTaskFromLayout();
		sendEditTask();
	}

	private void sendEditTask(){

		callPostApi(Const.EDIT_TASK, getJsonBuilder().add("id", task.id).add("name", task.name).add("description", task.description).add("date", MU.getDateString(task.date, Const.APP_DATE_FORMAT)).add("priority", task.priority).add("status", task.status).getJsonObj(), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSendEditTaskSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});

	}

	private void onSendEditTaskSuccess(JSONObject response){
		this.task = MU.convertToModel(response.optString("data"), Task.class);
		activity.backToFragment(TaskDetailFragment.class, TaskDetailFragment.BUNDLE_KEY_TASK, task);
	}

	@Override
	protected void onClickBackBtn(){
		if(!this.hasChangeData()){
			activity.backToFragment(TaskDetailFragment.class, TaskDetailFragment.BUNDLE_KEY_TASK, task);
		}else{
			dlgBuilder.build2OptsDlgTopDown(getString(R.string.discard), getString(R.string.save), new View.OnClickListener() {

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
		return !MU.isEmpty(task.name) && task.status != status || task.priority != priority || !getTextView(R.id.edt_share_task_edit_name).getText().toString().equals(task.name) || !getTextView(R.id.edt_share_task_edit_description).getText().toString().equals(task.description);
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
