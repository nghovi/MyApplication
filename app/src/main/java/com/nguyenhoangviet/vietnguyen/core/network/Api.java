package com.nguyenhoangviet.vietnguyen.core.network;

import android.content.Context;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by viet on 8/19/2015.
 */
public class Api{

	public static final int	AUTHORIZATION_TYPE_TOKEN	= 1;
	public static final int	AUTHORIZATION_TYPE_BASIC	= 2;
	public static final int	BODY_TYPE_JSON				= 1;
	public static final int	BODY_TYPE_URLENCODED		= 2;

	private boolean			isPost2						= false;

	public interface OnCallApiListener{

		void OnApiResponse(String url, JSONObject response, OnApiSuccessObserver observer);

		/**
		 * Called when cannot receive response. E.g. 404, ...
		 *
		 * @param url
		 * @param error
		 */
		void OnApiError(String url, VolleyError error);
	}

	/**
	 * Depend on how we implement OnApiResponse
	 */
	public interface OnApiSuccessObserver{

		void onSuccess(JSONObject response);

		void onFailure(JSONObject response);
	}

	public static void get(Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer, String token, int bodyType){
		if(bodyType == BODY_TYPE_URLENCODED){
			makeUrlEncodedJsonRequestTokenAuth(Request.Method.GET, context, url, param, onCallApiListener, observer, token);
		}else{
			makeJsonJsonRequest(Request.Method.GET, true, context, url, param, onCallApiListener, observer, token);
		}
	}

	public static void delete(Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer, String token, int bodyType){
		if(bodyType == BODY_TYPE_URLENCODED){
			makeUrlEncodedJsonRequestTokenAuth(Request.Method.DELETE, context, url, param, onCallApiListener, observer, token);

		}else{
			makeJsonJsonRequest(Request.Method.DELETE, true, context, url, param, onCallApiListener, observer, token);
		}
	}

	public static void postWithTokenAuth(Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer, String token, int bodyType){
		if(bodyType == BODY_TYPE_URLENCODED){
			makeUrlEncodedJsonRequestTokenAuth(Request.Method.POST, context, url, param, onCallApiListener, observer, token);
		}else{
			makeJsonJsonRequest(Request.Method.POST, true, context, url, param, onCallApiListener, observer, token);
		}
	}

	public static void put(Context context, final String url, JSONObject body, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer, String token, int bodyType){
		if(bodyType == BODY_TYPE_URLENCODED){
			makeUrlEncodedJsonRequestTokenAuth(Request.Method.PUT, context, url, body, onCallApiListener, observer, token);
		}else{
			makeJsonJsonRequest(Request.Method.PUT, true, context, url, body, onCallApiListener, observer, token);
		}
	}

	public static void postWithBasicAuth(Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer, int bodyType){
		makeUrlEncodedJsonRequestBasicAuth(Request.Method.POST, context, url, param, onCallApiListener, observer);
	}

	/**
	 * Send application/x-www-form-urlencoded encoded string as body content, receive JSONObject
	 * 
	 * @param method
	 * @param context
	 * @param url
	 * @param param
	 * @param onCallApiListener
	 * @param observer
	 * @param authorizationType
	 */
	private static void makeUrlEncodedJsonRequest(int method, Context context, String url, final JSONObject param, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer, final int authorizationType, final String token){
		if(method == Request.Method.GET){
			url = url + '?' + getQueryString(param);
		}
		final String fullUrl = url;
		JsonObjectRequest jsonObjRequest = new UrlEncodedJsonRequest(method, url, param, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response){
				MU.log("Response for UrlEncoded" + fullUrl + ": ");
				MU.log("Response:" + response.toString());
				onCallApiListener.OnApiResponse(fullUrl, response, observer);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error){
				MU.log("Error for " + fullUrl + ": ERROR! " + error.toString());
				onCallApiListener.OnApiError(fullUrl, error);
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError{
				if(authorizationType == AUTHORIZATION_TYPE_TOKEN){
					return getTokenAuthHeader(token);
				}else if(authorizationType == AUTHORIZATION_TYPE_BASIC){
					return getBasicAuthHeader();
				}
				return new HashMap<>();
			}
		};

		VolleySingleton.getInstance(context).addToRequestQueue(jsonObjRequest);
	}

	private static void makeUrlEncodedJsonRequestTokenAuth(int method, Context context, final String url, final JSONObject param, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer, final String token){
		makeUrlEncodedJsonRequest(method, context, url, param, onCallApiListener, observer, AUTHORIZATION_TYPE_TOKEN, token);
	}

	private static void makeUrlEncodedJsonRequestBasicAuth(int method, Context context, final String url, final JSONObject param, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer){
		makeUrlEncodedJsonRequest(method, context, url, param, onCallApiListener, observer, AUTHORIZATION_TYPE_BASIC, null);
	}

	/**
	 * send JSONObject as body content, receive JSONObject
	 * 
	 * @param method
	 * @param hasQueryString
	 * @param context
	 * @param url
	 * @param body
	 * @param onCallApiListener
	 * @param observer
	 */
	private static void makeJsonJsonRequest(int method, boolean hasQueryString, Context context, final String url, final JSONObject body, final OnCallApiListener onCallApiListener, final OnApiSuccessObserver observer, final String token){
		MU.log("body param for jsonjson request: " + (body != null ? body.toString() : "null"));
		JsonObjectRequest jsonObjRequest = new JsonObjectRequest(method, url, body, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response){
				MU.log("Response for JSONJSON " + url + ": ");
				MU.log("Response:" + response.toString());
				onCallApiListener.OnApiResponse(url, response, observer);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error){
				MU.log("Error for " + url + ": ERROR! " + error.toString());
				onCallApiListener.OnApiError(url, error);
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError{
				return getTokenAuthHeader(token);
			}
		};

		VolleySingleton.getInstance(context).addToRequestQueue(jsonObjRequest);
	}

	public static Map<String, String> getBasicAuthHeader() throws AuthFailureError{
		HashMap<String, String> params = new HashMap<String, String>();
		String creds = String.format("%s:%s", Const.BASIC_AUTH_USERNAME, Const.BASIC_AUTH_PASSWORD);
		String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
		params.put("Authorization", auth);
		return params;
	}

	public static Map<String, String> getTokenAuthHeader(String token) throws AuthFailureError{
		HashMap<String, String> params = new HashMap<String, String>();
		String auth = "Token " + token;
		params.put("Authorization", auth);
		return params;
	}

	private static String getQueryString(JSONObject jsonObject){
		StringBuilder builder = new StringBuilder();
		if(jsonObject != null){
			Iterator<String> iterator = jsonObject.keys();
			while(iterator.hasNext()){
				String key = iterator.next();
				try{
					builder.append(key + "=" + URLEncoder.encode(jsonObject.optString(key), "UTF-8") + "&");
				}catch(UnsupportedEncodingException e){
					e.printStackTrace();
				}
			}
		}
		return builder.toString();
	}

}