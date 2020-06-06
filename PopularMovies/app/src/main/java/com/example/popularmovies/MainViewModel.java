package com.example.popularmovies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.Favorites;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    LiveData<List<Favorites>> favorites;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favorites = database.movieDAO().loadAllMovies();
    }

    public LiveData<List<Favorites>> getMovies() {
        return favorites;
    }
}
