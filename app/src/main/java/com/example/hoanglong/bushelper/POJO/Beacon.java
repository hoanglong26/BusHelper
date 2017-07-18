package com.example.hoanglong.bushelper.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoanglong on 10-Jul-17.
 */

public class Beacon {

    @SerializedName("uuid")
    String uuid;
    @SerializedName("major")
    int major;
    @SerializedName("minor")
    int minor;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }


}
