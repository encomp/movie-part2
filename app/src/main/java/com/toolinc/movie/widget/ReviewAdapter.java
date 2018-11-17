package com.toolinc.movie.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.toolinc.movie.R;
import com.toolinc.movie.model.Review;

/**
 * ReviewAdapter provides a binding from an {@link ImmutableList} of {@link Review} to the view
 * {@code R.layout.reviews_list_item_review} displayed within a RecyclerView.
 */
public final class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewwHolder> {

    private final ImmutableList<Review> reviews;

    public ReviewAdapter(ImmutableList<Review> reviews) {
        this.reviews = Preconditions.checkNotNull(reviews, "Reviews are missing.");
    }

    @NonNull
    @Override
    public ReviewwHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.reviews_list_item_review, viewGroup, false);
        return new ReviewwHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewwHolder reviewwHolder, int i) {
        Review review = reviews.get(i);
        reviewwHolder.tvAuthor.setText(review.author());
        reviewwHolder.tvContent.setText(review.content());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    /**
     * Describes a review item about its place within the RecyclerView.
     */
    public final class ReviewwHolder extends RecyclerView.ViewHolder {

        private final TextView tvAuthor;
        private final TextView tvContent;

        public ReviewwHolder(View view) {
            super(view);
            tvAuthor = (TextView) view.findViewById(R.id.tv_author);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
