package com.nguyenhoangviet.vpcorp_purchase.core.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

/**
 * Created by NDTrung on 4/30/2015.
 */

public class CheckableImageView extends ImageView implements Checkable{

	private boolean		isChecked			= false;

	private final int[]	CHECKED_STATE_SET	= {android.R.attr.state_checked};

	public CheckableImageView(Context context){
		super(context);
	}

	public CheckableImageView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);
	}

	public CheckableImageView(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	@Override
	public int[] onCreateDrawableState(final int extraSpace){
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if(isChecked()) mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		return drawableState;
	}

	@Override
	public void setChecked(boolean checked){
		if(isChecked == checked) return;
		isChecked = checked;
		refreshDrawableState();
	}

	@Override
	public boolean isChecked(){
		return isChecked;
	}

	@Override
	public void toggle(){
		setChecked(!isChecked);
	}

	@Override
	public boolean performClick(){
		toggle();
		return super.performClick();
	}

}
