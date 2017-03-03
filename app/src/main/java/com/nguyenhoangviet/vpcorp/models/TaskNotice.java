package com.nguyenhoangviet.vpcorp.models;

import java.util.ArrayList;
import java.util.List;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

/**
 * Created by viet on 9/3/2015.
 */
@Table(name = "task_notice", id = "otherId")
public class TaskNotice extends MyModel{

	public TaskNotice(){

	}

	public TaskNotice(String taskId, String noticeId){
		this.taskId = taskId;
		this.noticeId = noticeId;
	}

	public static List<Notice> getNoticesByTask(Task task){
		List<Notice> notices = new ArrayList<Notice>();
		List<TaskNotice> taskNotices = new Select().from(Notice.class).where("taskId = ?", task.id).execute();
		for(TaskNotice taskNotice : taskNotices){
			Notice notice = new Select().from(Notice.class).where("id = ?", taskNotice.noticeId).executeSingle();
			if(notice != null){
				notices.add(notice);
			}
		}
		return notices;
	}

	public static List<TaskNotice> getByNotice(Notice notice) {
		return new Select().from(TaskNotice.class).where("noticeId = ?", notice.id).execute();
	}

	public static void deleteByTask(Task task){
		new Delete().from(TaskNotice.class).where("taskId = ?", task.id).execute();
	}

	public static void deleteByNotice(Notice notice){
		new Delete().from(TaskNotice.class).where("noticeId = ?", notice.id).execute();
	}

	public static void saveNew(String taskId, String noticeId){
		TaskNotice taskNotice = new TaskNotice(taskId, noticeId);
		taskNotice.save();
	}

	@Column(name = "taskId")
	@Expose
	public String	taskId;

	@Column(name = "noticeId")
	@Expose
	public String	noticeId;
}
