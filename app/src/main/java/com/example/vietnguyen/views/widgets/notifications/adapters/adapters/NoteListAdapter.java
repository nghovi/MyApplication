package com.example.vietnguyen.views.widgets.notifications.adapters.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Note;
import com.example.vietnguyen.myapplication.R;

/**
 * Created by viet on 8/13/2015.
 */
public class NoteListAdapter extends ArrayAdapter<Note>{

	private Context					context;
	public List<Note> data;
	private static LayoutInflater	inflater		= null;
	private int						resourceId;
	public boolean					isEditing		= false;
	private int						numItemChecked	= 0;
	private OnCheckItemListener		listener;

	public interface OnCheckItemListener{

		public void onChecked(Note checkedNote, int numItemChecked);
	}

	public NoteListAdapter(Context context, int resourceId, List<Note> data, OnCheckItemListener listener){
		super(context, resourceId, data);
		this.context = context;
		this.data = data;
		this.resourceId = resourceId;
		this.listener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(resourceId, null);
		final Note note = data.get(position);
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
		if(isEditing){
			checkBox.setVisibility(View.VISIBLE);
		}else{
			checkBox.setVisibility(View.GONE);
		}
		return convertView;
	}
}
