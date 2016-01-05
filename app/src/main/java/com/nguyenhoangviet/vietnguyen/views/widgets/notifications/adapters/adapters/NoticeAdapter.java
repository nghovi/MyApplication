package com.nguyenhoangviet.vietnguyen.views.widgets.notifications.adapters.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.core.views.adapters.MyArrayAdapter;
import com.nguyenhoangviet.vietnguyen.models.Notice;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import java.util.ArrayList;

/**
 * Created by viet on 8/13/2015.
 */
public class NoticeAdapter extends MyArrayAdapter<Notice>{

	private boolean			isEditable	= false;
	private OnNoticeDelete	onDeleteListener;

	public NoticeAdapter(Context context, ArrayList<Notice> data, boolean isEditable, OnNoticeDelete listener){
		super(context, R.layout.item_notice, data);
		this.isEditable = isEditable;
		this.onDeleteListener = listener;
	}

	public interface OnNoticeDelete{

		public void onDelete(Notice notice);
	}

	@Override
	protected void buildItemLayout(View convertView, final Notice notice){
		TextView txtDate = (TextView)convertView.findViewById(R.id.txt_item_remind_time_date);
		txtDate.setText(MU.getDateTimeForDisplaying(notice.noticeDate));
		ImageView imgDeleteIcon = (ImageView)convertView.findViewById(R.id.img_item_remind_time_delete);
		if(isEditable){
			imgDeleteIcon.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					if(onDeleteListener != null){
						onDeleteListener.onDelete(notice);
					}
				}
			});
		}else{
			imgDeleteIcon.setVisibility(View.GONE);
		}
	}
}
