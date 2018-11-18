package com.toolinc.movie.client.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@AutoValue
public abstract class Movies implements Serializable {

  @NonNull
  public static final Builder builder() {
    return new Builder();
  }

  public abstract int page();

  public abstract ImmutableList<Movie> movies();

  public static final class Builder extends TypeAdapter<Movies> {
    private static final Gson GSON =
        new GsonBuilder().registerTypeAdapter(Movie.class, Movie.builder()).create();

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movie> movies;

    private Builder() {}

    public Movies build() {
      return new AutoValue_Movies(page, ImmutableList.copyOf(movies));
    }

    @Override
    public void write(JsonWriter out, Movies value) throws IOException {}

    @Override
    public Movies read(JsonReader in) throws IOException {
      Builder builder = GSON.fromJson(in, Builder.class);
      return builder.build();
    }
  }
}
