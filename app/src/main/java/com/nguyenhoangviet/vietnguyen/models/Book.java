package com.nguyenhoangviet.vietnguyen.models;

import com.nguyenhoangviet.vietnguyen.core.utils.MU;

import java.util.List;

/**
 * Created by viet on 9/3/2015.
 */

public class Book extends MyModel{

	public Book(){

	}

	public Word findWord(String syllabus){
		if(this.words == null){
			return null;
		}
		for(Word word : this.words){
			if(word.syllabus.equals(syllabus)){
				return word;
			}
		}
		return null;
	}

	public boolean hasPhrase(String content){
		if(this.words == null){
			return false;
		}
		for(Word word : this.words){
			if(MU.checkMatch(word.syllabus, content)){
				return true;
			}
			if(word.phrases == null){
				return false;
			}

			for(Phrase phrase : word.phrases){
				if(MU.checkMatch(phrase.content, content)){
					return true;
				}
			}
		}
		return false;
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
