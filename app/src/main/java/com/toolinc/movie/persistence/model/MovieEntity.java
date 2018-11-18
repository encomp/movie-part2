package com.toolinc.movie.persistence.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "movieTable")
public class MovieEntity {
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "movieId")
  private String id;

  @NonNull
  @ColumnInfo(name = "title")
  private String title;

  @NonNull
  @ColumnInfo(name = "posterPath")
  private String posterPath;

  public MovieEntity(String id, String title, String posterPath) {
    this.id = id;
    this.title = title;
    this.posterPath = posterPath;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getPosterPath() {
    return posterPath;
  }
}
