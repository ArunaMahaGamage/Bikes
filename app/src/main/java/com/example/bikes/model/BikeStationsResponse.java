package com.example.bikes.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BikeStationsResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("totalResults")
    public String totalResults;

    /*@SerializedName("articles")
    public List<Articles> articles;*/
}
