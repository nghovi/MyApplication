package com.example.vietnguyen.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.example.vietnguyen.core.utils.MU;
import com.google.gson.annotations.Expose;

/**
 * Created by viet on 9/3/2015.
 */

@Table(name = "note", id = "otherId")
public class Note extends MyModel{

	public Note(){

	}

	public Note(String message, Date date){
		this.message = message;
		this.date = date;
	}

	@Column(name = "message")
	@Expose
	public String	message;

	@Column(name = "date")
	@Expose
	public Date		date;
}
