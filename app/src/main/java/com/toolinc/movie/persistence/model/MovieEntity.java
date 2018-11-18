package com.toolinc.movie.persistence.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.toolinc.movie.client.model.Movie;
import com.toolinc.movie.model.MovieModel;

/** Specifes the {@link MovieModel} entity that will be store on SQLite. */
@Entity(tableName = "movieTable")
public class MovieEntity implements MovieModel {
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "movieId")
  private String id;

  @ColumnInfo(name = "title")
  private String originalTitle;

  @ColumnInfo(name = "posterPath")
  private String posterPath;

  @ColumnInfo(name = "overview")
  private String overview;

  @ColumnInfo(name = "voteAverage")
  private String voteAverage;

  @ColumnInfo(name = "releaseDate")
  private String releaseDate;

  public MovieEntity(
      String id,
      String originalTitle,
      String posterPath,
      String overview,
      String voteAverage,
      String releaseDate) {
    this.id = id;
    this.originalTitle = originalTitle;
    this.posterPath = posterPath;
    this.overview = overview;
    this.voteAverage = voteAverage;
    this.releaseDate = releaseDate;
  }

  @Ignore
  public MovieEntity(Movie movie) {
    this(
        movie.id(),
        movie.originalTitle(),
        movie.posterPath(),
        movie.overview(),
        movie.voteAverage(),
        movie.releaseDate());
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public String originalTitle() {
    return originalTitle;
  }

  @Override
  public String posterPath() {
    return posterPath;
  }

  @Override
  public String overview() {
    return overview;
  }

  @Override
  public String voteAverage() {
    return voteAverage;
  }

  @Override
  public String releaseDate() {
    return null;
  }
}
