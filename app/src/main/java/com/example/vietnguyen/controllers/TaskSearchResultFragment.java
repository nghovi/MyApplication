package com.example.vietnguyen.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.TaskAdapter;

public class TaskSearchResultFragment extends MyFragment{

	public static final String	KEY_TASK_SEARCH_RESULT	= "KEY_TASK_SEARCH_RESULT";

	private List<Task>			tasks;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task_search_result, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();

		tasks = (List<Task>)getUpdatedData(KEY_TASK_SEARCH_RESULT, new ArrayList<Task>());
		TaskAdapter adapter = new TaskAdapter(activity, R.layout.item_task, new ArrayList<Task>(tasks));
		ListView lstTask = getListView(R.id.lst_fragment_task_search_result_task);
		lstTask.setAdapter(adapter);
		lstTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				Task task = (Task)adapterView.getItemAtPosition(i);
				gotoTaskDetail(task);
			}
		});
	}

	public void gotoTaskDetail(Task task){
		TaskDetailFragment frg = new TaskDetailFragment();
		frg.setTask(task);
		activity.addFragment(frg);
	}
}
