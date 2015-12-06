package com.example.vietnguyen.core.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Motto;
import com.example.vietnguyen.models.MyModel;
import com.example.vietnguyen.models.MyModel;
import com.example.vietnguyen.models.MyModel;
import com.example.vietnguyen.myapplication.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by viet on 8/13/2015.
 */
public abstract class MyArrayAdapter<MyModel> extends ArrayAdapter<MyModel>{

	public static final int MODE_NORMAL = 0;
	public static final int MODE_EDITING = 1;
	public static final int MODE_FILTER = 2;

	protected Context					context;
	protected List<MyModel> data;
	protected int						resourceId;
	protected int mode;

	public MyArrayAdapter(Context context, int resourceId, List<MyModel> data){
		super(context, resourceId, data);
		this.context = context;
		this.data = data;
		this.resourceId = resourceId;
	}

	public MyArrayAdapter(Context context, int resourceId){
		super(context, resourceId);
		this.context = context;
		this.data = new ArrayList<MyModel>();
		this.resourceId = resourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(resourceId, null);
		MyModel model = data.get(position);
		buildItemLayout(convertView, model);
		return convertView;
	}

	protected abstract void buildItemLayout(View convertView, MyModel model);


	//todo confirm that renew data with a new ref doesn't work for notifyDataSetChanged ?
	public void updateDataWith(List<MyModel> myModels) {
		//add new items
		for (MyModel myModel : myModels) {
			if (!data.contains(myModel)) {
				data.add(myModel);
			}
		}

		//delete old items
		Iterator<MyModel> id = data.iterator();
		while (id.hasNext()) {
			if (!myModels.contains(id.next())) {
				id.remove();
			}
		}
		this.notifyDataSetChanged();
	}

	public void removeDataItem(MyModel myModel) {
		data.remove(myModel);
	}

	public void addDataItem(MyModel myModel) {
		data.add(myModel);
	}

	public void checkToAddDataItem(MyModel myModel) {
		if (!data.contains(myModel)) {
			addDataItem(myModel);
		}
	}

	public int getDataSize() {
		return data.size();
	}

	public boolean isDataEmpty() {
		return getDataSize() <= 0;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

}
