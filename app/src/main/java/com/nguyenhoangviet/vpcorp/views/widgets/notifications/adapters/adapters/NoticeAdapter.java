package com.nguyenhoangviet.vpcorp.views.widgets.notifications.adapters.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenhoangviet.vpcorp.core.utils.MU;
import com.nguyenhoangviet.vpcorp.core.views.adapters.MyArrayAdapter;
import com.nguyenhoangviet.vpcorp.models.Notice;
import com.nguyenhoangviet.vpcorp.myapplication.R;

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
