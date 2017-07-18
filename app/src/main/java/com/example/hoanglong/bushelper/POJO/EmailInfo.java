package com.example.hoanglong.bushelper.POJO;

/**
 * Created by hoanglong on 07-Feb-17.
 */

public class EmailInfo {
    private String email;
    private String name;
    private String avatarUrl;

    public EmailInfo(String email) {
        this.email = email;
    }

    public EmailInfo(String email, String name, String avatarUrl) {
        this.email = email;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
