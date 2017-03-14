package com.nguyenhoangviet.vpcorp_purchase.core.controller;

import com.nguyenhoangviet.vpcorp_purchase.controllers.FragmentOfMainActivity;
import com.nguyenhoangviet.vpcorp_purchase.core.views.widgets.HeaderView;
import com.nguyenhoangviet.vpcorp_purchase.myapplication.R;

/**
 * Created by viet on 8/11/2015.
 * http://developer.android.com/guide/components/fragments.html
 */
public class MyFragmentWithHeaderFooter extends FragmentOfMainActivity implements HeaderView.HeaderViewActionListener{

	protected HeaderView	headerView;

	protected void buildLayout(){
		buildHeader();
	}

	protected void buildHeader() {
		headerView = (HeaderView)getView(R.id.view_vnote_header);
		String title = getHeaderTitle();
		boolean hasBackButton = hasBackButton();
		int leftImageId = getLeftImageId();
		int rightImageId = getRightImageId();
		headerView.initView(hasBackButton, leftImageId, title, rightImageId, this);
	}

	protected int getRightImageId(){
		return R.drawable.ic_mode_edit_white_36dp;
	}

	protected boolean hasBackButton(){
		return false;
	}

	protected String getHeaderTitle(){
		return "xxx";
	}

	@Override
	public void onRightImgClicked(){

	}

	@Override
	public void onBackImgClicked(){
		onClickBackBtn();
	}

	@Override
	public void onLeftImgClicked() {

	}

	public int getLeftImageId(){
		return 0;
	}
}
