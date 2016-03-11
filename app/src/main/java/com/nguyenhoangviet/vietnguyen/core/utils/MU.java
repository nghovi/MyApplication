package com.nguyenhoangviet.vietnguyen.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.views.widgets.MyTextView;
import com.nguyenhoangviet.vietnguyen.models.GsonModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nguyenhoangviet.vietnguyen.myapplication.BuildConfig;
import com.nguyenhoangviet.vietnguyen.myapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by viet on 8/7/2015.
 */

public class MU{

	// Log
	public static int		LOG_COUNTER	= 0;
	public static String	LOG_TAG		= "VNOTE******";

	public static void log(){
		if(BuildConfig.DEVELOP){
			LOG_COUNTER++;
			Log.e(LOG_TAG, String.valueOf(LOG_COUNTER));
		}
	}

	public static void log(String msg){
		if(BuildConfig.DEVELOP){
			LOG_COUNTER++;
			Log.e(LOG_TAG, String.valueOf(LOG_COUNTER) + ' ' + msg);
		}
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
				if(view instanceof MyTextView){
					jsonKey = ((MyTextView)view).getjsonKey();
					jsonObject.put(jsonKey, ((MyTextView)view).getText().toString());
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
			if(view instanceof MyTextView){
				jsonKey = ((MyTextView)view).getjsonKey();
				((MyTextView)view).setText(jsonObject.optString(jsonKey));
			}else if(view instanceof ViewGroup){
				interpolate((ViewGroup)view, jsonObject);
			}
		}
	}

	public static final class JsonBuilder{

		JSONObject	jsonObject;

		public JsonBuilder(){
			this.jsonObject = new JSONObject();
		}

		public JsonBuilder add(String key, Object value){
			try{
				this.jsonObject.put(key, value);
			}catch(JSONException e){
				e.printStackTrace();
			}
			return this;
		}

		public JSONObject getJsonObj(){
			return this.jsonObject;
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
		Gson gson = createNewGson();
		String json = gson.toJson(obj);
		try{
			return new JSONObject(json);
		}catch(JSONException e){
			e.printStackTrace();
		}
		return null;
	}

	public static Gson createNewGson(){
		GsonBuilder gsonBuilder = new GsonBuilder();
//		gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.setDateFormat(Const.APP_DATE_FORMAT);
		Gson gson = gsonBuilder.create();
		return gson;
	}

	public static <T>T convertToModel(String jsonString, Class<T> cls){
		T result = null;

		try{
			Gson gson = createNewGson();
			if(jsonString != null){
				result = gson.fromJson(jsonString, cls);
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
			Gson gson = createNewGson();
			if(jsonString != null){
				result = (List)gson.fromJson(jsonString, new GsonModel(cls));
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
				log("picassaLoadAndSaveImage failed for baseUrl " + url);
			}
		});
	}

	public static void picassaLoadImage(final String url, final ImageView imageView, Context context){
		if(MU.isEmpty(url)){
			return;
		}
		Picasso.with(context).load(url).into(imageView, new Callback.EmptyCallback() {

			@Override
			public void onSuccess(){
				super.onSuccess();
			}

			@Override
			public void onError(){
				log("picassaLoadAndSaveImage failed for baseUrl " + url);
			}
		});
	}

	public static void loadImage(Context context, String url, String fileName, ImageView imageView){
		File f = getImageFile(fileName, context);
		if(f.exists()){
			try{
				Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
				imageView.setImageBitmap(b);
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}else if(!MU.isEmpty(url)){
			picassaLoadAndSaveImage(url, imageView, context, fileName);
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

	public static void saveBitMapImage(Bitmap bitmapImage, String fileName, Context context){
		File mypath = getImageFile(fileName, context);
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(mypath);
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

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static boolean isEmpty(String str){
		return str == null || "".equals(str);
	}

	public static boolean isNotEmpty(String str){
		return str != null && !"".equals(str);
	}

	public static boolean isSameDay(Date d1, Date d2){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);
		return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
	}

	public static String getDateString(String dateStr, String sourceFormat, String destinateFormat){
		SimpleDateFormat sourceFormatter = new SimpleDateFormat(sourceFormat);
		SimpleDateFormat destinateFormatter = new SimpleDateFormat(destinateFormat);
		try{
			Date date = sourceFormatter.parse(dateStr);
			return destinateFormatter.format(date);
		}catch(ParseException e){
			e.printStackTrace();
		}
		return dateStr;
	}

	public static String getDateString(Date date, String destinateFormat){
		SimpleDateFormat destinateFormatter = new SimpleDateFormat(destinateFormat);
		return destinateFormatter.format(date);
	}

	public static String getAppDateString(Date date){
		return getDateString(date, Const.APP_DATE_FORMAT);
	}

	public static boolean checkMatch(String parent, String pattern){
		if(isEmpty(parent) || isEmpty(pattern)){
			return false;
		}
		return Pattern.compile(Pattern.quote(pattern), Pattern.CASE_INSENSITIVE).matcher(parent).find();
	}

	public static String getDateForDisplaying(Date date){
		// http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
		SimpleDateFormat ex = new SimpleDateFormat("EEEE, MMM d, yyyy");
		return ex.format(date);
	}

	public static String getYearDisplay(Date date){
		SimpleDateFormat ex = new SimpleDateFormat("yyyy");
		return ex.format(date);
	}

	public static String getMonthDisplay(Date date){
		SimpleDateFormat ex = new SimpleDateFormat("MM");
		return ex.format(date);
	}

	public static String getDayDisplay(Date date){
		SimpleDateFormat ex = new SimpleDateFormat("dd");
		return ex.format(date);
	}

	public static String getDateTimeForDisplaying(Date date){
		// http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
		SimpleDateFormat ex = new SimpleDateFormat("HH:mm EEEE, MMM d, yyyy");
		return ex.format(date);
	}

	public static boolean isInThePast(Date date){
		return date.getTime() < Calendar.getInstance().getTime().getTime();
	}

	public static String joinString(Iterable<? extends CharSequence> s, String delimiter){
		Iterator<? extends CharSequence> iter = s.iterator();
		if(!iter.hasNext()) return "";
		StringBuilder buffer = new StringBuilder(iter.next());
		while(iter.hasNext())
			buffer.append(delimiter).append(iter.next());
		return buffer.toString();
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

	// //////////////////////////////////////////////////////////////////VIEW SUPPORT///////////////////////////////////////////////////////////////

}
