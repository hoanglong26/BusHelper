package com.example.hoanglong.bushelper.POJO.googlemap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hoanglong on 27-Jun-17.
 */

public class Line {
    @SerializedName("name")
    @Expose
    private String busName;

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }
}
