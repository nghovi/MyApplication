package com.nguyenhoangviet.vpcorp_purchase.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.nguyenhoangviet.vpcorp_purchase.core.utils.MU;
//import com.facebook.share.widget.ShareDialog;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by viet on 9/3/2015.
 */
@Table(name = "task", id = "otherId")
public class Task extends MyModel{

	public static final String		NOTICE_ID_DELIMITER			= ",";
	public static final int			STATUS_UNFINISHED			= 0;
	public static final int			STATUS_FINISHED				= 1;
	public static final int			STATUS_ANY					= 2;
	public static final String[]	STATUS						= new String[]{"Unfinished", "Finished", "Any"};//todo DO NOT change the order

	public static final int			TASK_PRIORITY_HIGHEST		= 1;
	public static final int			TASK_PRIORITY_HIGH			= 2;
	public static final int			TASK_PRIORITY_MEDIUM		= 3;
	public static final int			TASK_PRIORITY_LOW			= 4;
	public static final int			TASK_PRIORITY_LOWEST		= 5;
	public static final String[]	TASK_PRIORITIES				= new String[]{"Highest", "High", "Medium", "Low", "Lowest"};
	public static final String[]	TASK_PRIORITIES_WITH_ANY	= new String[]{"Any", "Highest", "High", "Medium", "Low", "Lowest"};

	public Task(){

	}

	public Task(String id, int priority, int status, String name, String description, String comment, Date date, Date lastupdated){
		this.id = id;
		this.name = name;
		this.description = description;
		this.comment = comment;
		this.priority = priority; // 1->5
		this.date = date;
		this.status = status;
		this.lastupdated = lastupdated;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public List<String> getNoticeIdList(){
		List<String> result = new ArrayList<String>();
		if(!MU.isEmpty(this.noticeId)){
			String[] noticeIds = this.noticeId.split(NOTICE_ID_DELIMITER);
			Collections.addAll(result, noticeIds);
		}
		return result;
	}

	public void buildNoticeId(List<String> noticeIds){
		this.noticeId = MU.joinString(noticeIds, NOTICE_ID_DELIMITER);
	}

	public void addNoticeIdWithoutSave(String noticeId){
		List<String> noticeIdList = getNoticeIdList();
		noticeIdList.add(noticeId);
		buildNoticeId(noticeIdList);
	}

	public void deleteNoticeId(String noticeId){
		List<String> noticeIdList = getNoticeIdList();
		if(noticeIdList.remove(noticeId)){
			buildNoticeId(noticeIdList);
		}
	}

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

	@Column(name = "noticeId")
	@Expose
	public String	noticeId;
}
