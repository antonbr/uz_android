package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 14.08.16.
 */
public class LoginInfo {
    @SerializedName("device_id")
    private String deviceId;
    private String email;
    private String password;

    public LoginInfo(String deviceId, String email, String password) {
        this.deviceId = deviceId;
        this.email = email;
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
