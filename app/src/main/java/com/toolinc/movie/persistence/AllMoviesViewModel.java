package com.toolinc.movie.persistence;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.toolinc.movie.persistence.model.MovieEntity;

import java.util.List;

/** AllMoviesViewModel responsibility is to manage the data for the UI. */
public final class AllMoviesViewModel extends AndroidViewModel {

  private final MovieRepository movieRepository;

  public AllMoviesViewModel(Application application) {
    super(application);
    movieRepository = new MovieRepository(application);
  }

  public LiveData<List<MovieEntity>> getAllMovies() {
    return movieRepository.getAllMovies();
  }

  public void insert(MovieEntity movieEntity) {
    movieRepository.insert(movieEntity);
  }

  public void delete(MovieEntity movieEntity) {
    movieRepository.delete(movieEntity);
  }

  public void deleteAll() {
    movieRepository.deleteAll();
  }
}
