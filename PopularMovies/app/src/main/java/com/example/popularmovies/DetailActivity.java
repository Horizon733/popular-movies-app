package com.example.popularmovies;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.popularmovies.Adapters.ReviewsAdapter;
import com.example.popularmovies.Adapters.TrailerAdapater;
import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.Favorites;
import com.example.popularmovies.model.Reviews;
import com.example.popularmovies.model.YoutubeTrailer;
import com.example.popularmovies.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private static final int DEFAULT_TASK_ID = -1;
    private int mfavoriteId = DEFAULT_TASK_ID;
    public TrailerAdapater mTrailerAdapter;
    public ReviewsAdapter mReviewsAdapter;
    private AppDatabase mDb;
    private Favorites favorites;
    public RecyclerView mYoutubeRecyclerView;
    public RecyclerView mReviewsRecyclerView;

    private  String poster,posterBD,date,overview,ratings;
    String commonLink = "https://api.themoviedb.org/3/movie/";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDb = AppDatabase.getInstance(getApplicationContext());
        data();

    ActionBar actionBar = getSupportActionBar();

        final int movieId =  getIntent().getIntExtra("movieId",DEFAULT_TASK_ID);
        commonLink += movieId;
        String name = getIntent().getStringExtra("movieName");

        final TextView movieName = findViewById(R.id.movie_name_tv);
        movieName.setText(name);
        actionBar.setTitle(movieName.getText());

        TextView moviePlot = findViewById(R.id.movie_plot_tv);
        moviePlot.setText(overview);
        TextView movieRatings = findViewById(R.id.movie_ratings_tv);
        movieRatings.setText(ratings);
        TextView moviedate = findViewById(R.id.movie_date_tv);
        String convertDate = dateFormat(date);
        if(convertDate != null) {
            moviedate.setText(convertDate);
        }


        ImageView moviePoster = findViewById(R.id.poster_iv);
        Picasso.with(this)
                .load(poster)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(moviePoster);
        ImageView moviePosterBD = findViewById(R.id.poster_back_drop_iv);
        Picasso.with(this)
                .load(posterBD)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(moviePosterBD);
         favorites = new Favorites(movieId,name,poster,overview,date,ratings,posterBD);
        final ToggleButton bookmark = findViewById(R.id.bookmark_movie);
        bookmark.setChecked(false);
        bookmark.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_bookmark_filled));
        bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    if (isChecked ) {
                        bookmark.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_bookmark_filled));

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (mfavoriteId == DEFAULT_TASK_ID) {
                                    Favorites fav =  mDb.movieDAO().loadTasksById(movieId);
                                    if(fav == null){
                                        mDb.movieDAO().insertMovie(favorites);
                                        Log.e("Movie Id Insert",""+favorites.getMovieId());
                                    }
                                }
                            }
                        });
                    } else{
                        bookmark.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_bookmark_border));
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.movieDAO().deleteMovie(favorites);
                                Log.e("Movie Id Delete",""+movieId);
                            }
                        });

                    }

            }
        });
        Favorites fav =  mDb.movieDAO().loadTasksById(movieId);
        int id= 0;
        if(fav != null) {
             id = fav.getMovieId();
            if(id == movieId){
                bookmark.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_bookmark_border));
                bookmark.setChecked(true);
            }
        }

        if(id == 0) {
            bookmark.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_bookmark_border));
            bookmark.setChecked(false);
        }
        mYoutubeRecyclerView = findViewById(R.id.movie_trailers);

        mYoutubeRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mTrailerLayoutManager = new LinearLayoutManager(getApplicationContext());
        mTrailerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mYoutubeRecyclerView.setLayoutManager(mTrailerLayoutManager);

        LinearLayoutManager mReviewsLayoutManager = new LinearLayoutManager(getApplicationContext());
        mReviewsLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mReviewsRecyclerView = findViewById(R.id.movie_reviews);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);

        new fetchYoutubeLinks().execute();
        new fetchReviews().execute();


    }

private void data(){
        poster = getIntent().getStringExtra("moviePoster");
        posterBD = getIntent().getStringExtra("moviePosterBD");
        date = getIntent().getStringExtra("releaseDate");
        overview = getIntent().getStringExtra("overview");
        ratings = getIntent().getStringExtra("ratings");
}

private String dateFormat(String dateConvert){
    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat") SimpleDateFormat DATE_FORMAT = new SimpleDateFormat ("MMM dd, yyyy");
    String releaseDate = null;
    try {
        Date date = simpleDateFormat.parse(dateConvert);
        releaseDate = DATE_FORMAT.format(date);
        Log.d("date",""+releaseDate);
    } catch (ParseException e) {
        e.printStackTrace ();
    }
    return releaseDate;
}

public class fetchYoutubeLinks extends AsyncTask<Void,Void, List<YoutubeTrailer>>{

        public fetchYoutubeLinks(){super();}
    @Override
    protected List<YoutubeTrailer> doInBackground(Void... voids) {
        String videosLink =commonLink+ "/videos";
        URL url = JsonUtils.createUrl(videosLink,null);
        Log.v("Url for videos",""+(url));
        String jsonResponse = null;
        try {
            jsonResponse = JsonUtils.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("MainActivity", "Problem making the HTTP request.", e);
        }
        List<YoutubeTrailer> trailers = JsonUtils.parseYoutubeLinks(jsonResponse);
        if(trailers == null) {
            return null;
        }
        Log.d("Youtube",""+trailers.size());
        return trailers;
    }

    @Override
    protected void onPostExecute(List<YoutubeTrailer> youtubeTrailers) {
        mTrailerAdapter = new TrailerAdapater(DetailActivity.this,youtubeTrailers);
        mYoutubeRecyclerView.setAdapter(mTrailerAdapter);
    }
}


    public class fetchReviews extends AsyncTask<Void,Void, List<Reviews>>{

        @Override
        protected List<Reviews> doInBackground(Void... voids) {
            String reviewsLink = commonLink + "/reviews";
            URL url = JsonUtils.createUrl(reviewsLink,null);
            Log.v("Url for videos",""+(url));
            String jsonResponse = null;
            try {
                jsonResponse = JsonUtils.makeHttpRequest(url);
            } catch (IOException e) {
                Log.e("MainActivity", "Problem making the HTTP request.", e);
            }
            List<Reviews> reviews = JsonUtils.parseReviews(jsonResponse);
            if(reviews == null) {
                return null;
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(List<Reviews> reviews) {
            if(reviews.isEmpty()){
                TextView reviewsTextView = findViewById(R.id.reviews);
                reviewsTextView.setText("No Reviews!");
                reviewsTextView.setAllCaps(false);
                mReviewsRecyclerView.setVisibility(View.GONE);
            }
            mReviewsAdapter = new ReviewsAdapter(DetailActivity.this,reviews);
            mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        }
    }
}
