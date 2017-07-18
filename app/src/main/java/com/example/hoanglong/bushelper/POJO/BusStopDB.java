package com.example.hoanglong.bushelper.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hoanglong on 10-Jul-17.
 */

public class BusStopDB {
    @SerializedName("Beacon_BusStop")
    private List<BeaconBusStop> beaconList;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("BusInfo_BusStop")
    private List<BusInfoBusStop> busInfoBusStops;


    public List<BusInfoBusStop> getBusInfoBusStops() {
        return busInfoBusStops;
    }

    public void setBusInfoBusStops(List<BusInfoBusStop> busInfoBusStops) {
        this.busInfoBusStops = busInfoBusStops;
    }

    public List<BeaconBusStop> getBeaconList() {
        return beaconList;
    }

    public void setBeaconList(List<BeaconBusStop> beaconList) {
        this.beaconList = beaconList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
