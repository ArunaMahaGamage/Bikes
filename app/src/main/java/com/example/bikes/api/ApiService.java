package com.example.bikes.api;


import com.example.bikes.model.BikeStationsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/mim/plan/map_service.html")
    Call<BikeStationsResponse> getBikeStations(
            @Query("mtype") String mtype, @Query("co") String co);

    @GET("/v2/top-headlines")
    Call<BikeStationsResponse> getTopHeadlineNews(
            @Query("country") String country, @Query("apiKey") String apiKey);
}
