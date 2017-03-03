package com.nguyenhoangviet.vpcorp.views.widgets.notifications.adapters.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.nguyenhoangviet.vpcorp.core.utils.MU;
import com.nguyenhoangviet.vpcorp.core.views.adapters.MyArrayAdapter;
import com.nguyenhoangviet.vpcorp.models.Task;
import com.nguyenhoangviet.vpcorp.myapplication.R;

/**
 * Created by viet on 8/13/2015.
 */
public class TaskListAdapter extends MyArrayAdapter<Task>{

	public TaskListAdapter(Context context, int resourceId){
		super(context, resourceId, new ArrayList<Task>());
	}

	@Override
	protected void buildItemLayout(View convertView, Task task){
		TextView txtTaskName = (TextView)convertView.findViewById(R.id.txt_item_task_name);
		txtTaskName.setText(task.name);
		if(task.status == Task.STATUS_UNFINISHED){
			txtTaskName.setTextColor(Color.BLACK);
			txtTaskName.setTypeface(null, Typeface.BOLD);
		}

		TextView txtDescription = (TextView)convertView.findViewById(R.id.txt_item_task_description);
		txtDescription.setText(task.description);
		// TextView txtStatus = (TextView)convertView.findViewById(R.id.txt_item_task_status);
		// setTaskStatusBackground(txtStatus, task);
		//
		// TextView txtPriority = (TextView)convertView.findViewById(R.id.txt_item_task_priority);
		// setTaskPriorityBackground(txtPriority, task);

		if(this.mode == MyArrayAdapter.MODE_FILTER){
			TextView txtDate = (TextView)convertView.findViewById(R.id.txt_item_task_search_result_date);
			txtDate.setText(MU.getDateForDisplaying(task.date));
			txtDate.setVisibility(View.VISIBLE);
		}
	}

	public static void setTaskStatusBackground(TextView txtStatus, Task task){
		if(task.status == Task.STATUS_FINISHED){
			txtStatus.setText(Task.STATUS[1]);
			txtStatus.setBackgroundResource(R.drawable.task_status_finished_bg);
		}else{
			txtStatus.setText(Task.STATUS[0]);
			txtStatus.setBackgroundResource(R.drawable.task_status_unfinished_bg);
		}
	}

	public static void setTaskPriorityBackground(TextView txtPriority, Task task){
		int bgResource = R.drawable.task_priority_lowest;
		switch(task.priority){
		case 1:
			bgResource = R.drawable.task_priority_highest;
			break;
		case 2:
			bgResource = R.drawable.task_priority_high;
			break;
		case 3:
			bgResource = R.drawable.task_priority_medium;
			break;
		case 4:
			bgResource = R.drawable.task_priority_low;
			break;
		default:
			break;
		}
		txtPriority.setText(Task.TASK_PRIORITIES[task.priority - 1]);
		txtPriority.setBackgroundResource(bgResource);
	}
}
