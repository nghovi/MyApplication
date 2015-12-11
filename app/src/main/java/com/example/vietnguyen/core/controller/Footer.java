package com.example.vietnguyen.core.controller;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vietnguyen.core.views.widgets.CheckableImageView;
import com.example.vietnguyen.myapplication.R;

/**
 * Created by viet on 8/17/2015.
 * To use this class, you have to include core_footer.xml into your activity's layout
 */
public class Footer{

	private View			viewDivider;
	private LinearLayout	lnrFooter;

	public Footer(Activity activity){
		this.viewDivider = activity.findViewById(R.id.view_footer_divider);
		this.lnrFooter = (LinearLayout)activity.findViewById(R.id.lnr_footer);
	}

	public void setOnItemsClickListener(final View.OnClickListener listener){
		for(int i = 0; i < lnrFooter.getChildCount(); i++){
			View view = lnrFooter.getChildAt(i);
			final int finalI = i;
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					checkFooterItem(finalI);
					listener.onClick(view);
				}
			});
		}
	}

	public void checkFooterItem(int index){
		for(int i = 0; i < lnrFooter.getChildCount(); i++){
			LinearLayout lnrItem = (LinearLayout)lnrFooter.getChildAt(i);
			CheckableImageView img = (CheckableImageView)lnrItem.getChildAt(0);
			TextView txt = (TextView)lnrItem.getChildAt(1);
			if(i == index){
				img.setChecked(true);
                if (txt != null) {
                    txt.setTextColor(Color.RED);
                }
			}else{
				img.setChecked(false);
				txt.setTextColor(Color.BLACK);
			}
		}
	}

	public void hide(){
		setVisibility(View.GONE);
	}

	public void show(){
		setVisibility(View.VISIBLE);
	}

	private void setVisibility(int visibility){
		if(this.viewDivider != null){
			this.viewDivider.setVisibility(visibility);
		}
		this.lnrFooter.setVisibility(visibility);
	}

}
