package com.example.vietnguyen.core.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.vietnguyen.myapplication.R;

/**
 * Created by viet on 8/24/2015.
 */
public class CoreTextView extends TextView{

	// public String value;

	// public Boolean isPressed = false;

	public String	jsonKey;

	public CoreTextView(Context context){
		super(context);
	}

	public CoreTextView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.jsonKey};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);
		jsonKey = t.getString(0);
	}

	public CoreTextView(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	public String getjsonKey(){
		return jsonKey;
	}

	public void setjsonKey(String jsonKey){
		this.jsonKey = jsonKey;
	}
}
