package com.example.hoanglong.bushelper.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hoanglong on 27-Jun-17.
 */

public class TransitDetail {

    @SerializedName("arrival_stop")
    @Expose
    private BusStop arrival_stop;


    @SerializedName("departure_stop")
    @Expose
    private BusStop departure_stop;

    @SerializedName("line")
    @Expose
    private Line line;

    public BusStop getArrival_stop() {
        return arrival_stop;
    }

    public void setArrival_stop(BusStop arrival_stop) {
        this.arrival_stop = arrival_stop;
    }

    public BusStop getDeparture_stop() {
        return departure_stop;
    }

    public void setDeparture_stop(BusStop departure_stop) {
        this.departure_stop = departure_stop;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
