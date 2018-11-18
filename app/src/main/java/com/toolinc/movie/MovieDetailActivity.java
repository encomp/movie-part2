package com.toolinc.movie;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
import com.toolinc.movie.model.MovieModel;
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
  private final Callback<Reviews> reviewsCallback =
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
      };
  @BindView(R.id.fab_trailer)
  FloatingActionButton fabTrailer;
  private final Callback<Videos> videosCallback =
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
                  fabTrailer, getString(R.string.loading_trailers_error_msg), Snackbar.LENGTH_SHORT)
              .show();
        }
      };
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
  private MovieModel movieModel;
  private MovieRepository movieRepository;
  private Call<Reviews> reviewsCall;
  private Call<Videos> videosCall;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_movie_detail);
    // Bind the UI elements
    ButterKnife.bind(this);
    // Create a new Repository
    movieRepository = MovieRepository.create(getApplication());

    if (getIntent().hasExtra(Intent.EXTRA_KEY_EVENT)) {
      Optional<MovieModel> optional =
          Optional.fromNullable(
              (MovieModel) getIntent().getSerializableExtra(Intent.EXTRA_KEY_EVENT));
      initMovie(optional);
    } else {
      loadFromBundle(Optional.fromNullable(bundle));
    }
    initListeners();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Optional<Call<Reviews>> callReviewsOptional = Optional.fromNullable(reviewsCall);
    if (callReviewsOptional.isPresent()) {
      reviewsCall.cancel();
    }
    Optional<Call<Videos>> callVideosOptional = Optional.fromNullable(videosCall);
    if (callVideosOptional.isPresent()) {
      videosCall.cancel();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);
    bundle.putSerializable(BuildConfig.MOVIE_STATE, movieModel);
  }

  @Override
  protected void onRestoreInstanceState(Bundle bundle) {
    super.onRestoreInstanceState(bundle);
    loadFromBundle(Optional.fromNullable(bundle));
  }

  private void loadFromBundle(Optional<Bundle> bundleOptional) {
    if (bundleOptional.isPresent()) {
      Bundle bundle = bundleOptional.get();
      Optional<MovieModel> optional =
          Optional.fromNullable((MovieModel) bundle.getSerializable(BuildConfig.MOVIE_STATE));
      initMovie(optional);
    }
  }

  private void initMovie(Optional<MovieModel> movieModelOptional) {
    if (movieModelOptional.isPresent()) {
      movieModel = movieModelOptional.get();
      displayMovieDetailInfo(movieModel);
      reviewsCall = MovieClient.create().reviews(movieModel.id());
      videosCall = MovieClient.create().videos(movieModel.id());
      reviewsCall.enqueue(reviewsCallback);
      videosCall.enqueue(videosCallback);
      movieRepository
          .findById(movieModel.id())
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
  }

  private void initListeners() {
    fabAdd.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String msg =
                String.format(getString(R.string.insert_movie_msg), movieModel.originalTitle());
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            movieRepository.insert(new MovieEntity(movieModel));
            MovieDetailActivity.this.finish();
          }
        });
    fabRemove.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String msg =
                String.format(getString(R.string.delete_movie_msg), movieModel.originalTitle());
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            movieRepository.delete(new MovieEntity(movieModel));
            MovieDetailActivity.this.finish();
          }
        });
    fabTrailer.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(MovieDetailActivity.this, TrailersActivity.class);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, movieModel);
            startActivity(intent);
          }
        });
    fabReview.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(MovieDetailActivity.this, ReviewsActivity.class);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, movieModel);
            startActivity(intent);
          }
        });
  }

  private void displayMovieDetailInfo(MovieModel movie) {
    Picasso.get()
        .load(String.format(BuildConfig.IMAGE_BASE_URL, movie.posterPath()))
        .into(ivPoster);
    tvMovieTitle.setText(movie.originalTitle());
    tvMovieOverview.setText(movie.overview());
    tvVoteAverage.setRating(Float.valueOf(movie.voteAverage()) / 2);
    tvReleaseDate.setText(movie.releaseDate());
  }
}
