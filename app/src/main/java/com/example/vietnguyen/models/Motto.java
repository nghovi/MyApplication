package com.example.vietnguyen.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.example.vietnguyen.core.utils.MU;
import com.google.gson.Gson;
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
