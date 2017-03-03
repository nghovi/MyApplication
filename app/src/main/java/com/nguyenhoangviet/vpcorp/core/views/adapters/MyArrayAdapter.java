package com.nguyenhoangviet.vpcorp.core.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.nguyenhoangviet.vpcorp.core.controller.MyFragmentWithListInterface;

import java.util.Iterator;
import java.util.List;

/**
 * Created by viet on 8/13/2015.
 */
public abstract class MyArrayAdapter<MyModel> extends ArrayAdapter<MyModel>{

	public static final int					MODE_NORMAL		= 0;
	public static final int					MODE_EDITING	= 1;
	public static final int					MODE_FILTER		= 2;

	protected Context						context;
	protected List<MyModel>					data;
	protected int							resourceId;
	protected int							mode;
	protected MyFragmentWithListInterface	myFragmentWithListInterface;

	public MyArrayAdapter(Context context, int resourceId, List<MyModel> data){
		super(context, resourceId, data);
		this.context = context;
		this.data = data;
		this.resourceId = resourceId;
	}

	public void setMyFragmentWithListInterface(MyFragmentWithListInterface fragmentWithListInterface){
		this.myFragmentWithListInterface = fragmentWithListInterface;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(resourceId, null);
		MyModel model = getItem(position);
		buildItemLayout(convertView, model);
		return convertView;
	}

	protected abstract void buildItemLayout(View convertView, MyModel model);

	@Override
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
		if(myFragmentWithListInterface != null){
			if(data.size() <= 0){
				myFragmentWithListInterface.onEmptyData();
			}else{
				myFragmentWithListInterface.onNotEmptyData();
			}
		}
	}

	// confirm that renew data with a new ref doesn't work for notifyDataSetChanged ? confirmed.
	// the adapter keep pointing to its first data location
	public void updateDataWith(List<MyModel> myModels){

		// delete all old items
		Iterator<MyModel> id = data.iterator();
		while(id.hasNext()){
			MyModel myModel = id.next(); // must call id.next or else IllegalStateException for id.remove
			id.remove();
		}

		// add new items
		if(myModels != null){
			for(MyModel myModel : myModels){
				data.add(myModel);
			}
		}
		this.notifyDataSetChanged();
	}

	public void removeDataItem(MyModel myModel){
		data.remove(myModel);
	}

	public void addDataItem(MyModel myModel){
		data.add(myModel);
	}

	public void checkToAddDataItem(MyModel myModel){
		if(!data.contains(myModel)){
			addDataItem(myModel);
		}
	}

	public int getDataSize(){
		return data.size();
	}

	public boolean isDataEmpty(){
		return getDataSize() <= 0;
	}

	public void setMode(int mode){
		this.mode = mode;
	}

}
