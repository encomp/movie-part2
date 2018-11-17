package com.toolinc.movie.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.squareup.picasso.Picasso;
import com.toolinc.movie.BuildConfig;
import com.toolinc.movie.R;
import com.toolinc.movie.model.Movie;

/**
 * MovieAdapter provides a binding from an {@link ImmutableList} of {@link Movie} to the view
 * {@code R.layout.movies_list_item_movie} displayed within a RecyclerView.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviesViewHolder> {

    private final ImmutableList<Movie> movies;
    private final OnMovieSelected onMovieSelected;

    public MovieAdapter(ImmutableList<Movie> movies, OnMovieSelected onMovieSelected) {
        this.movies = Preconditions.checkNotNull(movies, "Movies are missing.");
        this.onMovieSelected = Preconditions.checkNotNull(onMovieSelected, "Missing the selection movie listener.");
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.movies_list_item_movie, viewGroup, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder moviesViewHolder, int position) {
        Movie movie = movies.get(position);
        Picasso.get()
                .load(String.format(BuildConfig.IMAGE_BASE_URL, movie.posterPath()))
                .into(moviesViewHolder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    /**
     * Describes a movie item about its place within the RecyclerView.
     */
    public final class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivPoster;

        public MoviesViewHolder(View view) {
            super(view);
            ivPoster = (ImageView) view.findViewById(R.id.iv_movie_poster);
            ivPoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieSelected.onSelected(movies.get(getAdapterPosition()));
        }
    }

    /**
     * Specifies the behavior upon selection of a {@link Movie}.
     */
    public interface OnMovieSelected {

        /**
         * Specifies the movie that has been selected by the user.
         */
        void onSelected(Movie movie);
    }
}
