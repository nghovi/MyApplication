package com.nguyenhoangviet.vietnguyen.models;

import java.util.Date;

/**
 * Created by viet on 9/3/2015.
 */

public class Note extends MyModel{

	public static final int	TYPE_MEMORY	= 0;
	public static final int	TYPE_IDEA	= 1;

	public Note(){

	}

	public Note(String content, int notetype){
		this.content = content;
		this.notetype = notetype;
	}

	public String	content;

	public int		notetype;
	public Date		modified;
	public Date		created;
}
