package com.example.vietnguyen.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.example.vietnguyen.core.utils.MU;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by viet on 9/3/2015.
 */

@Table(name = "book")
public class Book extends Model {

    public static final String VOCABULARY_DELIMITER = ",";
    public static final String VOCABULARY_USAGE_DELIMITER_OPEN = "[";
    public static final String VOCABULARY_USAGE_DELIMITER_CLOSE = "]";
    public static final String PHRASE_DELIMITER = ";";
    public static final String PHRASE_START_STR = "* ";


    public Book() {

    }

    public Book(String iconUrl, String author, String name, String comment, String vocabulary, String mood, String link) {
        this.iconUrl = iconUrl;
        this.author = author;
        this.name = name;
        this.comment = comment;
        this.vocabulary = vocabulary;
        this.mood = mood;
        this.link = link;
    }

    public List<String> getVocabularyList() {
        List<String> result = new ArrayList<String>();
        String[] words = this.vocabulary.split(VOCABULARY_DELIMITER);
        for (String word : words) {
            int startPhrasesIdx = word.indexOf(VOCABULARY_USAGE_DELIMITER_OPEN);
            if (startPhrasesIdx > 0) {
                result.add(word.substring(0, startPhrasesIdx));
            } else {
                result.add(word);
            }
        }
        return result;
    }

    public List<String> getWordUsage(String word) {
        List<String> result = new ArrayList<String>();
        String[] words = this.vocabulary.split(VOCABULARY_DELIMITER);
        for (String w : words) {
            if (w.indexOf(word) == 0) {
                int startPhrasesIdx = w.indexOf(VOCABULARY_USAGE_DELIMITER_OPEN);
                int endPhrasesIdx = w.indexOf(VOCABULARY_USAGE_DELIMITER_CLOSE);
                if (startPhrasesIdx > 0 && endPhrasesIdx > 0) {
                    String phrases = w.substring(startPhrasesIdx + 1, endPhrasesIdx);
                    String[] phrasesArr = phrases.split(PHRASE_DELIMITER);
                    Collections.addAll(result, phrasesArr);
                }
            }
        }
//        appendStartStr(result);
        return result;
    }

    public void appendStartStr(List<String> input) {
        for (ListIterator i = input.listIterator(); i.hasNext(); ) {
            i.set(PHRASE_START_STR + i.next());
        }
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

    @Column(name = "icon_url")
    @Expose
    public String iconUrl;

    @Column(name = "author")
    @Expose
    public String author;

    @Column(name = "name")
    @Expose
    public String name;

    @Column(name = "comment")
    @Expose
    public String comment;

    @Column(name = "vocabulary")
    @Expose
    public String vocabulary;

    @Column(name = "link")
    @Expose
    public String link;

    @Column(name = "mood")
    @Expose
    public String mood;
}
