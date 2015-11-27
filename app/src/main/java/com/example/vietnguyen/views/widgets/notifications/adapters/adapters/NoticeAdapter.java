package com.example.vietnguyen.views.widgets.notifications.adapters.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Notice;
import com.example.vietnguyen.myapplication.R;

import java.util.ArrayList;

/**
 * Created by viet on 8/13/2015.
 */
public class NoticeAdapter extends ArrayAdapter<Notice>{

	private Context					context;
	public ArrayList<Notice>		data;
	private static LayoutInflater	inflater	= null;

	public NoticeAdapter(Context context, ArrayList<Notice> data){
		super(context, R.layout.item_notice, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final Notice notice = this.getItem(position);
		convertView = inflater.inflate(R.layout.item_notice, null);
		TextView txtDate = (TextView)convertView.findViewById(R.id.txt_item_remind_time_date);
		txtDate.setText(MU.getDateTimeForDisplaying(notice.noticeDate));
		ImageView imgDeleteIcon = (ImageView)convertView.findViewById(R.id.img_item_remind_time_delete);
		imgDeleteIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				deleteNotice(notice);
			}
		});
		return convertView;
	}

	public void deleteNotice(Notice notice){
		notice.delete();
	}
}
