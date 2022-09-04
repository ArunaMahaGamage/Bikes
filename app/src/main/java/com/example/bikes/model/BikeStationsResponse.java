package com.example.bikes.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BikeStationsResponse {

    @SerializedName("features")
    public List<Features> features;

    @SerializedName("crs")
    public CRS crs;

    @SerializedName("type")
    public String type;
}
