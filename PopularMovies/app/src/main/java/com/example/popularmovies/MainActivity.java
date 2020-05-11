package com.example.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utils.JsonUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final int NUM_OF_COLUMNS = 2;
    private static String MOVIE_DB = "https://api.themoviedb.org/3/movie/";
    String prefrences = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, NUM_OF_COLUMNS);
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
            List<Movie> movieSearchResults = null;

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
