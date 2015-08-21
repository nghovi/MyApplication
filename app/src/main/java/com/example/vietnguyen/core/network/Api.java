package com.example.vietnguyen.core.network;

import android.content.Context;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vietnguyen.core.utils.MU;

/**
 * Created by viet on 8/19/2015.
 */
public class Api{

	public interface OnCallApiListener{
		public void onApiResponse(String url, JSONObject response);
        public void onApiError(String url, String errorMsg);
	}

	public static void get(Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener){
		makeRequest(Request.Method.GET, context, url, param, onCallApiListener);
	}

    public static void post(Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener){
        makeRequest(Request.Method.POST, context, url, param, onCallApiListener);
    }

    private static void makeRequest(int method, Context context, final String url, JSONObject param, final OnCallApiListener onCallApiListener){
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(method, url, param, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response){
                MU.log("Response for " + url + ": " + response.toString());
                onCallApiListener.onApiResponse(url, response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error){
                MU.log("Error for " + url + ": ERROR! " + error.toString());
               onCallApiListener.onApiError(url, error.toString());
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjRequest);
    }

}
