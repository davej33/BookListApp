package com.example.android.booklistapp;

/**
 * Created by dnj on 11/2/16.
 */

public class Book {
    private String mTitle;
    private String mAuthor;
    private String mURL;

    public Book(String title, String author, String url){
        mTitle = title;
        mAuthor = author;
        mURL = url;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmURL() {
        return mURL;
    }
}
