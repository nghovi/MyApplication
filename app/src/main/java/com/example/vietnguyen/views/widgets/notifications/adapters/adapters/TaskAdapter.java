package com.example.vietnguyen.views.widgets.notifications.adapters.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

/**
 * Created by viet on 8/13/2015.
 */
public class TaskAdapter extends ArrayAdapter<Task>{

	private Context					context;
	private ArrayList<Task>			data;
	private static LayoutInflater	inflater	= null;
	private int						resourceId;

	public TaskAdapter(Context context, int resourceId, ArrayList<Task> data){
		super(context, resourceId, data);
		this.context = context;
		this.data = data;
		this.resourceId = resourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(resourceId, null);
		Task task = data.get(position);
        if (task.status == Task.STATUS_FINISHED) {
            ImageView imgStatus = (ImageView)convertView.findViewById(R.id.img_icon);
            imgStatus.setImageResource((R.drawable.ico_checked));
        }
		TextView txt1 = (TextView)convertView.findViewById(R.id.txt1);
		txt1.setText(task.priority + "." + task.name);
		TextView txt2 = (TextView)convertView.findViewById(R.id.txt2);
        txt2.setText(task.description);
		return convertView;
	}
}
