package com.nguyenhoangviet.vietnguyen.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * Created by viet on 9/3/2015.
 */
public class TaskNotice extends MyModel{

	public TaskNotice(){

	}

	public TaskNotice(String taskId, String noticeId){
		this.taskId = taskId;
		this.noticeId = noticeId;
	}

	public String	taskId;

	public String	noticeId;
}
