package com.example.vietnguyen.controllers;

import java.io.File;
import java.util.Arrays;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.database.DBHelper;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;

public class PrimaryCardFragment extends MyFragment{

	private DBHelper		dbHelper;
	private ListView		listView;
	private final String	IMAGE_NAME	= "main_card.png";

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
		File mainImage = MU.getImageFile(IMAGE_NAME, activity);
		if(mainImage.exists()){
			MU.loadImage(IMAGE_NAME, imgMain, activity);
		}else{
			MU.picassaLoadAndSaveImage("http://i.huffpost.com/gen/1653793/thumbs/o-EMMA-WATSON-OSCARS-2014-570.jpg?8", imgMain, activity, IMAGE_NAME);
		}
		LinearLayout lnrCardInfo = (LinearLayout)getView().findViewById(R.id.lnr_primary_card_info);
		JSONObject jsonObject = MU.buildJsonObj(Arrays.asList("name", "Hoang Viet", "sex", "Male"));
		MU.interpolate(lnrCardInfo, jsonObject);
	}
}
