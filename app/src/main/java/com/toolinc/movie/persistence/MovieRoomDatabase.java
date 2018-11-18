package com.toolinc.movie.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.toolinc.movie.persistence.dao.MovieDao;
import com.toolinc.movie.persistence.model.MovieEntity;

/** MovieRoomDatabase provides direct access to the underlying database. */
@Database(
    entities = {MovieEntity.class},
    exportSchema = false,
    version = 1)
abstract class MovieRoomDatabase extends RoomDatabase {

  private static volatile MovieRoomDatabase INSTANCE;

  static MovieRoomDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (MovieRoomDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE =
              Room.databaseBuilder(
                      context.getApplicationContext(), MovieRoomDatabase.class, "movie_database")
                  .build();
        }
      }
    }
    return INSTANCE;
  }

  public abstract MovieDao movieDao();
}
