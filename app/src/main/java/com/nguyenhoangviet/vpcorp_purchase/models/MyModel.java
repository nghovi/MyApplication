package com.nguyenhoangviet.vpcorp_purchase.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.nguyenhoangviet.vpcorp_purchase.core.utils.MU;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by viet on 9/3/2015.
 */

@Table(name = "my_model", id = "otherId")
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

	public static <T>List<T>  getDeleted( Class model){
		return (List<T>)new Select().from(model).where("isDeleted = ?", true).execute();
	}

	public static <T>List<T>  getAllUndeleted( Class model){
		return (List<T>)new Select().from(model).where("isDeleted = ?", false).execute();
	}

	public static <T>List<T>  getUnSavedToRemote( Class model){
		return (List<T>)new Select().from(model).where("isRemoteSaved = ?", false).execute();
	}


	//Never physical delete
	@Column(name = "isDeleted")
	@Expose
	public boolean	isDeleted;

	@Column(name = "isRemoteSaved")
	@Expose
	public boolean	isRemoteSaved;


	@Column(name = "id")
	@Expose
	public String	id;

}
