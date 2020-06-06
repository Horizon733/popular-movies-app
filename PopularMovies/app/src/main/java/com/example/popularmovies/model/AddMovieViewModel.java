package com.example.popularmovies.model;

import androidx.lifecycle.ViewModel;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.Favorites;

public class AddMovieViewModel extends ViewModel {
    private Favorites favorites;

    public AddMovieViewModel(AppDatabase database, int movieId) {
        favorites = database.movieDAO().loadTasksById(movieId);
    }

    public Favorites getTasks() {
        return favorites;
    }
}
