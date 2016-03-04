package com.nguyenhoangviet.vietnguyen.core.network;

import android.content.Context;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by viet on 8/19/2015.
 */
public class Api{

	public interface OnCallApiListener{

		void OnApiResponse(String url, JSONObject response, OnApiSuccessObserver observer);

		/**
		 * Called when cannot receive response. E.g. 404, ...
		 *
		 * @param url
		 * @param errorMsg
		 */
		void OnApiError(String url, String errorMsg);
	}

	/**
	 * Depend on how we implement OnApiResponse
	 */
	public interface OnApiSuccessObserver{

		void onSuccess(JSONObject response);

		void onFailure(JSONObject repsone);
	}

	public static void get(Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer){
		makeRequest(Request.Method.GET, context, url, param, onCallApiListener, observer);
	}

	public static void post(Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer){
		makeRequest(Request.Method.POST, context, url, param, onCallApiListener, observer);
	}

	private static void makeRequest(int method, Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer){
		MU.log("<<<< param: " + param.toString());
		final String finalUrl = Api.getUrl(url, method, param);
		JsonObjectRequest jsonObjRequest = new BasicJsonRequest(method, finalUrl, param, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response){
				MU.log("Response for " + finalUrl + ": ");
				MU.log("Response:" + response.toString());
				onCallApiListener.OnApiResponse(url, response, observer);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error){
				MU.log("Error for " + finalUrl + ": ERROR! " + error.toString());
				onCallApiListener.OnApiError(url, error.toString());
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