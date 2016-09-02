package com.uzapp.pojo.tickets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 23.08.16.
 */
public class Coupon {
    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("number")
    @Expose
    public String number;

    public Coupon(int count, String name, String number) {
        this.count = count;
        this.name = name;
        this.number = number;
    }
}
