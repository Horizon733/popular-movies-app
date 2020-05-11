package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;
    private GridLayoutManager mLayoutManager;
    private static final int NEWS_LOADER_ID = 1;
    private final int NUM_OF_COLUMNS = 2;
    private static String MOVIE_DB = "https://api.themoviedb.org/3/movie/";
    String prefrences = "popular";
    List<Movie> movieSearchResults = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.list);

        mRecyclerView.setHasFixedSize(true);

         mLayoutManager = new GridLayoutManager(getApplicationContext(),NUM_OF_COLUMNS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        TextView mEmptyStateTextView = findViewById(R.id.empty_view_text);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchDataAsyncTask().execute(prefrences);
        } else {
            View loadingIndicator = findViewById(R.id.progress);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_connection);
            LinearLayout parentLayout = findViewById(R.id.layout);
            parentLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }


    }
    public void onClick(int position){
        Context context = this;
        Class destinationClass = DetailActivity.class;

        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, position);
        intentToStartDetailActivity.putExtra("moviePosterBD", movieSearchResults.get(position).getBackDropImage());
        intentToStartDetailActivity.putExtra("movieName", movieSearchResults.get(position).getMovieName());
        intentToStartDetailActivity.putExtra("moviePoster", movieSearchResults.get(position).getMoviePoster());
        intentToStartDetailActivity.putExtra("releaseDate", movieSearchResults.get(position).getReleaseDate());
        intentToStartDetailActivity.putExtra("overview", movieSearchResults.get(position).getOverview());
        intentToStartDetailActivity.putExtra("ratings", movieSearchResults.get(position).getVoteAverage());
        startActivity(intentToStartDetailActivity);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.popular:
                prefrences = "popular";
                new FetchDataAsyncTask().execute(prefrences);
                return true;
            case R.id.top_rated:
                prefrences = "top_rated";
                new FetchDataAsyncTask().execute(prefrences);
                return true;
        }
        return super.onOptionsItemSelected(item);

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

            Log.e("doInBackground","Gone");

            movieSearchResults = JsonUtils.fetchMovieData(MOVIE_DB, params[0]);

            if (movieSearchResults == null) {
                return null;
            }

            return movieSearchResults;
        }

        protected void onPostExecute(List<Movie> movies) {
            mMovieAdapter = new MovieAdapter(MainActivity.this,  MainActivity.this,movies);
            mRecyclerView.setAdapter(mMovieAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }


}
