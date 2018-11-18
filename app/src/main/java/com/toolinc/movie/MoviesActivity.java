package com.toolinc.movie;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.toolinc.movie.client.MovieClient;
import com.toolinc.movie.client.model.Movies;
import com.toolinc.movie.model.MovieModel;
import com.toolinc.movie.persistence.AllMoviesViewModel;
import com.toolinc.movie.persistence.model.MovieEntity;
import com.toolinc.movie.widget.MovieAdapter;

import java.io.Serializable;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
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
        Callback<Movies>,
        Observer<List<MovieEntity>> {
  private static final ImmutableList<MovieModel> EMPTY = ImmutableList.copyOf(Lists.newArrayList());

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.drawer_layout)
  DrawerLayout drawerLayout;

  @BindView(R.id.nav_view)
  NavigationView navigationView;

  @BindView(R.id.pb_loading_indicator)
  ProgressBar progressBar;

  @BindView(R.id.recyclerview_movies)
  RecyclerView recyclerView;

  @BindString(R.string.popular_movies)
  String popularLabel;

  @BindString(R.string.top_movies)
  String topMoviesLabel;

  private ImmutableList<MovieModel> movieModelsDb;
  private ImmutableList<MovieModel> movieModelsRest;

  private String currentMoviesMenu;
  private boolean favorites;
  private AllMoviesViewModel allMoviesViewModel;

  private final MovieAdapter moviesAdapter = new MovieAdapter(this);
  private Call<Movies> moviesCall;

  private static int calculateNoOfColumns(Context context) {
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    int scalingFactor = 200;
    int noOfColumns = (int) (dpWidth / scalingFactor);
    if (noOfColumns < 2) noOfColumns = 2;
    return noOfColumns;
  }

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_movies);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    drawerLayout.addDrawerListener(toggle);
    toggle.syncState();
    navigationView.setNavigationItemSelectedListener(this);

    GridLayoutManager layoutManager =
        new GridLayoutManager(this, calculateNoOfColumns(getApplicationContext()));
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setHasFixedSize(true);

    if (isMovieStateAvailable(bundle)) {
      setFromState(bundle);
    } else {
      fetchMovies(MovieClient.create().popular());
    }
    allMoviesViewModel = ViewModelProviders.of(this).get(AllMoviesViewModel.class);
    allMoviesViewModel.getAllMovies().observe(this, this::onChanged);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (Optional.fromNullable(moviesCall).isPresent()) {
      moviesCall.cancel();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);
    MoviesState moviesState =
        MoviesState.builder()
            .setMovieModels(moviesAdapter.getMovies())
            .setMovieModelsDb(movieModelsDb)
            .setMovieModelsRest(movieModelsRest)
            .setPopular(popularLabel, currentMoviesMenu)
            .setFavorites(favorites)
            .build();
    bundle.putSerializable(MoviesState.MOVIES_STATE, moviesState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle bundle) {
    super.onRestoreInstanceState(bundle);
    setFromState(bundle);
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
    switch (item.getItemId()) {
      case R.id.mi_movie:
        String title = item.getTitle().toString();
        Call<Movies> call = null;
        if (title.equals(popularLabel)) {
          call = MovieClient.create().popular();
          currentMoviesMenu = topMoviesLabel;
        } else {
          call = MovieClient.create().topRated();
          currentMoviesMenu = popularLabel;
        }
        item.setTitle(currentMoviesMenu);
        fetchMovies(call);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.nav_movies:
        favorites = false;
        refreshRecycleView(movieModelsRest);
        break;

      case R.id.nav_favorites:
        favorites = true;
        refreshRecycleView(movieModelsDb);
        break;
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

  @Override
  public void onResponse(Call<Movies> call, Response<Movies> response) {
    progressBar.setVisibility(View.INVISIBLE);
    movieModelsRest = response.body().movies();
    if (!favorites) {
      refreshRecycleView(movieModelsRest);
    }
  }

  @Override
  public void onFailure(Call<Movies> call, Throwable t) {
    recyclerView.setVisibility(View.INVISIBLE);
    Snackbar.make(recyclerView, getString(R.string.loading_movies_error_msg), Snackbar.LENGTH_LONG)
        .show();
  }

  @Override
  public void onChanged(@Nullable List<MovieEntity> movieEntities) {
    if (Optional.fromNullable(movieEntities).isPresent()) {
      movieModelsDb = ImmutableList.copyOf(movieEntities);
    } else {
      movieModelsDb = EMPTY;
    }
    if (favorites) {
      refreshRecycleView(movieModelsDb);
    }
  }

  private void setFromState(Bundle bundle) {
    if (isMovieStateAvailable(bundle)) {
      MoviesState moviesState = (MoviesState) bundle.getSerializable(MoviesState.MOVIES_STATE);
      favorites = moviesState.favorites();
      if (moviesState.popular()) {
        currentMoviesMenu = topMoviesLabel;
      } else {
        currentMoviesMenu = popularLabel;
      }
      movieModelsDb = moviesState.movieModelsDb();
      movieModelsRest = moviesState.movieModelsRest();
      refreshRecycleView(moviesState.movieModels());
    }
  }

  private void fetchMovies(Call<Movies> call) {
    if (Optional.fromNullable(moviesCall).isPresent()) {
      moviesCall.cancel();
    }
    moviesCall = call;
    Snackbar.make(recyclerView, getString(R.string.loading_movies_msg), Snackbar.LENGTH_SHORT)
        .show();
    progressBar.setVisibility(View.VISIBLE);
    moviesCall.enqueue(this);
  }

  private void refreshRecycleView(@Nullable ImmutableList<MovieModel> movieModels) {
    ImmutableList<MovieModel> tmpMovieModels = null;
    if (Optional.fromNullable(movieModels).isPresent()) {
      tmpMovieModels = movieModels;
    } else {
      tmpMovieModels = EMPTY;
    }
    moviesAdapter.setMovies(tmpMovieModels);
    recyclerView.setAdapter(moviesAdapter);
    recyclerView.setVisibility(View.VISIBLE);
  }

  private static final boolean isMovieStateAvailable(Bundle bundle) {
    return Optional.fromNullable(bundle).isPresent()
        && Optional.fromNullable((MoviesState) bundle.getSerializable(MoviesState.MOVIES_STATE))
            .isPresent();
  }

  /** Defines the state that needs to be kept for saving and restoring the state. */
  @AutoValue
  abstract static class MoviesState implements Serializable {
    private static final String MOVIES_STATE = "MOVIES_ACTIVITY_STATE";

    abstract ImmutableList<MovieModel> movieModels();

    abstract ImmutableList<MovieModel> movieModelsDb();

    abstract ImmutableList<MovieModel> movieModelsRest();

    abstract boolean popular();

    abstract boolean favorites();

    static final Builder builder() {
      return new AutoValue_MoviesActivity_MoviesState.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {

      abstract boolean popular();

      abstract Builder setMovieModels(ImmutableList<MovieModel> movieModels);

      abstract Builder setMovieModelsDb(ImmutableList<MovieModel> movieModelsDb);

      abstract Builder setMovieModelsRest(ImmutableList<MovieModel> movieModelsRest);

      abstract Builder setPopular(boolean popular);

      abstract Builder setFavorites(boolean favorites);

      Builder setPopular(String popularMoviesLabel, String currentMoviesMenu) {
        setPopular(popularMoviesLabel.equals(currentMoviesMenu));
        return this;
      }

      abstract MoviesState build();
    }
  }
}
