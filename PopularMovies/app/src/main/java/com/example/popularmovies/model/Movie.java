package com.example.popularmovies.model;

public class Movie {
    private int mMovieId;
    private String mBackDropImage;
    private String mMovieName;
    private String mMoviePoster;
    private String mOverview;
    private String mReleaseDate;
    private Double mVoteAverage;
    public static final String POSTER_URL = "https://image.tmdb.org/t/p/w185";
    public Movie(){
    }
    public Movie(int movieId,String movieName,String moviePoster,String overview,String releaseDate,Double voteAverage,String backDropImage){
        mMovieId = movieId;
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

public int getMovieId(){return mMovieId;}
    public String getBackDropImage(){return POSTER_URL+mBackDropImage;}
}
