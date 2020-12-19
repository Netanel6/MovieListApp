package com.netanel.movielistapp.utils;

import com.netanel.movielistapp.pojo.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("movies.json")
    Call<List<Movie>> getMovies();
}
