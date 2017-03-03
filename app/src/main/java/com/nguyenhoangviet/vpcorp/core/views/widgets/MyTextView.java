package com.nguyenhoangviet.vpcorp.core.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nguyenhoangviet.vpcorp.myapplication.R;

/**
 * Created by viet on 8/24/2015.
 */
public class MyTextView extends TextView{

	// public String value;

	// public Boolean isPressed = false;

	public String	jsonKey;

	public MyTextView(Context context){
		super(context);
	}

	public MyTextView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.jsonKey};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);
		jsonKey = t.getString(0);
	}

	public MyTextView(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	public String getjsonKey(){
		return jsonKey;
	}

	public void setjsonKey(String jsonKey){
		this.jsonKey = jsonKey;
	}

	public interface  OnAfterTextChangedListener {
		public void afterTextChanged(String before, String after);
	}

	public interface OnKeyboardBtnPressed {
		public void onPress(String text);
	}
}
