package com.example.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDAO {
    @Query("SELECT * FROM favorites")
    LiveData<List<Favorites>> loadAllMovies();

    @Insert
    void insertMovie(Favorites favorites);

    @Query("SELECT * FROM favorites WHERE movieId = :id")
    Favorites loadTasksById(int id);

    @Delete
    void deleteMovie(Favorites favorites);
}
