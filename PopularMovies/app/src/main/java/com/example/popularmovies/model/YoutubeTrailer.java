package com.example.popularmovies.model;

public class YoutubeTrailer {
    private String mTrailerKey;
    private String mTrailerTitle;
    private String trailerLink = "https://www.youtube.com/watch?v=";
    private String mTrailerThumbnailLink = "https://img.youtube.com/vi/";

    public YoutubeTrailer(String trailerKey,String trailerTitle){
        this.mTrailerKey = trailerKey;
        this.mTrailerTitle = trailerTitle;
    }

    public String getTrailerLink(){return trailerLink+this.mTrailerKey;}
    public String getTrailerTitle(){return mTrailerTitle;}
    public String getTrailerThumbnailLink(){return mTrailerThumbnailLink+this.mTrailerKey+"/default.jpg";}
}
