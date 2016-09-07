package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 07.09.16.
 */
public class SocialLoginInfo {
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("facebook_token")
    private String fbToken;
    @SerializedName("vk_token")
    private String vkToken;
    @SerializedName("ok_token")
    private String okToken;
    private String email;

    public SocialLoginInfo(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public String getVkToken() {
        return vkToken;
    }

    public void setVkToken(String vkToken) {
        this.vkToken = vkToken;
    }

    public String getOkToken() {
        return okToken;
    }

    public void setOkToken(String okToken) {
        this.okToken = okToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
