package com.example.vietnguyen.core;

/**
 * Created by viet on 8/13/2015.
 */
public class Const{

	public static final String	PREF_KEY_SESSION_ID		= "SESSION_ID";
	public static final String	IMAGE_DIR				= "images";
	public static final int		BITMAP_COMPRESS_QUALITY	= 100;

	// https://developers.facebook.com/apps/966395066741035/dashboard/
	public static final String	FACEBOOK_APP_ID			= "966395066741035";
	public static final String	FACEBOOK_GET_ALBUMS		= "https://graph.facebook.com/me/albums";
	public static final String	FACEBOOK_GET_PHOTOS		= "https://graph.facebook.com/";

//	public static final String	SERVER_URL				= "http://ec2-54-175-13-221.compute-1.amazonaws.com:8000/ecard/";
	public static final String	SERVER_URL				= "http://10.0.3.2:8000/ecard/";

	public static final String	SIGN_IN					= "sign_in";
	public static final String	SIGN_UP					= "sign_up";

	public static final String	GET_PRIMARY_CARD_INFO	= SERVER_URL + "get_primary_card_info";
	public static final String	GET_BOOKS				= SERVER_URL + "get_books";
	public static final String	GET_TASK				= SERVER_URL + "get_tasks";
	public static final String	ADD_TASK				= SERVER_URL + "add_task";
	public static final String	EDIT_TASK				= SERVER_URL + "edit_task";
	public static final String	UPDATE_STATUS_TASK		= SERVER_URL + "update_status_task";
	public static final String	DELETE_TASK				= SERVER_URL + "delete_task";

}
