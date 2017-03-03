package com.nguyenhoangviet.vietnguyen.core.controller;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.models.Motto;
import com.nguyenhoangviet.vietnguyen.models.MyModel;
import com.nguyenhoangviet.vietnguyen.models.Notice;
import com.nguyenhoangviet.vietnguyen.models.Task;
import com.nguyenhoangviet.vietnguyen.models.TaskNotice;

import android.content.Context;

/**
 * Created by viet on 12/29/2015.
 */
public class MyApplication extends com.activeandroid.app.Application{

	// Make sure to check for null for this variable
	public static volatile Context	appContext	= null;

	@Override
	public void onCreate(){
		super.onCreate();
//		Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("ecard.db").
//				addModelClass(MyModel.class).
//				addModelClass(Task.class).
//				addModelClass(Motto.class).
//				addModelClass(Book.class).
//				addModelClass(Notice.class).
//				addModelClass(TaskNotice.class)
//				.create();
//
//		ActiveAndroid.initialize(dbConfiguration);
	}

	@Override
	public void onTerminate(){
		super.onTerminate();
	}
}
