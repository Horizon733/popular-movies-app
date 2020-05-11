package com.example.popularmovies.model;

public class Movie {
    private String mBackDropImage;
    private String mMovieName;
    private String mMoviePoster;
    private String mOverview;
    private String mReleaseDate;
    private Double mVoteAverage;
    private final String POSTER_URL = "https://image.tmdb.org/t/p/w185";
    public Movie(){
    }
    public Movie(String movieName,String moviePoster,String overview,String releaseDate,Double voteAverage,String backDropImage){
        mMovieName = movieName;
        mMoviePoster = moviePoster;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mBackDropImage = backDropImage;
    }


    public String getMovieName(){return mMovieName;}
    public String getMoviePoster(){return POSTER_URL+mMoviePoster;}
    public String getOverview(){return mOverview;}
    public String getReleaseDate(){return mReleaseDate;}
    public String getVoteAverage(){return Double.toString(mVoteAverage);}


    public String getBackDropImage(){return POSTER_URL+mBackDropImage;}
}
