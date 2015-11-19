package com.example.vietnguyen.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.example.vietnguyen.core.utils.MU;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Modifier;

/**
 * Created by viet on 9/3/2015.
 */

public class MyModel extends Model{

	public MyModel(){

	}

	@Override
	public String toString(){
		Gson gson = MU.createNewGson();
		String str = gson.toJson(this).toString();
		return str;
	}

	public MyModel fromString(String model){
		Gson gson = new Gson();
		return gson.fromJson(model, this.getClass());
	}

}
