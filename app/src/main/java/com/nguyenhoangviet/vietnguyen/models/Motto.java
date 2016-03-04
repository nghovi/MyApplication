package com.nguyenhoangviet.vietnguyen.models;

import com.google.gson.annotations.Expose;

/**
 * Created by viet on 9/3/2015.
 */

public class Motto extends MyModel{

	public final static int	MOTTO_TYPE_SELF_IMPROVE	= 0;
	public final static int	MOTTO_TYPE_LOVE			= 1;

	public Motto(){

	}

	public Motto(int mottotype, String message){
		this.mottotype = mottotype;
		this.message = message;
	}

	public String	message;

	public int		mottotype;

	public String	id;

}
