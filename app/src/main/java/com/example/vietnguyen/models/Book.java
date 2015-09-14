package com.example.vietnguyen.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viet on 9/3/2015.
 */
public class Book {
    public Book(int id, String iconUrl, String author, String name, String comment, List<String> vocabulary) {
        this.iconUrl = iconUrl;
        this.author = author;
        this.name = name;
        this.comment = comment;
        this.vocabulary = vocabulary;
        this.id = id;
    }
    public String iconUrl;
    public String author;
    public String name;
    public String comment;
    public List<String> vocabulary;
    public int id;
}
