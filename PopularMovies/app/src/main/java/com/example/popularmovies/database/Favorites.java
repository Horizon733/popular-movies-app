package com.example.popularmovies.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.popularmovies.model.Movie;

@Entity(tableName = "favorites")
public class Favorites {

    private String movieName;
    @PrimaryKey
    private int movieId;
    private String mBackDropImage;
    private String mMoviePoster;
    private String mOverview;
    private String mReleaseDate;
    private String mVoteAverage;

    public Favorites(int movieId, String movieName, String moviePoster, String overview, String releaseDate, String voteAverage, String backDropImage) {
        this.movieName = movieName;
        this.movieId = movieId;
        this.mMoviePoster = moviePoster;
        this.mOverview = overview;
        this.mReleaseDate = releaseDate;
        this.mVoteAverage = voteAverage;
        this.mBackDropImage = backDropImage;
    }


    public String getMovieName() {
        return movieName;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMoviePoster() {
        return (mMoviePoster = mMoviePoster.replaceAll(Movie.POSTER_URL, ""));
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public String getBackDropImage() {
        return mBackDropImage = mBackDropImage.replaceAll(Movie.POSTER_URL, "");
    }
}
