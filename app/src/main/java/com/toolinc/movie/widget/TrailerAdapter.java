package com.toolinc.movie.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.toolinc.movie.client.model.Video;
import com.toolinc.movie.databinding.TrailersListItemVideoBinding;

/**
 * TrailerAdapter provides a binding from an {@link ImmutableList} of {@link Video} to the view
 * {@code R.layout.trailers_list_item_video} displayed within a RecyclerView.
 */
public final class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

  private final ImmutableList<Video> videos;
  private final OnVideoSelected onVideoSelected;

  public TrailerAdapter(ImmutableList<Video> videos, OnVideoSelected onVideoSelected) {
    this.videos = Preconditions.checkNotNull(videos, "Videos are missing.");
    this.onVideoSelected =
        Preconditions.checkNotNull(onVideoSelected, "OnVideoSelected is missing.");
  }

  @NonNull
  @Override
  public TrailerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
    TrailersListItemVideoBinding videoBinding =
        TrailersListItemVideoBinding.inflate(layoutInflater, viewGroup, false);
    return new TrailerHolder(videoBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull TrailerHolder trailerHolder, int i) {
    trailerHolder.bind(videos.get(i));
  }

  @Override
  public int getItemCount() {
    return videos.size();
  }

  /** Specifies the behavior upon selection of a {@link Video}. */
  public interface OnVideoSelected {

    /** Specifies the movie that has been selected by the user. */
    void onSelected(Video video);
  }

  /** Describes a review item about its place within the RecyclerView. */
  public final class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TrailersListItemVideoBinding videoBinding;

    public TrailerHolder(TrailersListItemVideoBinding videoBinding) {
      super(videoBinding.getRoot());
      this.videoBinding =
          Preconditions.checkNotNull(videoBinding, "TrailersListItemVideoBinding is missing.");
      this.videoBinding.fabVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      onVideoSelected.onSelected(videos.get(getAdapterPosition()));
    }

    void bind(Video video) {
      videoBinding.setVideo(video);
    }
  }
}
