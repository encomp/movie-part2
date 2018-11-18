package com.toolinc.movie.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.toolinc.movie.databinding.ReviewsListItemReviewBinding;
import com.toolinc.movie.client.model.Review;

/**
 * ReviewAdapter provides a binding from an {@link ImmutableList} of {@link Review} to the view
 * {@code R.layout.reviews_list_item_review} displayed within a RecyclerView.
 */
public final class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

  private final ImmutableList<Review> reviews;

  public ReviewAdapter(ImmutableList<Review> reviews) {
    this.reviews = Preconditions.checkNotNull(reviews, "Reviews are missing.");
  }

  @NonNull
  @Override
  public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
    ReviewsListItemReviewBinding reviewBinding =
        ReviewsListItemReviewBinding.inflate(layoutInflater, viewGroup, false);
    return new ReviewHolder(reviewBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i) {
    reviewHolder.bind(reviews.get(i));
  }

  @Override
  public int getItemCount() {
    return reviews.size();
  }

  /** Describes a review item about its place within the RecyclerView. */
  public final class ReviewHolder extends RecyclerView.ViewHolder {

    private final ReviewsListItemReviewBinding reviewBinding;

    public ReviewHolder(ReviewsListItemReviewBinding reviewBinding) {
      super(reviewBinding.getRoot());
      this.reviewBinding =
          Preconditions.checkNotNull(reviewBinding, "ReviewsListItemReviewBinding is missing.");
    }

    void bind(Review review) {
      reviewBinding.setReview(review);
    }
  }
}
