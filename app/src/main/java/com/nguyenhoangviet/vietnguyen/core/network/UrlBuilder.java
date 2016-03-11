package com.nguyenhoangviet.vietnguyen.core.network;

import com.android.volley.Request;
import com.nguyenhoangviet.vietnguyen.core.model.Url;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Book;
import com.nguyenhoangviet.vietnguyen.models.Link;
import com.nguyenhoangviet.vietnguyen.models.Note;
import com.nguyenhoangviet.vietnguyen.models.Phrase;
import com.nguyenhoangviet.vietnguyen.models.Task;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by viet on 3/10/2016.
 */
public class UrlBuilder{

//	public static final String	SERVER_URL	= SERVER_URL + "";
	public static final String	SERVER_URL	= "http://ec2-54-186-181-131.us-west-2.compute.amazonaws.com:8000/ecard/";


	// public static final String SIGN_IN = SERVER_URL + "api-token-auth/";
	// public static final String SIGN_UP = "sign_up";
	//
	// public static final String GET_PRIMARY_CARD_INFO = SERVER_URL + "get_primary_card_info/";
	// public static final String GET_BOOKS = SERVER_URL + "books/";
	// public static final String SEARCH_BOOKS = SERVER_URL + "search_books/";
	// public static final String GET_BOOK_DETAIL = SERVER_URL + "book/";
	//
	// public static final String ADD_BOOK = SERVER_URL + "add_book/";
	// public static final String EDIT_BOOK = SERVER_URL + "edit_book/";
	// public static final String DELETE_BOOK = SERVER_URL + "delete_book/";
	// public static final String ADD_PHRASE = SERVER_URL + "add_phrase/";
	// public static final String ADD_WORD = SERVER_URL + "add_word/";
	// public static final String DELETE_WORD = SERVER_URL + "delete_word/";
	// public static final String DELETE_PHRASE = SERVER_URL + "delete_phrase/";
	//
	// public static final String GET_TASKS = SERVER_URL + "tasks/";
	// public static final String GET_TASKS_BY_DATE = SERVER_URL + "tasks_by_date/";
	// public static final String ADD_TASK = SERVER_URL + "add_task/";
	// public static final String EDIT_TASK = SERVER_URL + "edit_task/";
	// public static final String DELETE_TASK = SERVER_URL + "delete_task/";
	//
	// public static final String GET_NOTICES = SERVER_URL + "get_notices/";
	// public static final String ADD_NOTICE = SERVER_URL + "add_notice/";
	// public static final String EDIT_NOTICE = SERVER_URL + "edit_notice/";
	// public static final String DELETE_NOTICE = SERVER_URL + "delete_notice/";
	//
	// public static final String GET_TASK_NOTICES = SERVER_URL + "get_notices/";
	// public static final String ADD_TASK_NOTICE = SERVER_URL + "add_notice/";
	// public static final String EDIT_TASK_NOTICE = SERVER_URL + "edit_notice/";
	// public static final String DELETE_TASK_NOTICE = SERVER_URL + "delete_notice/";
	//
	// public static final String GET_MOTTOS = SERVER_URL + "get_mottos/";
	//
	// public static final String GET_NOTES = SERVER_URL + "notes/";
	// public static final String ADD_NOTE = SERVER_URL + "add_note/";
	// public static final String EDIT_NOTE = SERVER_URL + "note/";
	// public static final String DELETE_NOTE = SERVER_URL + "delete_notes/";

	public static Url signIn(String username, String password){
		Url url = new Url();
		url.method = Request.Method.POST;
		url.baseUrl = SERVER_URL + "api-token-auth/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_BASIC;
		url.param = new MU.JsonBuilder().add("username", username).add("password", password).getJsonObj();
		url.bodyType = Api.BODY_TYPE_URLENCODED;
		return url;
	}

	public static Url taskList(JSONObject filterValues){
		Url url = new Url();
		url.method = Request.Method.GET;
		url.baseUrl = SERVER_URL + "tasks/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = filterValues;
		url.bodyType = Api.BODY_TYPE_URLENCODED;
		return url;
	}

	public static Url addTask(Task task){
		Url url = new Url();
		url.method = Request.Method.POST;
		url.baseUrl = SERVER_URL + "tasks/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = task.toJson();
		url.bodyType = Api.BODY_TYPE_JSON;
		return url;
	}

	public static Url editTask(Task task){
		Url url = new Url();
		url.method = Request.Method.PUT;
		url.baseUrl = SERVER_URL + "task/id=" + task.id + "/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = task.toJson();
		url.bodyType = Api.BODY_TYPE_JSON;
		return url;
	}

	public static Url deleteTask(String taskId){
		Url url = new Url();
		url.method = Request.Method.DELETE;
		url.baseUrl = SERVER_URL + "task/id=" + taskId + "/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = null;
		url.bodyType = Api.BODY_TYPE_JSON;
		return url;
	}

	// //////////////////////////////////////////////////////////////////// Note
	public static Url noteList(JSONObject filterValues){
		Url url = new Url();
		url.method = Request.Method.GET;
		url.baseUrl = SERVER_URL + "notes/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = filterValues;
		url.bodyType = Api.BODY_TYPE_URLENCODED;
		return url;
	}

	public static Url editNote(Note note){
		Url url = new Url();
		url.method = Request.Method.PUT;
		url.baseUrl = SERVER_URL + "note/id=" + note.id + "/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = note.toJson();
		url.bodyType = Api.BODY_TYPE_JSON;
		return url;
	}

	public static Url addNote(Note note){
		Url url = new Url();
		url.method = Request.Method.POST;
		url.baseUrl = SERVER_URL + "notes/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = note.toJson();
		url.bodyType = Api.BODY_TYPE_JSON;
		return url;
	}

	public static Url deleteNote(String noteIds){
		Url url = new Url();
		url.method = Request.Method.POST;
		url.baseUrl = SERVER_URL + "delete_notes/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = new MU.JsonBuilder().add("ids", noteIds).getJsonObj();
		url.bodyType = Api.BODY_TYPE_URLENCODED;
		return url;
	}

	// ////////////////////////////////////////////////////////////////////// Book
	public static Url bookList(JSONObject filterValues){
		Url url = new Url();
		url.method = Request.Method.GET;
		url.baseUrl = SERVER_URL + "books/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = filterValues;
		url.bodyType = Api.BODY_TYPE_URLENCODED;
		return url;
	}

	public static Url bookDetail(String bookId){
		Url url = new Url();
		url.method = Request.Method.GET;
		url.baseUrl = SERVER_URL + "book/id=" + bookId + "/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = null;
		url.bodyType = Api.BODY_TYPE_URLENCODED;
		return url;
	}

	public static Url addBook(Book book){
		Url url = new Url();
		url.method = Request.Method.POST;
		url.baseUrl = SERVER_URL + "books/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = book.toJson();
		url.bodyType = Api.BODY_TYPE_JSON;
		return url;
	}

	public static Url editBook(Book book){
		Url url = new Url();
		url.method = Request.Method.PUT;
		url.baseUrl = SERVER_URL + "book/id=" + book.id + "/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = book.toJson();
		url.bodyType = Api.BODY_TYPE_JSON;
		return url;
	}

	public static Url deleteBook(String bookId){
		Url url = new Url();
		url.method = Request.Method.DELETE;
		url.baseUrl = SERVER_URL + "book/id=" + bookId + "/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = null;
		url.bodyType = Api.BODY_TYPE_JSON;
		return url;
	}

	public static Url addWord(String bookId, String syllabus, String content){
		Url url = new Url();
		url.method = Request.Method.POST;
		url.baseUrl = SERVER_URL + "add_word/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = new MU.JsonBuilder().add("book_id", bookId).add("new_word", syllabus).add("new_phrase", content).getJsonObj();
		url.bodyType = Api.BODY_TYPE_URLENCODED;
		return url;
	}

	public static Url deleteWord(String wordId){
		Url url = new Url();
		url.method = Request.Method.DELETE;
		url.baseUrl = SERVER_URL + "word/id=" + wordId + "/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = null;
		url.bodyType = Api.BODY_TYPE_JSON;
		return url;
	}

	public static Url addPhrase(String wordId, String content){
		Url url = new Url();
		url.method = Request.Method.POST;
		url.baseUrl = SERVER_URL + "add_phrase/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = new MU.JsonBuilder().add("word_id", wordId).add("new_phrase", content).getJsonObj();
		url.bodyType = Api.BODY_TYPE_URLENCODED;
		return url;
	}

	public static Url deletePhrase(String phraseId){
		Url url = new Url();
		url.method = Request.Method.DELETE;
		url.baseUrl = SERVER_URL + "phrase/id=" + phraseId + "/";
		url.authorizationType = Api.AUTHORIZATION_TYPE_TOKEN;
		url.param = null;
		url.bodyType = Api.BODY_TYPE_JSON;
		return url;
	}

	private static String buildUrlParam(JSONObject param){
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = param.keys();
		while(iterator.hasNext()){
			String key = iterator.next();
			try{
				builder.append(key + "=" + URLEncoder.encode(param.optString(key), "UTF-8"));
				if(iterator.hasNext()){
					builder.append("&");
				}else{
					builder.append("/");
				}
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
}
