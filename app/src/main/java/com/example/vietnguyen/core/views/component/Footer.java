package com.example.vietnguyen.core.views.component;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.vietnguyen.core.views.widgets.QkCheckableImageView;

/**
 * Created by NDTrung on 4/30/2015.
 */
public class Footer extends LinearLayout{

	private List<QkCheckableImageView>	mImages	= new ArrayList<QkCheckableImageView>();

	public Footer(Context context){
		super(context, null);
	}

	public Footer(Context context, AttributeSet attrs){
		super(context, attrs, 0);
	}

	private void setChildrenView(){
		for(int i = 0; i < this.getChildCount(); i++){
			View view = this.getChildAt(i);
			if(view instanceof ImageView){
				mImages.add((QkCheckableImageView)view);
			}
		}
	}

	private void setOnClickListener(){
		for(QkCheckableImageView image : mImages){
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v){
					uncheckedAll();
					((QkCheckableImageView)v).setChecked(true);
				}
			});
		}
	}

	public void uncheckedAll(){
		// if(CCCollectionUtil.isEmpty(mImages)){
		// setChildrenView();
		// }
		for(QkCheckableImageView image : mImages){
			image.setChecked(false);
		}
	}
}
