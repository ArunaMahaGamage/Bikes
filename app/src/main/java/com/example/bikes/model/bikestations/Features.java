package com.example.bikes.model.bikestations;

import com.google.gson.annotations.SerializedName;

public class Features {
    @SerializedName("geometry")
    public Geometry geometry;

    @SerializedName("id")
    public String id;

    @SerializedName("type")
    public String type;

    @SerializedName("properties")
    public Properties properties;
}
