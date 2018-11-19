package com.toolinc.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.toolinc.movie.client.MovieClient;
import com.toolinc.movie.client.model.Video;
import com.toolinc.movie.client.model.Videos;
import com.toolinc.movie.model.MovieModel;
import com.toolinc.movie.widget.TrailerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Defines the behavior of the {@code R.layout.activity_trailers} to display all the trailers
 * available for a given movie.
 */
public final class TrailersActivity extends AppCompatActivity
    implements TrailerAdapter.OnVideoSelected, Callback<Videos> {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.backdrop)
  ImageView ivPoster;

  @BindView(R.id.pb_loading_indicator)
  ProgressBar progressBar;

  @BindView(R.id.recyclerview_reviews)
  RecyclerView recyclerView;

  private MovieModel movie;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trailers);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setHasFixedSize(true);

    if (getIntent().hasExtra(Intent.EXTRA_KEY_EVENT)) {
      movie = (MovieModel) getIntent().getSerializableExtra(Intent.EXTRA_KEY_EVENT);
      Picasso.get()
          .load(String.format(BuildConfig.IMAGE_BASE_URL, movie.posterPath()))
          .into(ivPoster);
      fetchTrailers(MovieClient.create().videos(movie.id()));
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

  private void fetchTrailers(Call<Videos> call) {
    Snackbar.make(recyclerView, getString(R.string.loading_trailers_msg), Snackbar.LENGTH_SHORT)
        .show();
    progressBar.setVisibility(View.VISIBLE);
    call.enqueue(this);
  }

  @Override
  public void onResponse(Call<Videos> call, Response<Videos> response) {
    progressBar.setVisibility(View.INVISIBLE);
    Videos videos = response.body();
    TrailerAdapter trailerAdapter = new TrailerAdapter(videos.videos(), this);
    recyclerView.setAdapter(trailerAdapter);
    recyclerView.setVisibility(View.VISIBLE);
  }

  @Override
  public void onFailure(Call<Videos> call, Throwable t) {
    recyclerView.setVisibility(View.INVISIBLE);
    Snackbar.make(
            recyclerView, getString(R.string.loading_trailers_error_msg), Snackbar.LENGTH_LONG)
        .show();
  }

  @Override
  public void onSelected(Video video) {
    Intent intent = new Intent(this, VideoActivity.class);
    intent.putExtra(Intent.EXTRA_KEY_EVENT, video);
    startActivity(intent);
  }
}
