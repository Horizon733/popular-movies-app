package com.example.popularmovies;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String poster = getIntent().getStringExtra("moviePoster");
        String name = getIntent().getStringExtra("movieName");
        String posterBD = getIntent().getStringExtra("moviePosterBD");
        String date = getIntent().getStringExtra("releaseDate");
        String overview = getIntent().getStringExtra("overview");
        String ratings = getIntent().getStringExtra("ratings");

        TextView movieName = findViewById(R.id.movie_name_tv);
        movieName.setText(name);
        TextView moviePlot = findViewById(R.id.movie_plot_tv);
        moviePlot.setText(overview);
        TextView movieRatings = findViewById(R.id.movie_ratings_tv);
        movieRatings.setText(ratings);
        TextView moviedate = findViewById(R.id.movie_date_tv);
            moviedate.setText(date);

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
    }


}
