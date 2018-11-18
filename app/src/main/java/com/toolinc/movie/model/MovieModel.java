package com.toolinc.movie.model;

import java.io.Serializable;

/** Specifies the contract for the movie model. */
public interface MovieModel extends Serializable {

  String id();

  String originalTitle();

  String posterPath();

  String overview();

  String voteAverage();

  String releaseDate();
}
