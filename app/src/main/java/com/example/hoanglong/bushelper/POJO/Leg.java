package com.example.hoanglong.bushelper.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Leg {

    @SerializedName("distance")
    @Expose
    private Distance distance;

    @SerializedName("duration")
    @Expose
    private Duration duration;


    @SerializedName("steps")
    @Expose
    private List<Step> steps;

    @SerializedName("start_address")
    @Expose
    private String start_address;

    @SerializedName("start_location")
    @Expose
    private MapLocation start_location;

    @SerializedName("end_address")
    @Expose
    private String end_address;

    @SerializedName("end_location")
    @Expose
    private MapLocation end_location;

    /**
     *
     * @return
     * The distance
     */
    public Distance getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     * The distance
     */
    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     * The duration
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     * The duration
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public MapLocation getStart_location() {
        return start_location;
    }

    public void setStart_location(MapLocation start_location) {
        this.start_location = start_location;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public MapLocation getEnd_location() {
        return end_location;
    }

    public void setEnd_location(MapLocation end_location) {
        this.end_location = end_location;
    }
}
