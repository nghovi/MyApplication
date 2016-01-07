package com.nguyenhoangviet.vietnguyen.controllers.Task;

import java.util.Arrays;
import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Task;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

public class TaskAddFragment extends AbstractTaskFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		targetDate = new Date();
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
	protected void prepareTask(){
		task = new Task();
		targetDate = new Date();
		task.date = targetDate;
		task.lastupdated = targetDate;
		task.priority = Task.TASK_PRIORITY_HIGHEST;
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
		backToTaskList();
		// Try to save to server
		// JSONObject param = MU.buildJsonObj(Arrays.asList("task", task.toString()));
		// postApi(Const.ADD_TASK, param, new Api.OnCallApiListener() {
		//
		// @Override
		// public void onApiResponse(JSONObject response) {
		// showShortToast("Save new task to server success");
		// task.id = response.optString("data");
		// task.isRemoteSaved = true;
		// task.save();
		// savedNotices = new ArrayList<Notice>();
		// backToTaskList();
		// }
		//
		// @Override
		// public void onApiError(String errorMsg) {
		// showShortToast("Save new task to server failed");
		// deleteUnUsedNotices();
		// task.save();
		// backToTaskList();
		// }
		// });
	}

}
