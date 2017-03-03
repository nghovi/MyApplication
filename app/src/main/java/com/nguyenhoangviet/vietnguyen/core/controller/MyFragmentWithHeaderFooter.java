package com.nguyenhoangviet.vietnguyen.core.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.core.views.widgets.HeaderView;
import com.nguyenhoangviet.vietnguyen.core.views.widgets.MyTextView;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by viet on 8/11/2015.
 * http://developer.android.com/guide/components/fragments.html
 */
public class MyFragmentWithHeaderFooter extends MyFragment implements HeaderView.HeaderViewActionListener{

	protected HeaderView headerView;

	protected void buildLayout(){
		headerView = (HeaderView) getView(R.id.view_vnote_header);
		String title = getHeaderTitle();
		boolean hasBackButton = hasBackButton();
		headerView.initView(title, R.drawable.ic_mode_edit_white_36dp, hasBackButton, this);
	}

	protected boolean hasBackButton() {
		return false;
	}


	@Override
	public void onRightImgClicked() {

	}

	@Override
	public void onBackImgClicked() {
		onClickBackBtn();
	}

	protected String getHeaderTitle() {
		return "xxx";
	}
}
