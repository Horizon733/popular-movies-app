package com.example.popularmovies.model;

public class Reviews {
    private String mAuthor;
    private String mContent;
    private String mReviewLink;

    public Reviews(String author, String content, String reviewLink){
        this.mAuthor = author;
        this.mContent = content;
        this.mReviewLink = reviewLink;
    }
    public String getauthor(){return mAuthor;}
    public String getcontent(){return mContent;}
    public String getReviewLink(){return mReviewLink;}
}
