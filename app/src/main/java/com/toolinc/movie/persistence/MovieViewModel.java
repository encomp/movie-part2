package com.toolinc.movie.persistence;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.toolinc.movie.persistence.model.MovieEntity;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

  private MovieRepository movieRepository;

  private LiveData<List<MovieEntity>> allMovies;

  public MovieViewModel(Application application) {
    super(application);
    movieRepository = new MovieRepository(application);
    allMovies = movieRepository.getAllWords();
  }

  LiveData<List<MovieEntity>> getAllWords() {
    return allMovies;
  }

  public void insert(MovieEntity word) {
    movieRepository.insert(word);
  }
}
