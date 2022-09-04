package com.example.bikes.model.bikestations;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Geometry {
    @SerializedName("coordinates")
    public List<String> coordinates;

    @SerializedName("type")
    public String type;
}
