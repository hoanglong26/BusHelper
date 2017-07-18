package com.example.hoanglong.bushelper.POJO.googlemap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hoanglong on 27-Jun-17.
 */

public class MapLocation {
    @SerializedName("lat")
    @Expose
    private String latitude;

    @SerializedName("lng")
    @Expose
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
