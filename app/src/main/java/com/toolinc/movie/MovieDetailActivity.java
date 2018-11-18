package com.toolinc.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.toolinc.movie.client.MovieClient;
import com.toolinc.movie.client.model.Movie;
import com.toolinc.movie.client.model.Reviews;
import com.toolinc.movie.client.model.Videos;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Defines the behavior of the {@code R.layout.activity_movie_detail} that displays the detail
 * information of a specific {@link Movie}.
 */
public final class MovieDetailActivity extends AppCompatActivity {

  private Movie movie;
  private FloatingActionButton fabFavorites;
  private FloatingActionButton fabReview;
  private FloatingActionButton fabTrailer;
  private ImageView ivPoster;
  private TextView tvMovieTitle;
  private TextView tvMovieOverview;
  private RatingBar tvVoteAverage;
  private TextView tvReleaseDate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);

    fabFavorites = (FloatingActionButton) findViewById(R.id.fab);
    fabReview = ((FloatingActionButton) findViewById(R.id.fab_review));
    fabTrailer = ((FloatingActionButton) findViewById(R.id.fab_trailer));
    ivPoster = (ImageView) findViewById(R.id.iv_movie_poster);
    tvMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
    tvMovieOverview = (TextView) findViewById(R.id.tv_movie_overview);
    tvVoteAverage = (RatingBar) findViewById(R.id.tv_vote_average);
    tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);

    if (getIntent().hasExtra(Intent.EXTRA_KEY_EVENT)) {
      movie = (Movie) getIntent().getSerializableExtra(Intent.EXTRA_KEY_EVENT);
      displayMovieDetailInfo(movie);
      fetchReviews(MovieClient.create().reviews(movie.id()));
      fetchTrailers(MovieClient.create().videos(movie.id()));
    }
    initListeners();
  }

  private void initListeners() {
    fabFavorites.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
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
