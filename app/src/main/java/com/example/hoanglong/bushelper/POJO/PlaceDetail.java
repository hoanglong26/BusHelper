package com.example.hoanglong.bushelper.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoanglong on 01-Jul-17.
 */

public class PlaceDetail {
    @SerializedName("results")
    @Expose
    private List<AddressComponent> addr_comp = new ArrayList<>();

    public List<AddressComponent> getAddr_comp() {
        return addr_comp;
    }

    public void setAddr_comp(List<AddressComponent> addr_comp) {
        this.addr_comp = addr_comp;
    }
}
