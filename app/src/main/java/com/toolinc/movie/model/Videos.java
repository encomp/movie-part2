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
public abstract class Videos implements Serializable {

    public abstract int id();

    public abstract ImmutableList<Video> videos();

    @NonNull
    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder extends TypeAdapter<Videos> {
        private static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(Video.class, Video.builder())
                .create();

        @SerializedName("id")
        private int id;

        @SerializedName("results")
        private List<Video> videos;

        private Builder() {
        }

        public Videos build() {
            return new AutoValue_Videos(id, ImmutableList.copyOf(videos));
        }

        @Override
        public void write(JsonWriter out, Videos value) throws IOException {
        }

        @Override
        public Videos read(JsonReader in) throws IOException {
            Builder builder = GSON.fromJson(in, Builder.class);
            return builder.build();
        }
    }
}
