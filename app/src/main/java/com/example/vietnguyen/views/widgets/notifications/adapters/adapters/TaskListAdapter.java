package com.example.vietnguyen.views.widgets.notifications.adapters.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.MyArrayAdapter;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

/**
 * Created by viet on 8/13/2015.
 */
public class TaskListAdapter extends MyArrayAdapter<Task> {

	public TaskListAdapter(Context context, int resourceId){
		super(context, resourceId);
	}

	@Override
	protected void buildItemLayout(View convertView, Task task) {
		if(task.status == Task.STATUS_FINISHED){
			ImageView imgStatus = (ImageView)convertView.findViewById(R.id.img_icon);
			imgStatus.setImageResource((R.drawable.ico_checked));
		}
		TextView txt1 = (TextView)convertView.findViewById(R.id.txt1);
		txt1.setText(task.priority + "." + task.name);
		TextView txt2 = (TextView)convertView.findViewById(R.id.txt2);
		txt2.setText(task.description);
		if(this.mode == MyArrayAdapter.MODE_FILTER){
			TextView txtDate = (TextView)convertView.findViewById(R.id.txt_item_task_search_result_date);
			txtDate.setText(MU.getDateForDisplaying(task.date));
			txtDate.setVisibility(View.VISIBLE);
		}
	}
}
