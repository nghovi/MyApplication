package com.nguyenhoangviet.vietnguyen.core.network;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by viet on 10/8/2015.
 * # don't know why simple JsonObjectRequest of volley doesn't work, so using UrlEncodedJsonRequest from quicker as temporary solution.
 * Because JsoonObjectRequest used JSONObject as body param, why this class use urlencoded type.
 */
public class UrlEncodedJsonRequest extends JsonObjectRequest{

	private Map<String, String>	mParams	= new HashMap<String, String>();

	public UrlEncodedJsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){

		super(method, url, listener, errorListener);

		MU.log("UrlEncodedJsonRequest========== jsonRequest =  " + (jsonRequest == null ? "NULL" : jsonRequest.toString()));
		try{
			if(jsonRequest != null){
				JSONArray array = jsonRequest.names();
				if(array != null){
					for(int i = 0; i < array.length(); i++){
						String key = array.getString(i);
						String val = jsonRequest.getString(key);
						mParams.put(key, val);
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public String getBodyContentType(){
		return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
	}

	/**
	 * パラメータのマップ
	 */

	@Override
	protected Map<String, String> getParams(){
		return mParams;
	}

	@Override
	public byte[] getBody(){
		Map<String, String> params = getParams();
		if(params != null && params.size() > 0){
			return encodeParameters(params, getParamsEncoding());
		}
		return null;
	}

	/**
	 * Converts <code>params</code> into an application/x-www-form-urlencoded encoded string.
	 */
	private byte[] encodeParameters(Map<String, String> params, String paramsEncoding){
		StringBuilder encodedParams = new StringBuilder();
		try{
			for(Map.Entry<String, String> entry : params.entrySet()){
				encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
				encodedParams.append('&');
			}
			return encodedParams.toString().getBytes(paramsEncoding);
		}catch(UnsupportedEncodingException uee){
			throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
		}
	}
}
