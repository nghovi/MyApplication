package com.example.vietnguyen.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viet on 9/3/2015.
 */

@Table(name = "book")
public class Book extends Model{

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

	public String[] getVocabularyList(){
		return this.vocabulary.split(VOCABULARY_DELIMITER);
	}

	@Column(name = "icon_url")
	@Expose
	public String	iconUrl;

	@Column(name = "author")
	@Expose
	public String	author;

	@Column(name = "name")
	@Expose
	public String	name;

	@Column(name = "comment")
	@Expose
	public String	comment;

	@Column(name = "vocabulary")
	@Expose
	public String	vocabulary;

	@Column(name = "link")
	@Expose
	public String	link;

	@Column(name = "mood")
	@Expose
	public String	mood;
}
