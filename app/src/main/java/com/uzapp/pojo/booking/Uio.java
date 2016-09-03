package com.uzapp.pojo.booking;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Vladimir on 01.09.2016.
 */
@Parcel
public class Uio {

    @SerializedName("uio")
    private String uio;

    public Uio() {}

    public String getUio() {
        return uio;
    }

    public void setUio(String uio) {
        this.uio = uio;
    }
}
