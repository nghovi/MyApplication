package com.example.vietnguyen.core.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

/**
 * Created by NDTrung on 4/30/2015.
 */



public class QkCheckableImageView extends ImageView implements Checkable {

	private boolean						mChecked			= false;

	private final int[]					CHECKED_STATE_SET	= {android.R.attr.state_checked};

    public interface QkOnCheckedChangeListener{

        void onCheckedChanged(Checkable view, boolean isChecked);
    }

	private QkOnCheckedChangeListener	mOnCheckedChangeListener;

	public QkCheckableImageView(Context context){
		super(context);
	}

	public QkCheckableImageView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);
		// setOnClickListener(this);
	}

	public QkCheckableImageView(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
		// / setOnClickListener(this);
	}

	@Override
	public int[] onCreateDrawableState(final int extraSpace){
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if(isChecked()) mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		return drawableState;
	}

	@Override
	protected void drawableStateChanged(){
		super.drawableStateChanged();
		invalidate();
	}

	@Override
	public void setChecked(boolean checked){
		if(mChecked == checked) return;
		mChecked = checked;
		refreshDrawableState();

		if(mOnCheckedChangeListener != null){
			mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
		}
	}

	@Override
	public boolean isChecked(){
		return mChecked;
	}

	@Override
	public void toggle(){
		setChecked(!mChecked);
	}

	@Override
	public boolean performClick(){
		toggle();
		return super.performClick();
	}

	// @Override
	// public void onClick(View v) {
	// toggle();
	// }

	public void setOnCheckedChangeListener(QkOnCheckedChangeListener listener){
		mOnCheckedChangeListener = listener;
	}
}
