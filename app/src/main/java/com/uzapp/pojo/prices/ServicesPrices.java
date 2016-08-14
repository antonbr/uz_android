package com.uzapp.pojo.prices;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Vladimir on 01.08.2016.
 */
@Parcel
public class ServicesPrices {
    @SerializedName("code")
    String code;
    @SerializedName("name")
    String name;

    public ServicesPrices() {

    }

    public ServicesPrices(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
