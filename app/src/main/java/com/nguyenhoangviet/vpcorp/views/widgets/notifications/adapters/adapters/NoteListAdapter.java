package com.nguyenhoangviet.vpcorp.views.widgets.notifications.adapters.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nguyenhoangviet.vpcorp.core.utils.MU;
import com.nguyenhoangviet.vpcorp.core.views.adapters.MyArrayAdapter;
import com.nguyenhoangviet.vpcorp.models.Note;
import com.nguyenhoangviet.vpcorp.myapplication.R;

/**
 * Created by viet on 8/13/2015.
 */
public class NoteListAdapter extends MyArrayAdapter<Note> {

	private int						numItemChecked	= 0;
	private OnCheckItemListener		listener;

	public interface OnCheckItemListener{

		public void onChecked(Note checkedNote, int numItemChecked);
	}

	public NoteListAdapter(Context context, int resourceId, OnCheckItemListener listener){
		super(context, resourceId, new ArrayList<Note>());
		this.listener = listener;
	}

	@Override
	protected void buildItemLayout(View convertView, final Note note) {
		TextView txtMessage = (TextView)convertView.findViewById(R.id.item_note_message);
		txtMessage.setText(note.message);
		TextView txtDate = (TextView)convertView.findViewById(R.id.item_note_date);
		txtDate.setText(MU.getDateForDisplaying(note.date));
		CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.item_note_checkbox);
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b){
				numItemChecked = b ? numItemChecked + 1 : numItemChecked - 1;
				if(listener != null){
					listener.onChecked(note, numItemChecked);
				}
			}
		});
		if(mode == MyArrayAdapter.MODE_EDITING){
			checkBox.setVisibility(View.VISIBLE);
		}else{
			checkBox.setVisibility(View.GONE);
		}
	}
}
