package com.example.hoanglong.bushelper.model;

/**
 * Created by hoanglong on 26-Jun-17.
 */

public class PlacePrediction {

    public CharSequence placeId;
    public CharSequence description;

    public PlacePrediction(CharSequence placeId, CharSequence description) {
        this.placeId = placeId;
        this.description = description;
    }

    @Override
    public String toString() {
        return description.toString();
    }

    public CharSequence getPlaceId() {
        return placeId;
    }

    public void setPlaceId(CharSequence placeId) {
        this.placeId = placeId;
    }

    public CharSequence getDescription() {
        return description;
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }
}