package com.example.mana.movieapp;

/**
 * Created by Abdelrahman on 8/19/2016.
 */
public class review_encaps {
String content,author;
    public review_encaps(String content,String author)
    {
        this.setAuthor(author);
        this.setContent(content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
