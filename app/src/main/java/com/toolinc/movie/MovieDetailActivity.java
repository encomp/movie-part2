package com.toolinc.movie;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.squareup.picasso.Picasso;
import com.toolinc.movie.client.MovieClient;
import com.toolinc.movie.client.model.Movie;
import com.toolinc.movie.client.model.Reviews;
import com.toolinc.movie.client.model.Videos;
import com.toolinc.movie.persistence.MovieRepository;
import com.toolinc.movie.persistence.model.MovieEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Defines the behavior of the {@code R.layout.activity_movie_detail} that displays the detail
 * information of a specific {@link Movie}.
 */
public final class MovieDetailActivity extends AppCompatActivity {

  @BindView(R.id.fab_add)
  FloatingActionButton fabAdd;

  @BindView(R.id.fab_remove)
  FloatingActionButton fabRemove;

  @BindView(R.id.fab_review)
  FloatingActionButton fabReview;

  @BindView(R.id.fab_trailer)
  FloatingActionButton fabTrailer;

  @BindView(R.id.iv_movie_poster)
  ImageView ivPoster;

  @BindView(R.id.tv_movie_title)
  TextView tvMovieTitle;

  @BindView(R.id.tv_movie_overview)
  TextView tvMovieOverview;

  @BindView(R.id.tv_vote_average)
  RatingBar tvVoteAverage;

  @BindView(R.id.tv_release_date)
  TextView tvReleaseDate;

  private Movie movie;
  private MovieRepository movieRepository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    // Bind the UI elements
    ButterKnife.bind(this);
    // Create a new Repository
    movieRepository = MovieRepository.create(getApplication());

    if (getIntent().hasExtra(Intent.EXTRA_KEY_EVENT)) {
      movie = (Movie) getIntent().getSerializableExtra(Intent.EXTRA_KEY_EVENT);
      displayMovieDetailInfo(movie);
      fetchReviews(MovieClient.create().reviews(movie.id()));
      fetchTrailers(MovieClient.create().videos(movie.id()));
      movieRepository
          .findById(movie.id())
          .observe(
              this,
              new Observer<MovieEntity>() {
                @Override
                public void onChanged(@Nullable MovieEntity movieEntity) {
                  Optional<MovieEntity> optionalMovie = Optional.fromNullable(movieEntity);
                  if (optionalMovie.isPresent()) {
                    fabAdd.hide();
                    fabRemove.show();
                  } else {
                    fabAdd.show();
                    fabRemove.hide();
                  }
                }
              });
    }
    initListeners();
  }

  private void initListeners() {
    fabAdd.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String msg = String.format(getString(R.string.insert_movie_msg), movie.originalTitle());
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            movieRepository.insert(new MovieEntity(movie));
            MovieDetailActivity.this.finish();
          }
        });
    fabRemove.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String msg = String.format(getString(R.string.delete_movie_msg), movie.originalTitle());
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            movieRepository.delete(new MovieEntity(movie));
            MovieDetailActivity.this.finish();
          }
        });
    fabTrailer.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(MovieDetailActivity.this, TrailersActivity.class);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, movie);
            startActivity(intent);
          }
        });
    fabReview.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(MovieDetailActivity.this, ReviewsActivity.class);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, movie);
            startActivity(intent);
          }
        });
  }

  private void displayMovieDetailInfo(Movie movie) {
    Picasso.get()
        .load(String.format(BuildConfig.IMAGE_BASE_URL, movie.posterPath()))
        .into(ivPoster);
    tvMovieTitle.setText(movie.originalTitle());
    tvMovieOverview.setText(movie.overview());
    tvVoteAverage.setRating(Float.valueOf(movie.voteAverage()) / 2);
    tvReleaseDate.setText(movie.releaseDate());
  }

  private void fetchReviews(Call<Reviews> call) {
    call.enqueue(
        new Callback<Reviews>() {

          @Override
          public void onResponse(Call<Reviews> call, Response<Reviews> response) {
            if (response.body().totalResults() > 0) {
              fabReview.show();
            } else {
              fabReview.hide();
            }
          }

          @Override
          public void onFailure(Call<Reviews> call, Throwable t) {
            fabReview.hide();
            Snackbar.make(
                    fabReview, getString(R.string.loading_reviews_error_msg), Snackbar.LENGTH_SHORT)
                .show();
          }
        });
  }

  private void fetchTrailers(Call<Videos> call) {
    call.enqueue(
        new Callback<Videos>() {

          @Override
          public void onResponse(Call<Videos> call, Response<Videos> response) {
            if (response.body().videos().size() > 0) {
              fabTrailer.show();
            } else {
              fabTrailer.hide();
            }
          }

          @Override
          public void onFailure(Call<Videos> call, Throwable t) {
            fabTrailer.hide();
            Snackbar.make(
                    fabTrailer,
                    getString(R.string.loading_trailers_error_msg),
                    Snackbar.LENGTH_SHORT)
                .show();
          }
        });
  }
}
