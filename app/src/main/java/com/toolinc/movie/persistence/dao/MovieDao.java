package com.toolinc.movie.persistence.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.toolinc.movie.persistence.model.MovieEntity;

import java.io.Serializable;
import java.util.List;

/** Specifies the data access object behavior for the {@link MovieEntity}. */
@Dao
public interface MovieDao extends Serializable {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(MovieEntity movieEntity);

  @Delete
  void delete(MovieEntity movieEntity);

  @Query("DELETE FROM movieTable")
  void deleteAll();

  @Query("SELECT * FROM movieTable WHERE movieId = :id")
  LiveData<MovieEntity> findById(String id);

  @Query("SELECT * from movieTable ORDER BY movieId ASC")
  LiveData<List<MovieEntity>> getAll();

  public enum Operation implements Serializable {
    Insert,
    Delete
  }
}
