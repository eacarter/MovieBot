package com.appsolutions.moviebot;

import retrofit.Callback;
import retrofit.http.GET;

import retrofit.http.Query;

/**
 * Created by eric on 2/6/17.
 */
public interface MovieInterface {

    @GET("/?plot=short&r=json")
    void getFeed(@Query("t") String title, @Query("y") String year, Callback<MovieModel> response);

}
