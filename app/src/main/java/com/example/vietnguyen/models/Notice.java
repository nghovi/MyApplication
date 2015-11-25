package com.example.vietnguyen.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.vietnguyen.core.utils.MU;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by viet on 9/3/2015.
 * This model is used for saving future notification for task, motto, etc...
 */

@Table(name = "notice", id = "otherId")
public class Notice extends MyModel{

	public static final String NOTICE_TYPE_TASK = "0";
	public static final String NOTICE_TYPE_MOTTO= "1";


	public Notice(){

	}

	public Notice(String type, String title, String message, String value, Date noticeDate) {

	}

	public static void deleteNotices(List<String> noticeIds) {
		for (String noticeId : noticeIds) {
//			Notice = new Select().from(Notice.class).where("id")
		}
	}

	@Column(name = "title")
	@Expose
	public String	title;

	@Column(name = "value")
	@Expose
	public String	value;

	@Column(name = "noticeDate")
	@Expose
	private Date	noticeDate;

	@Column(name = "message")
	@Expose
	public String	message;

	@Column(name = "noticeType")
	@Expose
	public String noticeType;

	@Column(name = "id")
	@Expose
	public String	id;

}
