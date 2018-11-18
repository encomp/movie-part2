package com.toolinc.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.toolinc.movie.client.MovieClient;
import com.toolinc.movie.client.model.Movie;
import com.toolinc.movie.client.model.Reviews;
import com.toolinc.movie.widget.ReviewAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Defines the behavior of the {@code R.layout.activity_reviews} to display all the reviews
 * available for a given movie.
 */
public class ReviewsActivity extends AppCompatActivity implements Callback<Reviews> {

  private Movie movie;
  private ImageView ivPoster;
  private ProgressBar progressBar;
  private RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reviews);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ivPoster = (ImageView) findViewById(R.id.backdrop);
    progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    recyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setHasFixedSize(true);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    if (getIntent().hasExtra(Intent.EXTRA_KEY_EVENT)) {
      movie = (Movie) getIntent().getSerializableExtra(Intent.EXTRA_KEY_EVENT);
      Picasso.get()
          .load(String.format(BuildConfig.IMAGE_BASE_URL, movie.posterPath()))
          .into(ivPoster);
      fetchReviews(MovieClient.create().reviews(movie.id()));
    }
  }

  private void fetchReviews(Call<Reviews> call) {
    Snackbar.make(recyclerView, getString(R.string.loading_reviews_msg), Snackbar.LENGTH_SHORT)
        .show();
    progressBar.setVisibility(View.VISIBLE);
    call.enqueue(this);
  }

  @Override
  public void onResponse(Call<Reviews> call, Response<Reviews> response) {
    progressBar.setVisibility(View.INVISIBLE);
    Reviews reviews = response.body();
    ReviewAdapter reviewAdapter = new ReviewAdapter(reviews.reviews());
    recyclerView.setAdapter(reviewAdapter);
    recyclerView.setVisibility(View.VISIBLE);
  }

  @Override
  public void onFailure(Call<Reviews> call, Throwable t) {
    recyclerView.setVisibility(View.INVISIBLE);
    Snackbar.make(recyclerView, getString(R.string.loading_reviews_error_msg), Snackbar.LENGTH_LONG)
        .show();
  }
}
