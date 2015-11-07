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

@Table(name = "book", id="otherId")
public class Book extends Model {

    public static final String WORD_DELIMITER = "@@@@";
    public static final String PHRASE_OPEN_STR = "^^^^";
    public static final String PHRASE_CLOSE_STR = "$$$$";
    public static final String PHRASE_DELIMITER = "&&&&";
    public static final String PHRASE_START_STR = "*";


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
        String[] words = this.vocabulary.split(WORD_DELIMITER);
        for (String word : words) {
            int startPhrasesIdx = word.indexOf(PHRASE_OPEN_STR);
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
        String[] words = this.vocabulary.split(WORD_DELIMITER);
        for (String w : words) {
            if (w.indexOf(word) == 0) {
                int startPhrasesIdx = w.indexOf(PHRASE_OPEN_STR);
                int endPhrasesIdx = w.indexOf(PHRASE_CLOSE_STR);
                if (startPhrasesIdx > 0 && endPhrasesIdx > 0) {
                    String phrases = w.substring(startPhrasesIdx + PHRASE_OPEN_STR.length(), endPhrasesIdx);
                    String[] phrasesArr = phrases.split(PHRASE_DELIMITER);
                    Collections.addAll(result, phrasesArr);
                }
            }
        }
//        appendStartStr(result);
        return result;
    }

//    public String getWordSegment(String word) {
//        List<String> phrases = getWordUsage(word);
//        String phraseSection = "";
//        if (phrases.size() > 0) {
//            phraseSection = Book.PHRASE_OPEN_STR;
//            for (String phrase : phrases) {
//                phraseSection = phraseSection + PHRASE_DELIMITER +
//            }
//        }
//
//        if (vocabulary.contains(Book.WORD_DELIMITER + word)) {
//            for (String phrase : ph)
//            return Book.WORD_DELIMITER + word
//        }
//    }

    public void addWordForBook(String newWord){
        if("".equals(newWord)){
            return;
        }
        //Todo delete special in newWord
        if(vocabulary == null || vocabulary.equals("")){
            vocabulary = newWord;
        }else{
            boolean hasOtherWord = vocabulary.length() > 0;
            if(hasOtherWord){
                vocabulary = newWord + Book.WORD_DELIMITER + vocabulary;
            }else{
                vocabulary = newWord;
            }
        }
    }

    public void deleteWordForBook(String oldWord) {
        if ("".equals(oldWord) || "".equals(vocabulary) || vocabulary.contains(oldWord) == false) {
            return;
        } else {
            if (vocabulary.contains(oldWord + Book.PHRASE_OPEN_STR)) {//has phrases
                if (vocabulary.contains(Book.WORD_DELIMITER + oldWord)) {//has previous word

                } else { //first word
                    if (vocabulary.contains(Book.WORD_DELIMITER)) {//has other word followed
                        vocabulary = vocabulary.substring(vocabulary.indexOf(Book.PHRASE_CLOSE_STR + Book.WORD_DELIMITER) + Book.PHRASE_CLOSE_STR.length() + Book.WORD_DELIMITER.length());
                    } else {//only word
                        vocabulary = "";
                    }
                }
            } else {//no phrase
                if (vocabulary.contains(oldWord + Book.WORD_DELIMITER)) {//has other word followed
                    int startIdx = vocabulary.indexOf(oldWord + Book.WORD_DELIMITER);
                    vocabulary = vocabulary.substring(0, startIdx) + vocabulary.substring(startIdx+oldWord.length() + Book.WORD_DELIMITER.length());
                } else if (vocabulary.contains(Book.WORD_DELIMITER + oldWord)) {//last word in list
                    vocabulary = vocabulary.substring(0, vocabulary.indexOf(Book.WORD_DELIMITER + oldWord));
                } else {//only word
                    vocabulary = "";
                }
            }
        }
    }


    public void addPhraseForWord( String word, String newPhrase){
        if("".equals(word) || "".equals(newPhrase)){
            return;
        }
        //TODO delete special in newPhrase
        if(vocabulary.indexOf(word) == 0){ // first word
            if(vocabulary.indexOf(word + Book.PHRASE_OPEN_STR) == 0){// already has phrases
                int startIdx = word.length() + Book.PHRASE_OPEN_STR.length();
                vocabulary = vocabulary.substring(0, startIdx) + newPhrase + Book.PHRASE_DELIMITER + vocabulary.substring(startIdx);
            }else{
                int startIdx = word.length();
                vocabulary = vocabulary.substring(0, startIdx) + Book.PHRASE_OPEN_STR + newPhrase + Book.PHRASE_CLOSE_STR + vocabulary.substring(startIdx);
            }
        }else{
            int startIdx = vocabulary.indexOf(Book.WORD_DELIMITER + word + Book.PHRASE_OPEN_STR);
            if(startIdx > 0){ // already has phrase
                startIdx = startIdx + Book.WORD_DELIMITER.length() + word.length() + Book.PHRASE_OPEN_STR.length();
                vocabulary = vocabulary.substring(0, startIdx) + newPhrase + Book.PHRASE_DELIMITER + vocabulary.substring(startIdx);
            }else{ // empty
                startIdx = vocabulary.indexOf(Book.WORD_DELIMITER + word) + Book.WORD_DELIMITER.length() + word.length();
                vocabulary = vocabulary.substring(0, startIdx) + Book.PHRASE_OPEN_STR + newPhrase + Book.PHRASE_CLOSE_STR + vocabulary.substring(startIdx);
            }
        }
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

    @Column(name="id")
    @Expose
    public String id;

}
