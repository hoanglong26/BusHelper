package com.example.hoanglong.bushelper.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoanglong on 07-Feb-17.
 */

public class UserAccount {
    @SerializedName("UserToken")
    private String token;

    @SerializedName("id")
    private int id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
