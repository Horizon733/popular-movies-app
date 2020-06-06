package com.example.popularmovies;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.Adapters.MovieAdapter;
import com.example.popularmovies.database.Favorites;
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
    String prefrences = "popular";
    public TextView mEmptyStateTextView;
    List<Movie> movieSearchResults = new ArrayList<>();
    ProgressBar progressBar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getApplicationContext(), NUM_OF_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mEmptyStateTextView = findViewById(R.id.empty_view_text);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ConstraintLayout parentLayout = findViewById(R.id.layout);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchDataAsyncTask().execute(prefrences);
            LinearLayout noConnection = findViewById(R.id.empty_view);
            noConnection.setVisibility(View.GONE);
        } else {
            View loadingIndicator = findViewById(R.id.progress);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_connection);

            parentLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        Bundle queryBundle = new Bundle();
        queryBundle.putString(MOVIE_DB, prefrences);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                prefrences = getString(R.string.popular_movie);
                actionBar.setTitle(getString(R.string.app_name));
                new FetchDataAsyncTask().execute(prefrences);
                return true;
            case R.id.top_rated:
                prefrences = getString(R.string.top_rated_movie);
                actionBar.setTitle(getString(R.string.top_rated));
                new FetchDataAsyncTask().execute(prefrences);
                return true;
            case R.id.favorites:
                prefrences = getString(R.string.favorites);
                actionBar.setTitle(getString(R.string.favorites));
                setupViewModel();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }


    private void setupViewModel() {
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Favorites>>() {
            @Override
            public void onChanged(List<Favorites> favorites) {
                List<Movie> mMovieData = new ArrayList<>();
                for (Favorites fav : favorites) {
                    Movie m = new Movie(fav.getMovieId()
                            , fav.getMovieName()
                            , fav.getMoviePoster()
                            , fav.getOverview()
                            , fav.getReleaseDate()
                            , Double.parseDouble(fav.getVoteAverage())
                            , fav.getBackDropImage());
                    mMovieData.add(m);
                }
                new FetchDataAsyncTask().onPostExecute(mMovieData);
            }
        });
    }

    public class FetchDataAsyncTask extends AsyncTask<String, Void, List<Movie>> {
        public FetchDataAsyncTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            // Holds data returned from the API

            Log.e("doInBackground", "Gone");

            movieSearchResults = JsonUtils.fetchMovieData(MOVIE_DB, params[0]);

            if (movieSearchResults == null) {
                return null;
            }

            return movieSearchResults;
        }


        protected void onPostExecute(List<Movie> movies) {
            mMovieAdapter = new MovieAdapter(MainActivity.this, movies);
            mRecyclerView.setAdapter(mMovieAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }


}
