package com.example.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.DetailActivity;
import com.example.popularmovies.R;
import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    List<Movie> mMovieData;
    Context mContext;

    public MovieAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mMovieData = movies;

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.movie_list_item;
        View v = LayoutInflater.from(mContext).inflate(layoutIdForListItem, viewGroup, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final Movie currentMovie = mMovieData.get(position);
        Picasso.with(mContext)
                .load(currentMovie.getMoviePoster())
                .fit()
                .error(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .into(holder.mMoviePoster);
        holder.mMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = mContext;
                Class destinationClass = DetailActivity.class;
                Intent intentToStartDetailActivity = new Intent(context, destinationClass);
                intentToStartDetailActivity.putExtra("movieId", currentMovie.getMovieId());
                intentToStartDetailActivity.putExtra("moviePosterBD", currentMovie.getBackDropImage());
                intentToStartDetailActivity.putExtra("movieName", currentMovie.getMovieName());
                intentToStartDetailActivity.putExtra("moviePoster", currentMovie.getMoviePoster());
                intentToStartDetailActivity.putExtra("releaseDate", currentMovie.getReleaseDate());
                intentToStartDetailActivity.putExtra("overview", currentMovie.getOverview());
                intentToStartDetailActivity.putExtra("ratings", currentMovie.getVoteAverage());
                mContext.startActivity(intentToStartDetailActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mMovieData == null || mMovieData.size() == 0) {
            return -1;
        }
        return mMovieData.size();

    }


    public interface MovieAdapterOnClickHandler {
        void onClick(int position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public CardView mParentlayout;
        public ImageView mMoviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mParentlayout = itemView.findViewById(R.id.movie_cv);
            mMoviePoster = itemView.findViewById(R.id.movie_poster);

        }

    }

}
