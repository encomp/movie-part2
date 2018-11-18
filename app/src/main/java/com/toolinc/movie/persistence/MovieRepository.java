package com.toolinc.movie.persistence.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.toolinc.movie.persistence.MovieRoomDatabase;
import com.toolinc.movie.persistence.dao.MovieDao;
import com.toolinc.movie.persistence.model.MovieEntity;

import java.util.List;

public class MovieRepository {

  private MovieDao movieDao;
  private LiveData<List<MovieEntity>> allMovies;

  MovieRepository(Application application) {
    MovieRoomDatabase db = MovieRoomDatabase.getDatabase(application);
    movieDao = db.movieDao();
    allMovies = movieDao.getAllWords();
  }

  LiveData<List<MovieEntity>> getAllWords() {
    return allMovies;
  }

  public void insert(MovieEntity word) {
    new insertAsyncTask(movieDao).execute(word);
  }

  private static class insertAsyncTask extends AsyncTask<MovieEntity, Void, Void> {

    private MovieDao asyncMovieDao;

    insertAsyncTask(MovieDao dao) {
      asyncMovieDao = dao;
    }

    @Override
    protected Void doInBackground(final MovieEntity... params) {
      asyncMovieDao.insert(params[0]);
      return null;
    }
  }
}
