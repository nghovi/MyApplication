package com.nguyenhoangviet.vpcorp.models;

import com.nguyenhoangviet.vpcorp.core.utils.MU;
import com.google.gson.annotations.Expose;

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
		this.isDeleted = false;
	}

	public static void deleteNotices(List<String> noticeIds){
		for(String noticeId : noticeIds){
			// Notice = new Select().from(Notice.class).where("id")
		}
	}

	public static void deleteOverdueNotices(){

	}

	public static ArrayList<Notice> getNoticesForTask(Task task){
		ArrayList<Notice> notices = new ArrayList<Notice>();
//		List<String> noticeIds = task.getNoticeIdList();
//		for(String noticeId : noticeIds){
//			Notice notice = new Select().from(Notice.class).where("id = ?", noticeId).executeSingle();
//			if(notice != null){
//				notices.add(notice);
//			}
//		}
		return notices;
	}

	public static ArrayList<Notice> getOnGoingNoticesForTask(Task task){
		ArrayList<Notice> notices = new ArrayList<Notice>();
//		List<String> noticeIds = task.getNoticeIdList();
//		for(String noticeId : noticeIds){
//			Notice notice = new Select().from(Notice.class).where("otherId = ?", noticeId).where("isDeleted = ?", false).executeSingle();
//			if(notice != null && !MU.isInThePast(notice.noticeDate)){
//				notices.add(notice);
//			}
//		}
		return notices;
	}

	public static ArrayList<Notice> getOnGoingNotices(){
		ArrayList<Notice> notices = new ArrayList<Notice>();
//		List<Notice> allNotices = new Select().from(Notice.class).where("isDeleted = ?", false).execute();
//		for(Notice notice : allNotices){
//			if(!MU.isInThePast(notice.noticeDate)){
//				notices.add(notice);
//			}
//		}
		return notices;
	}

	public static List<Notice> getUnSavedToRemote(){
		List<Notice> result = new ArrayList<Notice>();
		ArrayList<Notice> notices = getOnGoingNotices();
		for(Notice notice : notices){
			if(notice.isRemoteSaved == false){
				result.add(notice);
			}
		}
		return result;
	}
	public String	type;

	public String	title;
	public String	value;
	public Date		noticeDate;
	public String	message;
	public String	noticeType;
}
