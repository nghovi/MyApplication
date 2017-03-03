//package com.example.vietnguyen.views.widgets.notifications.adapters.adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
////import com.andtinder.model.CardModel;
////import com.andtinder.view.CardStackAdapter;
////import com.andtinder.view.SimpleCardStackAdapter;
//import com.example.vpcorp.core.utils.MU;
//import com.example.vpcorp.myapplication.R;
//
///**
// * Created by viet on 9/28/2015.
// */
//public final class MyCardStackAdapter extends CardStackAdapter {
//
//////	private Context	mContext;
////
////	public MyCardStackAdapter(Context mContext){
////		super(mContext);
//////		this.mContext = mContext;
////	}
////
////	@Override
////	public View getCardView(int position, CardModel model, View convertView, ViewGroup parent){
////		if(convertView == null){
////			LayoutInflater inflater = LayoutInflater.from(getContext());
////			convertView = inflater.inflate(R.layout.std_card_inner, parent, false);
////			assert convertView != null;
////		}
////
////		ImageView imgView = (ImageView)convertView.findViewById(R.id.img_cardstack);
////
////		MU.picassaLoadImage(model.getImageUrl(), imgView, getContext());
////
////		((TextView)convertView.findViewById(R.id.pic_title)).setText(model.getTitle());
////		//((TextView)convertView.findViewById(R.id.img_cardstack)).setText(model.getDescription());
////
////		return convertView;
////	}
// }
