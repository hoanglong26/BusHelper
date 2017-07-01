package com.example.hoanglong.bushelper.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hoanglong on 27-Jun-17.
 */

public class Step {
    @SerializedName("html_instructions")
    @Expose
    private String instruction;


    @SerializedName("transit_details")
    @Expose
    private TransitDetail transit_detail;


    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public TransitDetail getTransit_detail() {
        return transit_detail;
    }

    public void setTransit_detail(TransitDetail arrival_stop) {
        this.transit_detail = arrival_stop;
    }


}

