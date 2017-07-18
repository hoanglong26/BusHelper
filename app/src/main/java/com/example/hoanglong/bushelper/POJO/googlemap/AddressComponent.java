package com.example.hoanglong.bushelper.POJO.googlemap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoanglong on 01-Jul-17.
 */

public class AddressComponent {

    @SerializedName("formatted_address")
    @Expose
    private String address;


    @SerializedName("place_id")
    @Expose
    private String place_id;

    public AddressComponent(String address, String place_id) {
        this.address = address;
        this.place_id = place_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }
}
