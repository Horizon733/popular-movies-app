package com.example.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Reviews;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewAdapter> {
    List<Reviews> mReviewsData;
    Context mContext;

    public ReviewsAdapter(Context context, List<Reviews> reviews) {
        mContext = context;
        mReviewsData = reviews;
    }

    @NonNull
    @Override
    public ReviewsViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.reviews_list_item;
        View v = LayoutInflater.from(mContext).inflate(layoutIdForListItem, parent, false);
        return new ReviewsAdapter.ReviewsViewAdapter(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewsViewAdapter holder, int position) {
        final Reviews currentReviews = mReviewsData.get(position);
        holder.mAuthor.setText(currentReviews.getauthor());
        holder.mContent.setText(currentReviews.getcontent());
        holder.mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri link = Uri.parse(currentReviews.getReviewLink());
                Intent startReview = new Intent(Intent.ACTION_VIEW, link);
                mContext.startActivity(startReview);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mReviewsData == null || mReviewsData.size() == 0) {
            return -1;
        }
        return mReviewsData.size();
    }

    public class ReviewsViewAdapter extends RecyclerView.ViewHolder {
        public TextView mAuthor;
        public TextView mContent;

        public ReviewsViewAdapter(@NonNull View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.author);
            mContent = itemView.findViewById(R.id.content);
        }
    }
}
