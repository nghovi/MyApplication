package com.example.vietnguyen.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.views.widgets.CoreTextView;
import com.example.vietnguyen.models.GsonModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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

	public static JSONObject buildJsonObjFromModel(Object obj){
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		try{
			return new JSONObject(json);
		}catch(JSONException e){
			e.printStackTrace();
		}
		return null;
	}

	public static <T>T convertToModel(String jsonString, Class<T> cls){
		T result = null;

		try{
			Gson ex = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ssZZZ").create();
			if(jsonString != null){
				result = ex.fromJson(jsonString, cls);
			}
		}catch(Exception var4){
			var4.printStackTrace();
			return null;
		}

		return result;
	}

	public static <T>List<T> convertToModelList(String jsonString, Class<T> cls){
		Object result = new ArrayList();

		try{
			Gson ex = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ssZZZ").create();
			if(jsonString != null){
				result = (List)ex.fromJson(jsonString, new GsonModel(cls));
			}

			return (List)result;
		}catch(Exception var4){
			var4.printStackTrace();
			return new ArrayList();
		}
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

	public static void picassaLoadImage(final String url, final ImageView imageView, Context context){
		Picasso.with(context).load(url).into(imageView, new Callback.EmptyCallback() {

			@Override
			public void onSuccess(){
				super.onSuccess();
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

	public static boolean isSameDay(Date d1, Date d2){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
	}

	// Not working, don't know why
	// public static Date getDayOnly(Date originDate) {
	// Calendar cal = Calendar.getInstance();
	// cal.setTime(originDate);
	// cal.clear(Calendar.HOUR);
	// cal.clear(Calendar.AM_PM);
	// cal.clear(Calendar.MINUTE);
	// cal.clear(Calendar.SECOND);
	// cal.clear(Calendar.MILLISECOND);
	// return cal.getTime();
	// }

	/*
	 * Determine the enable/disable status of a button base on EditTexts or TextViews
	 * ONLY enable button when ALL text views already has data.
	 * @param views: List of EditText or TextView
	 * usage example: addTextWatcher(mBtnCheckRecruiting, R.drawable.shape_button_enable2, Arrays.asList((View)mTxtStartDt, mEdtQuestion));
	 */
	public static void addTextWatcher(final View clickableText, final List<View> views){
		clickableText.setEnabled(false);
		TextWatcher tw = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){

			}

			@Override
			public void afterTextChanged(Editable s){
				for(View view : views){
					if(((TextView)view).getText().toString().equals("")){
						clickableText.setVisibility(View.INVISIBLE);
						return;
					}
				}
				clickableText.setVisibility(View.VISIBLE);
				clickableText.setEnabled(true);
			}
		};

		for(View view : views){
			((TextView)(view)).addTextChangedListener(tw);
		}
	}
}
