package com.nguyenhoangviet.vietnguyen.models;

import com.google.gson.Gson;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Created by viet on 9/3/2015.
 */

public class MyModel{

	public MyModel(){

	}

	public JSONObject toJson(){
		Gson gson = MU.createNewGson();
		try{
			return new JSONObject(gson.toJson(this));
		}catch(JSONException e){
			e.printStackTrace();
		}
		return null;
	}

	public String	id;

}
