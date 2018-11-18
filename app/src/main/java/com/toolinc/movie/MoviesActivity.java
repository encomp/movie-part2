package com.toolinc.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.toolinc.movie.client.MovieClient;
import com.toolinc.movie.client.model.Movies;
import com.toolinc.movie.model.MovieModel;
import com.toolinc.movie.widget.MovieAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Defines the behavior of the {@code R.layout.activity_movies} that display the first page of the
 * themoviedb api.
 */
public final class MoviesActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
        MovieAdapter.OnMovieSelected,
        Callback<Movies> {

  private ProgressBar progressBar;
  private RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movies);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    recyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setHasFixedSize(true);
    fetchMovies(MovieClient.create().popular());
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.movies_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (R.id.mi_movie == item.getItemId()) {
      String title = item.getTitle().toString();
      Call<Movies> call = null;
      if (title.equals(getString(R.string.popular_movies))) {
        call = MovieClient.create().popular();
        item.setTitle(R.string.top_movies);
      } else {
        call = MovieClient.create().topRated();
        item.setTitle(R.string.popular_movies);
      }
      fetchMovies(call);
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.nav_favorites) {
      Intent intent = new Intent(this, FavoritesAcivity.class);
      startActivity(intent);
    }
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onSelected(MovieModel movie) {
    Intent intent = new Intent(this, MovieDetailActivity.class);
    intent.putExtra(Intent.EXTRA_KEY_EVENT, movie);
    startActivity(intent);
  }

  private void fetchMovies(Call<Movies> call) {
    Snackbar.make(recyclerView, getString(R.string.loading_movies_msg), Snackbar.LENGTH_SHORT)
        .show();
    progressBar.setVisibility(View.VISIBLE);
    call.enqueue(this);
  }

  @Override
  public void onResponse(Call<Movies> call, Response<Movies> response) {
    progressBar.setVisibility(View.INVISIBLE);
    Movies movies = response.body();
    MovieAdapter moviesAdapter = new MovieAdapter(movies.movies(), MoviesActivity.this);
    recyclerView.setAdapter(moviesAdapter);
    recyclerView.setVisibility(View.VISIBLE);
  }

  @Override
  public void onFailure(Call<Movies> call, Throwable t) {
    recyclerView.setVisibility(View.INVISIBLE);
    Snackbar.make(recyclerView, getString(R.string.loading_movies_error_msg), Snackbar.LENGTH_LONG)
        .show();
  }
}
