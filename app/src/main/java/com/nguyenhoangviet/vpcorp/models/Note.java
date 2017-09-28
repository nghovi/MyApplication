package com.nguyenhoangviet.vpcorp.models;

import java.util.Date;

/**
 * Created by viet on 9/3/2015.
 */

public class Note extends MyModel{

	public Note(){

	}

	public Note(String message, Date date){
		this.message = message;
		this.date = date;
	}

	public String	message;

	public Date		date;
}
