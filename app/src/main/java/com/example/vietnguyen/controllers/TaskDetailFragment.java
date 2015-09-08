package com.example.vietnguyen.controllers;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

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
				markTaskDone();
			}
		});

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

	private void sendDeleteTask(){
		JSONObject params = MU.buildJsonObj(Arrays.<String>asList("taskId", String.valueOf(this.task.id)));
		// activity.postApi(Const.DELETE_TASK, params, this);
		onApiResponse(Const.DELETE_TASK, new JSONObject());
	}

	private void gotoEdit(){
		TaskAddFragment fragment = new TaskAddFragment();
		fragment.setEdit(true, this.task);
		activity.addFragment(fragment);
	}

	private void markTaskDone(){
		JSONObject params = MU.buildJsonObj(Arrays.asList("taskId", String.valueOf(this.task.id), "status", String.valueOf(Task.STATUS_FINISHED)));
		// activity.postApi(Const.UPDATE_STATUS_TASK, params, this);
		onApiResponse(Const.UPDATE_STATUS_TASK, new JSONObject());
	}

	private void backToTaskList(){
		activity.backOneFragment();
	}

	@Override
	public void onApiResponse(String url, JSONObject response){
		switch(url){
		case Const.UPDATE_STATUS_TASK:
			backToTaskList();
			break;
		case Const.DELETE_TASK:
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
