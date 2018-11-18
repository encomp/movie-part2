package com.toolinc.movie.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;
import com.toolinc.movie.BuildConfig;
import com.toolinc.movie.databinding.MoviesListItemMovieBinding;
import com.toolinc.movie.model.MovieModel;

/**
 * MovieAdapter provides a binding from an {@link ImmutableList} of {@link MovieModel} to the view
 * {@code R.layout.movies_list_item_movie} displayed within a RecyclerView.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviesViewHolder> {

  private final OnMovieSelected onMovieSelected;
  private ImmutableList<MovieModel> movies = ImmutableList.copyOf(Lists.newArrayList());

  public MovieAdapter(OnMovieSelected onMovieSelected) {
    this.onMovieSelected =
        Preconditions.checkNotNull(onMovieSelected, "Missing the selection movie listener.");
  }

  @NonNull
  @Override
  public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    MoviesListItemMovieBinding movieBinding =
        MoviesListItemMovieBinding.inflate(inflater, viewGroup, false);
    return new MoviesViewHolder(movieBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull MoviesViewHolder moviesViewHolder, int position) {
    MovieModel movie = movies.get(position);
    Picasso.get()
        .load(String.format(BuildConfig.IMAGE_BASE_URL, movie.posterPath()))
        .into(moviesViewHolder.movieBinding.ivMoviePoster);
  }

  @Override
  public int getItemCount() {
    return movies.size();
  }

  public void setMovies(ImmutableList<MovieModel> movies) {
    if (Optional.fromNullable(movies).isPresent()) {
      this.movies = movies;
    } else {
      this.movies = ImmutableList.copyOf(Lists.newArrayList());
    }
  }

  public ImmutableList<MovieModel> getMovies() {
    return movies;
  }

  /** Specifies the behavior upon selection of a {@link MovieModel}. */
  public interface OnMovieSelected {

    /** Specifies the movie that has been selected by the user. */
    void onSelected(MovieModel movie);
  }

  /** Describes a movie item about its place within the RecyclerView. */
  public final class MoviesViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {

    private final MoviesListItemMovieBinding movieBinding;

    public MoviesViewHolder(MoviesListItemMovieBinding movieBinding) {
      super(movieBinding.getRoot());
      this.movieBinding =
          Preconditions.checkNotNull(movieBinding, "MoviesListItemMovieBinding is missing.");
      movieBinding.ivMoviePoster.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      onMovieSelected.onSelected(movies.get(getAdapterPosition()));
    }
  }
}
