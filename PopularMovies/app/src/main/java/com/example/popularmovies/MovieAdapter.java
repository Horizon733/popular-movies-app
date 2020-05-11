package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    List<Movie> mMovieData;
    Context mContext;

    public MovieAdapter(Context context, List<Movie> movies){
        mContext = context;
        mMovieData = movies;
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.movie_list_item;
        GridView v = (GridView) LayoutInflater.from(mContext).inflate(layoutIdForListItem,viewGroup,false);
        return new MovieViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie currentMovie = mMovieData.get(position);
        holder.mMoviename.setText(currentMovie.getMovieName());
        Picasso.with(mContext)
                .load(currentMovie.getMoviePoster())
                .fit()
                .error(R.mipmap.ic_launcher_round)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.mMoviePoster);

    }

    @Override
    public int getItemCount() {
        if(mMovieData == null ||mMovieData.size() == 0){ return -1;}
        return mMovieData.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        public CardView mParentlayout;
        public ImageView mMoviePoster;
        public TextView mMoviename;

        public MovieViewHolder(View itemView){
            super(itemView);
            mParentlayout = itemView.findViewById(R.id.movie_cv);
            mMoviename = itemView.findViewById(R.id.movie_name);
            mMoviePoster = itemView.findViewById(R.id.movie_poster);

        }
    }

}
