package com.example.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.YoutubeTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapater extends RecyclerView.Adapter<TrailerAdapater.TrailerViewAdapter> {
    List<YoutubeTrailer> mTrailerData;
    Context mContext;

    public TrailerAdapater(Context context, List<YoutubeTrailer> trailers) {
        mContext = context;
        mTrailerData = trailers;
    }

    @NonNull
    @Override
    public TrailerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.trailer_list_item;
        View v = LayoutInflater.from(mContext).inflate(layoutIdForListItem, parent, false);
        return new TrailerAdapater.TrailerViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewAdapter holder, int position) {
        final YoutubeTrailer currentTrailer = mTrailerData.get(position);
        holder.mTrailerTitle.setText(currentTrailer.getTrailerTitle());
        Picasso.with(mContext)
                .load(currentTrailer.getTrailerThumbnailLink())
                .fit()
                .error(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .into(holder.mThumbnail);
        holder.mThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri link = Uri.parse(currentTrailer.getTrailerLink());
                Intent startYoutube = new Intent(Intent.ACTION_VIEW, link);
                mContext.startActivity(startYoutube);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mTrailerData == null || mTrailerData.size() == 0) {
            return -1;
        }
        return mTrailerData.size();
    }

    public class TrailerViewAdapter extends RecyclerView.ViewHolder {
        public ImageView mThumbnail;
        public TextView mTrailerTitle;
        public ImageButton mYoutubePlay;

        public TrailerViewAdapter(@NonNull View itemView) {
            super(itemView);
            mThumbnail = itemView.findViewById(R.id.trailer_thumbnail);
            mTrailerTitle = itemView.findViewById(R.id.tv_trailer_title);
            mYoutubePlay = itemView.findViewById(R.id.youtube_play);
        }
    }
}
