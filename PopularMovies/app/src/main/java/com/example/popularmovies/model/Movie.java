package com.example.popularmovies.model;

public class Movie {
    private String mMovieName;
    private String mMoviePoster;
    private String mOverview;
    private String mReleaseDate;
    private float mVoteAverage;
    private final String POSTER_URL = "https://image.tmdb.org/t/p/w185";
    public Movie(){
    }

    public void setMovieName(String movieName){mMovieName = movieName;}
    public void setMoviePoster(String moviePoster){mMoviePoster = moviePoster;}
    public void setOverview(String overview){mOverview = overview;}
    public void setReleaseDate(String releaseDate){mReleaseDate = releaseDate;}
    public void setVoteAverage(float voteAverage){mVoteAverage = voteAverage;}

    public String getMovieName(){return mMovieName;}
    public String getMoviePoster(){return POSTER_URL+mMoviePoster;}
    public String getOverview(){return mOverview;}
    public String getReleaseDate(){return mReleaseDate;}
    public float getVoteAverage(){return mVoteAverage;}
}
