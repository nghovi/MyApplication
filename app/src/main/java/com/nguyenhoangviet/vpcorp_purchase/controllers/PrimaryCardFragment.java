package com.nguyenhoangviet.vpcorp_purchase.controllers;

import java.util.Arrays;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nguyenhoangviet.vpcorp_purchase.core.controller.MyFragment;
import com.nguyenhoangviet.vpcorp_purchase.core.database.DBHelper;
import com.nguyenhoangviet.vpcorp_purchase.core.utils.MU;
import com.nguyenhoangviet.vpcorp_purchase.myapplication.R;

public class PrimaryCardFragment extends MyFragment{

	private DBHelper		dbHelper;
	private ListView		listView;
	private final String	IMAGE_NAME	= "main_card1.png";

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_primary_card, container, false);
		dbHelper = new DBHelper(getActivity());

		return view;
	}

	@Override
	public void buildLayout(){
		ImageView imgMain = (ImageView)getView().findViewById(R.id.img_primary_card_main);
		imgMain.setScaleType(ImageView.ScaleType.FIT_XY);

		ImageView imgSex = getImageView(R.id.icn_sex);
		// todo if it's female
		if(false){
//			imgSex.setImageResource(R.drawable.ico_female_16);
		}

		// File mainImage = MU.getImageFile(IMAGE_NAME, activity);
		// if(mainImage.exists()){
		// MU.loadImage(IMAGE_NAME, imgMain, activity);
		// }else{
		// MU.picassaLoadAndSaveImage("https://s-media-cache-ak0.pinimg.com/736x/cd/c6/d3/cdc6d3066ce29d922df73f2549ad72c3.jpg", imgMain, activity,
		// IMAGE_NAME);
		// }
		LinearLayout lnrCardInfo = (LinearLayout)getView().findViewById(R.id.lnr_primary_card_info);
		JSONObject jsonObject = MU.buildJsonObj(Arrays.asList("name", "Hoang Viet", "sex", "Male", "age", "25", "motto", "Don't work hard, work intelligent"));
		MU.interpolate(lnrCardInfo, jsonObject);
	}
}
