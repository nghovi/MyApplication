package com.nguyenhoangviet.vpcorp_purchase.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

/**
 * Created by viet on 9/3/2015.
 */

@Table(name = "Motto", id = "otherId")
public class Motto extends Model{

	public final static int	MOTTO_TYPE_SELF_IMPROVE	= 0;
	public final static int	MOTTO_TYPE_LOVE			= 1;

	public Motto(){

	}

	public Motto(int mottotype, String message){
		this.mottotype = mottotype;
		this.message = message;
	}

	@Column(name = "message")
	@Expose
	public String	message;

	@Column(name = "mottotype")
	@Expose
	public int		mottotype;

	@Column(name = "id")
	@Expose
	public String	id;

}
