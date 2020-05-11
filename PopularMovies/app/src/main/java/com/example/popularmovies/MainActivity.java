package com.example.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;
    private GridLayoutManager mLayoutManager;
    private final int NUM_OF_COLUMNS = 2;
    private static String MOVIE_DB = "https://api.themoviedb.org/3/movie/";
    String prefrences = "top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.list);

        mRecyclerView.setHasFixedSize(true);

         mLayoutManager = new GridLayoutManager(getApplicationContext(),NUM_OF_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new FetchDataAsyncTask().execute(prefrences);

    }


    public class FetchDataAsyncTask extends AsyncTask<String, Void, List<Movie>> {
        public FetchDataAsyncTask() {
            super();
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            // Holds data returned from the API
            List<Movie> movieSearchResults = new ArrayList<>();
            Log.e("doInBackground","Gone");

            movieSearchResults = JsonUtils.fetchMovieData(MOVIE_DB, params[0]);

            if (movieSearchResults == null) {
                return null;
            }

            return movieSearchResults;
        }

        protected void onPostExecute(List<Movie> movies) {
            mMovieAdapter = new MovieAdapter(MainActivity.this, movies);
            mRecyclerView.setAdapter(mMovieAdapter);
        }
    }
}
