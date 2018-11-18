package com.toolinc.movie.model;

import java.io.Serializable;

public interface MovieModel extends Serializable {

  String id();

  String originalTitle();

  String posterPath();

  String overview();

  String voteAverage();

  String releaseDate();
}
