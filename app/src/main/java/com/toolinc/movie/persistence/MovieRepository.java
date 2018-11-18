package com.toolinc.movie.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.toolinc.movie.executor.AppExecutors;
import com.toolinc.movie.persistence.dao.MovieDao;
import com.toolinc.movie.persistence.model.MovieEntity;

import java.util.List;

/**
 * MovieRepository hides the async thread execution for insert, delete and retrieval of infomration.
 */
public final class MovieRepository {

  private final MovieDao movieDao;

  MovieRepository(Application application) {
    movieDao = MovieRoomDatabase.getDatabase(application).movieDao();
  }

  public static MovieRepository create(Application application) {
    return new MovieRepository(application);
  }

  LiveData<List<MovieEntity>> getAllMovies() {
    return movieDao.getAll();
  }

  public LiveData<MovieEntity> findById(String id) {
    return movieDao.findById(id);
  }

  public void insert(MovieEntity movieEntity) {
    AppExecutors.getInstance().diskIO().execute(() -> movieDao.insert(movieEntity));
  }

  public void delete(MovieEntity movieEntity) {
    AppExecutors.getInstance().diskIO().execute(() -> movieDao.delete(movieEntity));
  }

  public void deleteAll() {
    AppExecutors.getInstance().diskIO().execute(() -> movieDao.deleteAll());
  }
}
