package com.nguyenhoangviet.vietnguyen.controllers.Task;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragment;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.network.UrlBuilder;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Task;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import org.json.JSONObject;

public class TaskDetailFragment extends MyFragment{

	private Task				task;
	public static final String	BUNDLE_KEY_TASK	= "TASK";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task_detail, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		task = (Task)getUpdatedData(BUNDLE_KEY_TASK, new Task());
		builHeaderFunctions();
		buildTaskDetail();
		// buildNotices();
	}

	private void builHeaderFunctions(){
		setOnClickFor(R.id.img_fragment_task_detail_eit, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				gotoEdit();
			}
		});

		setOnClickFor(R.id.img_fragment_task_detail_delete, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onDeleteIconClicked();
			}
		});

		setOnClickFor(R.id.img_fragment_task_detail_done, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onCheckIconClicked();
			}
		});
	}

	private void buildTaskDetail(){
		setTextFor(R.id.txt_fragment_task_detail_date, MU.getDateForDisplaying(task.date));
		setTextFor(R.id.txt_fragment_task_detail_name, task.name);
		setTextFor(R.id.txt_fragment_task_detail_description, task.description);
		setTextFor(R.id.txt_fragment_task_detail_priority, Task.TASK_PRIORITIES_REAL[task.priority - 1]);
		setTextFor(R.id.txt_fragment_task_detail_status, Task.STATUS[task.status]);
		if(task.status == Task.STATUS_UNFINISHED){
			TextView txtTaskStatus = getTextView(R.id.txt_fragment_task_detail_status);
			txtTaskStatus.setTextColor(Color.BLACK);
		}
	}

	// private void buildNotices(){
	// ArrayList<Notice> notices = Notice.getOnGoingNoticesForTask(task);
	// buildVirtualListByLinearLayout(R.id.lst_fragment_task_detail_remind, R.layout.item_notice, notices, new VirtualItemLayoutBuilder() {
	//
	// @Override
	// public void buildItemLayout(View itemRoot, Object itemData){
	// TextView txtDate = (TextView)itemRoot.findViewById(R.id.txt_item_remind_time_date);
	// txtDate.setText(MU.getDateTimeForDisplaying(((Notice)itemData).noticeDate));
	// ImageView imgDeleteIcon = (ImageView)itemRoot.findViewById(R.id.img_item_remind_time_delete);
	// imgDeleteIcon.setVisibility(View.GONE);
	// }
	// });
	// }

	@Override
	protected void onClickBackBtn(){
		backToTaskList();
	}

	private void onDeleteIconClicked(){

		dlgBuilder.build2OptsDlgTopDown(getString(R.string.cancel), getString(R.string.delete), null, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				sendDeleteTask();
				onClickBackBtn();
			}
		}).show();
	}

	private void onCheckIconClicked(){
		int newStatus = Task.STATUS_UNFINISHED;
		String option = getString(R.string.fragment_task_detail_mark_as_unfinished);
		if(this.task.status == Task.STATUS_UNFINISHED){
			newStatus = Task.STATUS_FINISHED;
			option = getString(R.string.fragment_task_detail_mark_as_finished);
		}
		task.status = newStatus;
		dlgBuilder.build2OptsDlgTopDown(getString(R.string.cancel), option, null, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				sendEditTask();
			}
		}).show();
	}

	private void sendEditTask(){

		callApi(UrlBuilder.editTask(task), new Api.OnApiSuccessObserver() {

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
		this.task = MU.convertToModel(response.toString(), Task.class);
		setTextFor(R.id.txt_fragment_task_detail_status, Task.STATUS[task.status]);
	}

	private void sendDeleteTask(){
		callApi(UrlBuilder.deleteTask(task.id), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onDeleteTaskSuccess(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});

	}

	private void onDeleteTaskSuccess(JSONObject response){
		backToTaskList();
	}

	private void gotoEdit(){
		TaskEditFragment fragment = new TaskEditFragment();
		activity.addFragment(fragment, BUNDLE_KEY_TASK, task);
	}

	private void backToTaskList(){
		activity.backToFragment(TaskListFragment.class, TaskListFragment.KEY_TARGET_DATE, task.date);
	}
}
