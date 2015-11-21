package com.example.vietnguyen.views.widgets.notifications.adapters.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by viet on 8/13/2015.
 */
public class TaskRemindAdapter extends ArrayAdapter<Task>{

	private Context					context;
	private ArrayList<Task>			data;
	private static LayoutInflater	inflater	= null;
	private int						resourceId;
	private boolean					isFiltered	= false;
	private View.OnClickListener onDeleteListener;

	public TaskRemindAdapter(Context context, ArrayList<Task> data, boolean isFiltered, View.OnClickListener onDeleteListener){
		super(context,R.layout.item_remind_time, data);
		this.context = context;
		this.onDeleteListener = onDeleteListener;
		this.data = data;
		this.resourceId = resourceId;
		this.isFiltered = isFiltered;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.item_remind_time, null);
		TextView txtDate = (TextView)convertView.findViewById(R.id.txt_item_remind_time_date);
		ImageView imgDeleteIcon = (ImageView)convertView.findViewById(R.id.img_item_remind_time_delete);
		imgDeleteIcon.setOnClickListener(onDeleteListener);
		return convertView;
	}
}
