package com.toolinc.movie;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.common.collect.ImmutableList;
import com.toolinc.movie.persistence.AllMoviesViewModel;
import com.toolinc.movie.persistence.MovieRepository;
import com.toolinc.movie.persistence.model.MovieEntity;
import com.toolinc.movie.widget.MovieAdapter;

import java.util.List;

import javax.annotation.Nullable;

public class FavoritesAcivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private MovieRepository movieRepository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites_acivity);

    recyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setHasFixedSize(true);
    movieRepository = MovieRepository.create(getApplication());

    AllMoviesViewModel allMoviesViewModel =
        ViewModelProviders.of(this).get(AllMoviesViewModel.class);
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.favorites_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_delete_all) {
      movieRepository.deleteAll();
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
