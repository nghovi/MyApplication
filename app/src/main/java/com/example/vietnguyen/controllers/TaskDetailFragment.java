package com.example.vietnguyen.controllers;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

import java.util.Arrays;
import java.util.Date;

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
		TextView txtDone = getTextView(R.id.txt_edit);
		txtDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				gotoEdit();
			}
		});
		ImageView imgBack = getImageView(R.id.img_back);
		imgBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				backToTaskList();
			}
		});

		ImageView iconDelete = getImageView(R.id.img_icon_delete);
		iconDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onDeleteIconClicked();
			}
		});

		ImageView iconDone = getImageView(R.id.img_icon_done);
		iconDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onCheckIconClicked();
			}
		});

		TextView txtDate = getTextView(R.id.txt_date);
		Date taskTime = task.date;
		txtDate.setText(taskTime.toString());

		TextView txtName = getTextView(R.id.txt_name);
		txtName.setText(this.task.name);
		TextView txtDescription = getTextView(R.id.txt_description);
		txtDescription.setText(this.task.description);
	}

	private void onDeleteIconClicked(){

		dlgBuilder.build2OptsDlgTopDown(getString(R.string.cancel), getString(R.string.delete), null, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				sendDeleteTask();
			}
		}).show();
	}

	private void onCheckIconClicked(){
		int newStatus = Task.STATUS_UNFINISHED;
		String option = "Mark as unfinished";
		if (this.task.status == Task.STATUS_UNFINISHED) {
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

	private void sendUpdateTaskStatus(int status) {
		this.task.setStatus(status);
		this.task.save();
		Toast.makeText(activity, "Success to update task status locally", Toast.LENGTH_SHORT).show();
		JSONObject params = MU.buildJsonObj(Arrays.asList("taskId", String.valueOf(this.task.getId()), "status", String.valueOf(Task.STATUS_FINISHED)));
		activity.postApi(Const.UPDATE_STATUS_TASK, params, this);
	}

	private void sendDeleteTask(){
		this.task.delete();
		Toast.makeText(activity, "Delete task from local", Toast.LENGTH_SHORT).show();
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("id", String.valueOf(this.task.getId())));
		postApi(Const.DELETE_TASK, params);
	}

	private void gotoEdit(){
		TaskAddFragment fragment = new TaskAddFragment();
		fragment.setEdit(true, this.task);
		activity.addFragment(fragment);
	}

	private void backToTaskList(){
		activity.backToFragment(TaskListFragment.class);
	}

	@Override
	public void onApiError(String url, String errorMsg){
		switch(url){
		case Const.DELETE_TASK:
			Toast.makeText(activity, "Failed to delete task to server", Toast.LENGTH_SHORT).show();
			backToTaskList();
			break;
		case Const.UPDATE_STATUS_TASK:
			Toast.makeText(activity, "Failed to update task status to server", Toast.LENGTH_SHORT).show();
			backToTaskList();
			break;
		}
	}

	@Override
	public void onApiResponse(String url, JSONObject response){
		switch(url){
		case Const.UPDATE_STATUS_TASK:
			Toast.makeText(activity, "Success to update task status to server", Toast.LENGTH_SHORT).show();
			backToTaskList();
			break;
		case Const.DELETE_TASK:
			Toast.makeText(activity, "Suscess to delete task to server", Toast.LENGTH_SHORT).show();
			backToTaskList();
			break;
		default:
			break;
		}
	}

	public void setTask(Task task){
		this.task = task;
	}
}
