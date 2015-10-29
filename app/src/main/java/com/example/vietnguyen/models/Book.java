package com.example.vietnguyen.models;

import com.google.gson.Gson;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viet on 9/3/2015.
 */
public class Book extends SugarRecord<Book>{

	public static final String	VOCABULARY_DELIMITER	= ",";

	public Book(){

	}

	public Book(String iconUrl, String author, String name, String comment, String vocabulary, String mood, String link){
		this.iconUrl = iconUrl;
		this.author = author;
		this.name = name;
		this.comment = comment;
		this.vocabulary = vocabulary;
		this.mood = mood;
		this.link = link;
	}

	@Override
	public String toString(){
		Gson gson = new Gson();
		return gson.toJson(this).toString();
	}

	public static Book fromString(String book){
		Gson gson = new Gson();
		return gson.fromJson(book, Book.class);
	}

	public String[] getVocabularyList(){
		return this.vocabulary.split(VOCABULARY_DELIMITER);
	}

	public String	iconUrl;
	public String	author;
	public String	name;
	public String	comment;
	public String	vocabulary;
	public String	link;
	public String	mood;
}
