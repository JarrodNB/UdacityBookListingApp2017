package com.example.android.book_listing;

public class Book {

    private String title;
    private String author;
    private String websiteUrl;

    public Book(String title, String author, String websiteUrl){
        this.title = title;
        this.author = author;
        this.websiteUrl = websiteUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getWebsiteUrl(){
        return websiteUrl;
    }

}
