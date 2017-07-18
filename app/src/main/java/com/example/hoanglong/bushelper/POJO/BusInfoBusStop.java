package com.example.hoanglong.bushelper.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoanglong on 12-Jul-17.
 */

public class BusInfoBusStop {
    @SerializedName("BusInfo")
    BusInfo busInfo;

    public BusInfo getBusInfo() {
        return busInfo;
    }

    public void setBusInfo(BusInfo busInfo) {
        this.busInfo = busInfo;
    }
}
