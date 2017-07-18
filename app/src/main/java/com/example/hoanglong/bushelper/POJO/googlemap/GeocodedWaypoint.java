package com.example.hoanglong.bushelper.POJO.googlemap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoanglong on 28-Jun-17.
 */

public class GeocodedWaypoint {
    @SerializedName("place_id")
    @Expose
    private String place_id;

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
