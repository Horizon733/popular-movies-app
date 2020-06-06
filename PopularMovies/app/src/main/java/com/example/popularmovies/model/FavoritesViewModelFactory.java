package com.example.popularmovies.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.popularmovies.database.AppDatabase;

public class FavoritesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mMovieId;

    public FavoritesViewModelFactory(AppDatabase database, int movieId){
        mDb = database;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddMovieViewModel(mDb, mMovieId);
    }
}
