package com.nguyenhoangviet.vpcorp.models;

import com.nguyenhoangviet.vpcorp.core.utils.MU;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by viet on 9/3/2015.
 */

public class MyModel {

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


	//Never physical delete
	public boolean	isDeleted;

	public boolean	isRemoteSaved;

	public String	id;

}
