package com.nguyenhoangviet.vpcorp.models;

import java.util.Date;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

/**
 * Created by viet on 9/3/2015.
 */

@Table(name = "note", id = "otherId")
public class Note extends MyModel{

	public Note(){

	}

	public Note(String message, Date date){
		this.message = message;
		this.date = date;
	}

	@Column(name = "message")
	@Expose
	public String	message;

	@Column(name = "date")
	@Expose
	public Date		date;
}
