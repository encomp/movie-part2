package com.toolinc.movie.model;

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
public abstract class Reviews implements Serializable {

    public abstract int id();

    public abstract int page();

    public abstract int totalPages();

    public abstract int totalResults();

    public abstract ImmutableList<Review> reviews();

    @NonNull
    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder extends TypeAdapter<Reviews> {
        private static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(Review.class, Review.builder())
                .create();

        @SerializedName("id")
        private int id;

        @SerializedName("page")
        private int page;

        @SerializedName("results")
        private List<Review> reviews;

        @SerializedName("total_pages")
        private int totalPages;

        @SerializedName("total_results")
        private int totalResults;

        private Builder() {
        }

        public Reviews build() {
            return new AutoValue_Reviews(id,
                    page,
                    totalPages,
                    totalResults,
                    ImmutableList.copyOf(reviews));
        }

        @Override
        public void write(JsonWriter out, Reviews value) throws IOException {
        }

        @Override
        public Reviews read(JsonReader in) throws IOException {
            Builder builder = GSON.fromJson(in, Builder.class);
            return builder.build();
        }
    }

}
