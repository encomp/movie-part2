package com.toolinc.movie.client;

import com.google.gson.GsonBuilder;
import com.toolinc.movie.BuildConfig;
import com.toolinc.movie.model.Movies;
import com.toolinc.movie.model.Reviews;
import com.toolinc.movie.model.Videos;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Specifies the contract of the information that will be retrieve from the themoviedb api
 */
public interface MovieClient {
    static final String API_KEY = "api_key";
    static final GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(Movies.class, Movies.builder())
            .registerTypeAdapter(Reviews.class, Reviews.builder())
            .registerTypeAdapter(Videos.class, Videos.builder());
    static final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder()
                        .addQueryParameter(API_KEY, BuildConfig.API_KEY).build();
                request = request.newBuilder().url(url).get().build();
                Response response = chain.proceed(request);
                return response;
            });
    static final Retrofit retrofitClient = new Retrofit.Builder()
            .baseUrl(BuildConfig.END_POINT)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .client(okHttpClientBuilder.build())
            .build();

    @GET("movie/top_rated")
    Call<Movies> topRated();

    @GET("movie/popular")
    Call<Movies> popular();

    @GET("movie/{movie_id}/reviews")
    Call<Reviews> reviews(@Path("movie_id") String movieId);

    @GET("movie/{movie_id}/videos")
    Call<Videos> videos(@Path("movie_id") String movieId);

    static MovieClient create() {
        return retrofitClient.create(MovieClient.class);
    }
}
