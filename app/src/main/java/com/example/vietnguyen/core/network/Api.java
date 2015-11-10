package com.example.vietnguyen.core.network;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vietnguyen.core.utils.MU;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by viet on 8/19/2015.
 */
public class Api{

	public interface OnCallApiListener{

		public void onApiResponse(JSONObject response);

		public void onApiError(String errorMsg);
	}

	public void get(Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener){
		makeRequest(Request.Method.GET, context, url, param, onCallApiListener);
	}

	public void post(Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener){
		makeRequest(Request.Method.POST, context, url, param, onCallApiListener);
	}

	private void makeRequest(int method, Context context, String url, JSONObject param, final OnCallApiListener onCallApiListener){
		MU.log("<<<< param: " + param.toString());
		final String finalUrl = Api.getUrl(url, method, param);
		JsonObjectRequest jsonObjRequest = new BasicJsonRequest(method, url, param, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response){
				MU.log("Response for " + finalUrl + ": ");
				MU.log("Response:" + response.toString());
				onCallApiListener.onApiResponse(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error){
				MU.log("Error for " + finalUrl + ": ERROR! " + error.toString());
				onCallApiListener.onApiError(error.toString());
			}
		});

		VolleySingleton.getInstance(context).addToRequestQueue(jsonObjRequest);
	}

	public static String getUrl(String url, int method, JSONObject jsonObject){
		if(method == Request.Method.GET){
			return url + "?" + getQueryString(jsonObject);
		}
		return url;
	}

	private static String getQueryString(JSONObject jsonObject){
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = jsonObject.keys();
		while(iterator.hasNext()){
			String key = iterator.next();
			try{
				builder.append(key + "=" + URLEncoder.encode(jsonObject.optString(key), "UTF-8") + "&");
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
		}
		return builder.toString();
	}

}
