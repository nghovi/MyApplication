package com.nguyenhoangviet.vietnguyen.models;

import java.util.List;

/**
 * Created by viet on 9/3/2015.
 */

public class Book extends MyModel{

	public Book(){

	}

	public Word findWord(String syllabus){
		for(Word word : this.words){
			if(word.syllabus.equals(syllabus)){
				return word;
			}
		}
		return null;
	}

	public Book(String iconurl, String author, String name, String comment, String mood){
		this.iconurl = iconurl;
		this.author = author;
		this.name = name;
		this.comment = comment;
	}

	public List<Word>	words;
	public String		iconurl;
	public String		author;
	public String		name;
	public String		comment;
	public Link			link;
}
