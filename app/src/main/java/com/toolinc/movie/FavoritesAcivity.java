package com.toolinc.movie;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.common.collect.ImmutableList;
import com.toolinc.movie.persistence.AllMoviesViewModel;
import com.toolinc.movie.persistence.model.MovieEntity;
import com.toolinc.movie.widget.MovieAdapter;

import java.util.List;

import javax.annotation.Nullable;

public class FavoritesAcivity extends AppCompatActivity {

  private ProgressBar progressBar;
  private RecyclerView recyclerView;
  private MovieAdapter moviesAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites_acivity);

    progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    recyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setHasFixedSize(true);

    AllMoviesViewModel allMoviesViewModel = ViewModelProviders.of(this).get(AllMoviesViewModel.class);
    allMoviesViewModel
        .getAllMovies()
        .observe(
            this,
            new Observer<List<MovieEntity>>() {
              @Override
              public void onChanged(@Nullable final List<MovieEntity> words) {
                recyclerView.setAdapter(new MovieAdapter(ImmutableList.copyOf(words)));
                recyclerView.setVisibility(View.VISIBLE);
              }
            });
  }
}
