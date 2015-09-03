package com.example.vietnguyen.models;

/**
 * Created by viet on 9/3/2015.
 */
public class Book {
    public Book(int id, String iconUrl, String author, String name, String comment) {
        this.iconUrl = iconUrl;
        this.author = author;
        this.name = name;
        this.comment = comment;
        this.id = id;
    }
    public String iconUrl;
    public String author;
    public String name;
    public String comment;
    public int id;
}
