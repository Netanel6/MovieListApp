package com.netanel.movielistapp.utils;

import com.netanel.movielistapp.pojo.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

//API interface with the method annonation to get all movies list from movies.json
public interface JsonPlaceHolderApi {

    @GET("movies.json")
    Call<List<Movie>> getMovies();
}
