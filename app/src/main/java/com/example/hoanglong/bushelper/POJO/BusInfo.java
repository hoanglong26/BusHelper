package com.example.hoanglong.bushelper.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoanglong on 12-Jul-17.
 */

public class BusInfo {
    @SerializedName("id")
    int id;

    @SerializedName("number")
    String number;

    @SerializedName("start_time")
    String start_time;

    @SerializedName("end_time")
    String end_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
