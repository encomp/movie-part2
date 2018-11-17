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
public abstract class Movie implements Serializable {

    public abstract String id();

    public abstract String originalTitle();

    public abstract String posterPath();

    public abstract String overview();

    public abstract String voteAverage();

    public abstract String releaseDate();

    @NonNull
    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder extends TypeAdapter<Movie> {
        private static final Gson GSON = new Gson();

        @SerializedName("id")
        private String id;

        @SerializedName("original_title")
        private String originalTitle;

        @SerializedName("poster_path")
        private String posterPath;

        @SerializedName("overview")
        private String overview;

        @SerializedName("vote_average")
        private String voteAverage;

        @SerializedName("release_date")
        private String releaseDate;

        private Builder() {
        }

        public Movie build() {
            return new AutoValue_Movie(id, originalTitle, posterPath, overview, voteAverage,
                    releaseDate);
        }

        @Override
        public void write(JsonWriter out, Movie movie) throws IOException {
        }

        @Override
        public Movie read(JsonReader in) throws IOException {
            Builder builder = GSON.fromJson(in, Builder.class);
            return builder.build();
        }
    }
}
