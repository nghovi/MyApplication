package com.example.vietnguyen.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.example.vietnguyen.core.utils.MU;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by viet on 9/3/2015.
 */

@Table(name = "book", id = "otherId")
public class Book extends Model{

	public static final String	WORD_DELIMITER		= "@@@@";
	public static final String	PHRASE_OPEN_STR		= "^^^^";
	public static final String	PHRASE_CLOSE_STR	= "$$$$";
	public static final String	PHRASE_DELIMITER	= "&&&&";
	public static final String	PHRASE_START_STR	= "*";

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

	public Set<String> getWords(){
		return getVocabs().keySet();
	}

	public List<String> getPhrasesOfWord(String word){
		return getVocabs().get(word);
	}

	public void addWordForBook(String newWord){
		getVocabs().put(newWord, new ArrayList<String>());
		buildVocabulary();
	}

	public void deleteWord(String oldWord){
		getVocabs().remove(oldWord);
		buildVocabulary();
	}

	public void addPhraseForWord(String word, String newPhrase){
		List<String> phrases = getVocabs().get(word);
		if(phrases != null){
			phrases.add(newPhrase);
		}
		buildVocabulary();
	}

	public void deletePhraseForWord(String word, String phrase){
		List<String> phrases = getVocabs().get(word);
		if(phrases != null && phrase.contains(phrase)){
			phrases.remove(phrase);
		}
		buildVocabulary();
	}

	public List<String> getVocabularyList(){
		List<String> result = new ArrayList<String>();
		String[] words = this.vocabulary.split(WORD_DELIMITER);
		for(String word : words){
			int startPhrasesIdx = word.indexOf(PHRASE_OPEN_STR);
			if(startPhrasesIdx > 0){
				result.add(word.substring(0, startPhrasesIdx));
			}else{
				result.add(word);
			}
		}
		return result;
	}

	public List<String> getWordUsage(String word){
		List<String> result = new ArrayList<String>();
		String[] words = this.vocabulary.split(WORD_DELIMITER);
		for(String w : words){
			if(w.indexOf(word) == 0){
				int startPhrasesIdx = w.indexOf(PHRASE_OPEN_STR);
				int endPhrasesIdx = w.indexOf(PHRASE_CLOSE_STR);
				if(startPhrasesIdx > 0 && endPhrasesIdx > 0){
					String phrases = w.substring(startPhrasesIdx + PHRASE_OPEN_STR.length(), endPhrasesIdx);
					String[] phrasesArr = phrases.split(PHRASE_DELIMITER);
					Collections.addAll(result, phrasesArr);
				}
			}
		}
		return result;
	}

	@Override
	public String toString(){
		Gson gson = MU.createNewGson();
		String str = gson.toJson(this).toString();
		return str;
	}

	public Book fromString(String model){
		Gson gson = new Gson();
		return gson.fromJson(model, Book.class);
	}

	private void buildVocabs(){
		vocabs = new HashMap<String, List<String>>();
		List<String> words = getVocabularyList();
		for(String word : words){
			List<String> phrases = getWordUsage(word);
			vocabs.put(word, phrases);
		}
	}

	public void buildVocabulary(){
		vocabulary = "";
		Iterator it = vocabs.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry wordPhrases = (Map.Entry)it.next();
			String word = (String)wordPhrases.getKey();
			if("".equals(vocabulary)){
				vocabulary = word;
			}else{
				vocabulary = vocabulary + WORD_DELIMITER + word;
			}
			List<String> phrases = (List<String>)wordPhrases.getValue();
			vocabulary = vocabulary + buildPhrases(phrases);
		}
	}

	private String buildPhrases(List<String> phrases){
		String phraseSection = "";
		if(phrases != null && phrases.size() > 0){
			phraseSection = PHRASE_OPEN_STR;
			for(int i = 0; i < phrases.size(); i++){
				if(i == 0){
					phraseSection = phraseSection + phrases.get(i);
				}else{
					phraseSection = phraseSection + PHRASE_DELIMITER + phrases.get(i);
				}
			}
			phraseSection = phraseSection + PHRASE_CLOSE_STR;
		}
		return phraseSection;
	}

	public Map<String, List<String>> getVocabs(){
		if(vocabs == null){
			buildVocabs();
		}
		return vocabs;
	}

	private Map<String, List<String>>	vocabs;

	public String getVocabulary(){
		return vocabulary;
	}

	public void setVocabulary(String vocabulary){
		this.vocabulary = vocabulary;
	}

	public Book clone(){
		Book b = new Book();
		b.id = id;
		b.setVocabulary(vocabulary);
		b.author = author;
		b.name = name;
		b.comment = comment;
		b.iconUrl = iconUrl;
		b.link = link;
		b.mood = mood;
		return b;
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
	private String	vocabulary;

	@Column(name = "link")
	@Expose
	public String	link;

	@Column(name = "mood")
	@Expose
	public String	mood;

	@Column(name = "id")
	@Expose
	public String	id;

}
