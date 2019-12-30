package com.engineeringaitest.api;

import com.engineeringaitest.model.APIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface for Retrofit
 */
public interface ApiInterface {

    @GET("users")
    Call<APIResponse> getUserImageList(
            @Query("offset") int offset,
            @Query("limit") int limit);
}