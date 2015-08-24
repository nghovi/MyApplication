package com.example.vietnguyen.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.views.widgets.CoreTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by viet on 8/7/2015.
 */

public class MU{

	// Log
	public static int		LOG_COUNTER	= 0;
	public static String	LOG_TAG		= "******";

	public static void log(){
		LOG_COUNTER++;
		Log.e(LOG_TAG, String.valueOf(LOG_COUNTER));
	}

	public static void log(String msg){
		LOG_COUNTER++;
		Log.e(LOG_TAG, String.valueOf(LOG_COUNTER) + ' ' + msg);
	}

	public static void log(String msg, JSONObject jsonObject){
		LOG_COUNTER++;
		log(msg + ":\n" + jsonObject.toString());
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	public static JSONObject getJson(ViewGroup layout, JSONObject jsonObject){
		if(layout == null){
			return jsonObject;
		}

		if(jsonObject == null){
			jsonObject = new JSONObject();
		}

		try{
			Resources resources = layout.getContext().getResources();
			for(int i = 0; i < layout.getChildCount(); i++){
				String jsonKey = "";
				View view = layout.getChildAt(i);
				if(view instanceof CoreTextView){
					jsonKey = ((CoreTextView)view).getjsonKey();
					jsonObject.put(jsonKey, ((CoreTextView)view).getText().toString());
				}
			}
		}catch(JSONException e){
			e.printStackTrace();
			log("JSONObject getJson exception");
		}
		return jsonObject;
	}

	public static void interpolate(ViewGroup layout, JSONObject jsonObject){
		if(layout == null || jsonObject == null){
			return;
		}

		for(int i = 0; i < layout.getChildCount(); i++){
			String jsonKey = "";
			View view = layout.getChildAt(i);
			if(view instanceof CoreTextView){
				jsonKey = ((CoreTextView)view).getjsonKey();
				((CoreTextView)view).setText(jsonObject.optString(jsonKey));
			}else if(view instanceof ViewGroup){
				interpolate((ViewGroup)view, jsonObject);
			}
		}
	}

	public static JSONObject buildJsonObj(List<String> keyValues){
		JSONObject jsonObject = new JSONObject();
		try{
			for(int i = 0; i < keyValues.size(); i += 2){
				jsonObject.put(keyValues.get(i), keyValues.get(i + 1));
			}
		}catch(JSONException e){
			e.printStackTrace();
		}

		return jsonObject;
	}

	public static void picassaLoadImage(final String url, final ImageView imageView, final Context context){
		Picasso.with(context).load(url).into(imageView, new Callback.EmptyCallback() {

			@Override
			public void onSuccess(){
				super.onSuccess();
			}

			@Override
			public void onError(){
				log("picassaLoadImage failed for url " + url);
			}
		});
	}

	public static void picassaLoadAndSaveImage(final String url, final ImageView imageView, final Context context, final String fileName){
		Picasso.with(context).load(url).into(imageView, new Callback.EmptyCallback() {

			@Override
			public void onSuccess(){
				super.onSuccess();
				saveImage(imageView, fileName, context);
			}

			@Override
			public void onError(){
				log("picassaLoadAndSaveImage failed for url " + url);
			}
		});
	}

	public static void loadImage(String fileName, ImageView imageView, Context context){
		File f = getImageFile(fileName, context);
		try{
			Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
			imageView.setImageBitmap(b);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}

	public static void saveImage(ImageView imageView, String fileName, Context context){
		File mypath = getImageFile(fileName, context);
		FileOutputStream fos = null;
		try{

			fos = new FileOutputStream(mypath);
			Bitmap bitmapImage = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
			bitmapImage.compress(Bitmap.CompressFormat.PNG, Const.BITMAP_COMPRESS_QUALITY, fos);
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static File getImageFile(String fileName, Context context){
		ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
		File directory = cw.getDir(Const.IMAGE_DIR, Context.MODE_PRIVATE);
		File mypath = new File(directory, fileName);
		return mypath;
	}

	public static boolean isImageExist(String fileName, Context context){
		File file = getImageFile(fileName, context);
		return file.exists();
	}
}
