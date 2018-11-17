package com.toolinc.movie.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;

@AutoValue
public abstract class Video implements Serializable {

  @NonNull
  public static final Builder builder() {
    return new Builder();
  }

  public abstract String id();

  public abstract String key();

  public abstract String name();

  public abstract String site();

  public abstract String type();

  public static final class Builder extends TypeAdapter<Video> {

    private static final Gson GSON = new Gson();

    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("type")
    private String type;

    private Builder() {}

    public Video build() {
      return new AutoValue_Video(id, key, name, site, type);
    }

    @Override
    public void write(JsonWriter out, Video value) throws IOException {}

    @Override
    public Video read(JsonReader in) throws IOException {
      Builder builder = GSON.fromJson(in, Builder.class);
      return builder.build();
    }
  }
}
