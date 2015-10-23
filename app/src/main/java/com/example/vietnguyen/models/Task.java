package com.example.vietnguyen.models;

import com.google.gson.Gson;
import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by viet on 9/3/2015.
 */
public class Task extends SugarRecord<Task>{

	public static final int			STATUS_UNFINISHED	= 0;
	public static final int			STATUS_FINISHED		= 1;
	public static final String[]	TASK_PRIORITIES		= new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

	public Task(){

	}

	public Task(int priority, int status, String name, String description, String comment, Date date, Date lastupdated){
		this.name = name;
		this.description = description;
		this.comment = comment;
		this.priority = priority;
		this.date = date;
		this.status = status;
		this.lastupdated = lastupdated;
	}

	@Override
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this).toString();
	}

	public static Task fromString(String task){
		Gson gson = new Gson();
		return gson.fromJson(task, Task.class);
	}

	public void setStatus(int status){
		this.status = status;
	}

	public String	description;
	public Date		date;
	public Date		lastupdated;
	public String	name;
	public String	comment;
	public int		priority;
	public int		status;
}
