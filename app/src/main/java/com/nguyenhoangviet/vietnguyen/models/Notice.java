package com.nguyenhoangviet.vietnguyen.models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by viet on 9/3/2015.
 * This model is used for saving future notification for task, motto, etc...
 */

public class Notice extends MyModel{

	public static final String	NOTICE_TYPE_TASK	= "0";
	public static final String	NOTICE_TYPE_MOTTO	= "1";

	public Notice(){

	}

	public Notice(String type, String title, String message, String value, Date noticeDate){
		this.type = type;
		this.title = title;
		this.message = message;
		this.value = value;
		this.noticeDate = noticeDate;
	}

	public String	type;

	public String	title;

	public String	value;

	public Date		noticeDate;

	public String	message;

	public String	noticeType;
}
