package com.nguyenhoangviet.vpcorp_purchase.core.views.widgets;

import com.nguyenhoangviet.vpcorp_purchase.myapplication.R;

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
	private ImageView	imgLeft;

	public interface HeaderViewActionListener{

		public void onBackImgClicked();

		public void onLeftImgClicked();

		public void onRightImgClicked();
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

	public void initView(boolean hasBackBtn, int leftImageId, String title, int rightImageId, HeaderViewActionListener headerViewActionListener){
		this.listener = headerViewActionListener;
		imgBack = (ImageView)findViewById(R.id.img_vnote_header_back);
		imgBack.setVisibility(hasBackBtn ? View.VISIBLE : View.GONE);

		imgLeft = (ImageView)findViewById(R.id.img_vnote_header_left);
		imgLeft.setVisibility(leftImageId != 0 ? View.VISIBLE : View.GONE);

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
			imgLeft.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view){
					listener.onLeftImgClicked();
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
