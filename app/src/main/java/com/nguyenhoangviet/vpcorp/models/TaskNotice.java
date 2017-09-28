package com.nguyenhoangviet.vpcorp.models;

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

	public static List<Notice> getNoticesByTask(Task task){
		List<Notice> notices = new ArrayList<Notice>();
//		List<TaskNotice> taskNotices = new Select().from(Notice.class).where("taskId = ?", task.id).execute();
//		for(TaskNotice taskNotice : taskNotices){
//			Notice notice = new Select().from(Notice.class).where("id = ?", taskNotice.noticeId).executeSingle();
//			if(notice != null){
//				notices.add(notice);
//			}
//		}
		return notices;
	}

	public String	taskId;

	public String	noticeId;
}
