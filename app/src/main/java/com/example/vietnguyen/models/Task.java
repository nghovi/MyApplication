package com.example.vietnguyen.models;

import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by viet on 9/3/2015.
 */
public class Task{

    public static final int STATUS_UNFINISHED = 0;
    public static final int STATUS_FINISHED = 1;

	public Task(int id, int priority,int status, String name, String description, Date date){
		this.id = id;
		this.name = name;
		this.description = description;
        this.priority = priority;
		this.date = date;
        this.status = status;
	}

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this).toString();
    }

    public static Task fromString(String task) {
        Gson gson = new Gson();
        return gson.fromJson(task, Task.class);
    }

    public void setStatus(int status) {
        this.status = status;
    }

	public String	description;
	public Date		date;
	public String	name;
	public String	comment;
	public int		id;
    public int priority;
    public int status;
}
