package com.example.vietnguyen.controllers.Task;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vietnguyen.Const;
import com.example.vietnguyen.core.controller.MyFragment;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Notice;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.NoticeAdapter;

import java.util.ArrayList;
import java.util.Arrays;

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
		buildNotices();
	}

	private void builHeaderFunctions(){
		setOnClickFor(R.id.txt_fragment_task_detail_edit, new View.OnClickListener() {

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
		setTextFor(R.id.txt_fragment_task_detail_comment, task.comment);
		setTextFor(R.id.txt_fragment_task_detail_priority, String.valueOf(task.priority));
		setTextFor(R.id.txt_fragment_task_detail_status, Task.STATUS[task.status]);
	}

	private void buildNotices(){
		ArrayList<Notice> notices = Notice.getOnGoingNoticesForTask(task);
		ListView lstNotice = getListView(R.id.lst_fragment_task_detail_remind);
		NoticeAdapter adapter = new NoticeAdapter(activity, notices, false, null);
		lstNotice.setAdapter(adapter);
	}

	// @Override
	// protected void onClickBackBtn(){
	// //backToTaskList();
	// }

	private void onDeleteIconClicked(){

		dlgBuilder.build2OptsDlgTopDown(getString(R.string.cancel), getString(R.string.delete), null, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				// sendDeleteTask();
				task.delete();
				onClickBackBtn();
			}
		}).show();
	}

	private void onCheckIconClicked(){
		int newStatus = Task.STATUS_UNFINISHED;
		String option = "Mark as unfinished";
		if(this.task.status == Task.STATUS_UNFINISHED){
			newStatus = Task.STATUS_FINISHED;
			option = "Mark as finished";
		}
		final int finalNewStatus = newStatus;
		dlgBuilder.build2OptsDlgTopDown(getString(R.string.cancel), option, null, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				sendUpdateTaskStatus(finalNewStatus);
			}
		}).show();
	}

	private void sendUpdateTaskStatus(int status){
		this.task.setStatus(status);
		setTextFor(R.id.txt_fragment_task_detail_status, Task.STATUS[task.status]);
		task.isRemoteSaved = false;
		task.save();
		// onClickBackBtn();

		// JSONObject param = MU.buildJsonObj(Arrays.asList("task", task.toString()));
		// postApi(Const.EDIT_TASK, param, new Api.OnCallApiListener() {
		//
		// @Override
		// public void onApiResponse(JSONObject response){
		// Toast.makeText(activity, "Success to update task status to server", Toast.LENGTH_SHORT).show();
		// task.isRemoteSaved = true;
		// task.save();
		// onClickBackBtn();
		// // backToTaskList();
		// }
		//
		// @Override
		// public void onApiError(String errorMsg){
		// Toast.makeText(activity, "Failed to update task status to server", Toast.LENGTH_SHORT).show();
		// task.isRemoteSaved = false;
		// task.save();
		// onClickBackBtn();
		// // backToTaskList();
		// }
		// });
	}

	// private void sendDeleteTask(){
	// JSONObject params = MU.buildJsonObj(Arrays.<String>asList("id", task.id));
	// postApi(Const.DELETE_TASK, params, new Api.OnCallApiListener() {
	//
	// @Override
	// public void onApiResponse(JSONObject response){
	// Toast.makeText(activity, "Suscess to delete task to server", Toast.LENGTH_SHORT).show();
	// task.delete();
	// onClickBackBtn();
	// // backToTaskList();
	// }
	//
	// @Override
	// public void onApiError(String errorMsg){
	// Toast.makeText(activity, "Failed to delete task to server", Toast.LENGTH_SHORT).show();
	// task.isDeleted = true;
	// task.isRemoteSaved = false;
	// task.save();
	// onClickBackBtn();
	// // backToTaskList();
	// }
	// });
	// Toast.makeText(activity, "Deleted task from local", Toast.LENGTH_SHORT).show();
	// }

	private void gotoEdit(){
		TaskEditFragment fragment = new TaskEditFragment();
		activity.addFragment(fragment, BUNDLE_KEY_TASK, task);
	}

	private void backToTaskList(){
		activity.backToFragment(TaskListFragment.class, TaskListFragment.KEY_TARGET_DATE, task.date);
	}
}
