package com.example.vietnguyen.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * Created by viet on 9/3/2015.
 */
@Table(name = "task", id = "otherId")
public class Task extends MyModel{

	public static final int			STATUS_UNFINISHED			= 0;
	public static final int			STATUS_FINISHED				= 1;
	public static final int			STATUS_ANY					= 2;
	public static final String[]	STATUS						= new String[]{"Unfinished", "Finished", "Any"};

	public static final String[]	TASK_PRIORITIES				= new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	public static final String[]	TASK_PRIORITIES_WITH_ANY	= new String[]{"Any", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

	public Task(){

	}

	public Task(String id, int priority, int status, String name, String description, String comment, Date date, Date lastupdated){
		this.id = id;
		this.name = name;
		this.description = description;
		this.comment = comment;
		this.priority = priority; // 1->10
		this.date = date;
		this.status = status;
		this.lastupdated = lastupdated;
	}

	public void setStatus(int status){
		this.status = status;
	}

	@Column(name = "id")
	@Expose
	public String	id;

	@Column(name = "description")
	@Expose
	public String	description;

	@Column(name = "date")
	@Expose
	public Date		date;

	@Column(name = "lastupdated")
	@Expose
	public Date		lastupdated;

	@Column(name = "name")
	@Expose
	public String	name;

	@Column(name = "comment")
	@Expose
	public String	comment;

	@Column(name = "priority")
	@Expose
	public int		priority;

	@Column(name = "status")
	@Expose
	public int		status;
}
