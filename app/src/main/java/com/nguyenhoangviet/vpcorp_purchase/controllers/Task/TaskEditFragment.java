package com.nguyenhoangviet.vpcorp_purchase.controllers.Task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenhoangviet.vpcorp_purchase.core.utils.MU;
import com.nguyenhoangviet.vpcorp_purchase.models.Task;
import com.nguyenhoangviet.vpcorp_purchase.myapplication.R;

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
		hideSofeKeyboard();
		buildTaskFromLayout();
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
		return !MU.isEmpty(task.name) && task.status != status || task.priority != priority || !getTextView(R.id.edt_share_task_edit_name).getText().toString().equals(task.name) || !getTextView(R.id.edt_share_task_edit_description).getText().toString().equals(task.description) || !getTextView(R.id.edt_share_task_edit_comment).getText().toString().equals(task.comment);
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
