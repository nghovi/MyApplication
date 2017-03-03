package com.nguyenhoangviet.vietnguyen.core.views.widgets;

import com.nguyenhoangviet.vietnguyen.myapplication.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by viet on 3/2/2017.
 */

public class HeaderView extends LinearLayout{

	private TextView	txtTitle;
	private ImageView	imgBack;
	private ImageView	imgRight;

	public interface HeaderViewActionListener{

		public void onRightImgClicked();

		public void onBackImgClicked();
	}

	HeaderViewActionListener	listener;

	public HeaderView(Context context){
		super(context);
	}

	public HeaderView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public HeaderView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
	}

	public void initView(String title, int rightImageId, boolean hasBackBtn, final HeaderViewActionListener listener){
		this.listener = listener;
		imgBack = (ImageView)findViewById(R.id.img_vnote_header_back);
		imgBack.setVisibility(hasBackBtn ? View.VISIBLE : View.GONE);

		txtTitle = (TextView)findViewById(R.id.txt_vnote_header_title);
		txtTitle.setText(title);

		imgRight = (ImageView)findViewById(R.id.img_vnote_header_right);
		if(rightImageId != 0){
			imgRight.setImageResource(rightImageId);
		}

		if(this.listener != null){
			imgBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view){
					listener.onBackImgClicked();
				}
			});
			imgRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view){
					listener.onRightImgClicked();
				}
			});
		}
	}
}
