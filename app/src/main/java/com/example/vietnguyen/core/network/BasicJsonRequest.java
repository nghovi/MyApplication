package com.example.vietnguyen.core.network;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by viet on 10/8/2015.
 * #todo: don't know why simple JsonObjectRequest of volley doesn't work, so using BasicJsonRequest from quicker as temporary solution.
 */
public class BasicJsonRequest extends JsonObjectRequest{

	private Map<String, String>	mParams	= new HashMap<String, String>();

	public BasicJsonRequest(int method, String url, Map<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
		super(method, url, "", listener, errorListener);
		mParams = params;
	}

	public BasicJsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){

		super(method, url, "", listener, errorListener);
		try{
			JSONArray array = jsonRequest.names();
			if(array != null){
				for(int i = 0; i < array.length(); i++){
					String key = array.getString(i);
					String val = jsonRequest.getString(key);
					mParams.put(key, val);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError{
		HashMap<String, String> params = new HashMap<String, String>();
		String creds = String.format("%s:%s", "shk", "258456");
		String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
		params.put("Authorization", auth);
		return params;
	}

	/**
	 * パラメータのマップ
	 */

	@Override
	protected Map<String, String> getParams(){
		return mParams;
	}

	/**
	 * リクエストパラメータの形式を、JSON形式ではなく、通常のPOSTに変更する。通常のJSONObjectRequestではこの設定ができない。
	 * Request.javaから引用
	 *
	 * @return
	 */
	public String getBodyContentType(){
		return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
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
