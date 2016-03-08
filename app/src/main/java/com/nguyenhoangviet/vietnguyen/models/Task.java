package com.nguyenhoangviet.vietnguyen.models;

import com.nguyenhoangviet.vietnguyen.core.utils.MU;
//import com.facebook.share.widget.ShareDialog;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by viet on 9/3/2015.
 */
public class Task extends MyModel{

	public static final String		NOTICE_ID_DELIMITER		= ",";
	public static final int			STATUS_UNFINISHED		= 0;
	public static final int			STATUS_FINISHED			= 1;
	public static final int			STATUS_ANY				= 2;
	public static final String[]	STATUS					= new String[]{"Unfinished", "Finished", "Any"};	// todo DO NOT change the order

	public static final int			TASK_PRIORITY_HIGH		= 1;
	public static final int			TASK_PRIORITY_MEDIUM	= 2;
	public static final int			TASK_PRIORITY_LOW		= 3;
	public static final String[]	TASK_PRIORITIES			= new String[]{"Any", "High", "Medium", "Low"};
	public static final String[]	TASK_PRIORITIES_REAL	= new String[]{"High", "Medium", "Low"};

	public static final String[]	TASK_PRIORITIES_SHORT	= new String[]{"A", "H", "M", "L"};

	public Task(){

	}

	public String	description;

	public Date		date;

	public Date		lastupdated;

	public String	name;

	public int		priority;

	public int		status;

	public String	noticeId;
}
