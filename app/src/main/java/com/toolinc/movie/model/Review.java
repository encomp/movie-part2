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
public abstract class Review implements Serializable {

    public abstract String id();

    public abstract String author();

    public abstract String content();

    public abstract String url();

    @NonNull
    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder extends TypeAdapter<Review> {

        private static final Gson GSON = new Gson();

        @SerializedName("id")
        private String id;

        @SerializedName("author")
        private String author;

        @SerializedName("content")
        private String content;

        @SerializedName("url")
        private String url;

        private Builder() {
        }

        public Review build() {
            return new AutoValue_Review(id, author, content, url);
        }

        @Override
        public void write(JsonWriter out, Review value) throws IOException {
        }

        @Override
        public Review read(JsonReader in) throws IOException {
            Builder builder = GSON.fromJson(in, Builder.class);
            return builder.build();
        }
    }
}
