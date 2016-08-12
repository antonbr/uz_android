package com.uzapp.pojo.prices;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Vladimir on 01.08.2016.
 */
@Parcel
public class PlacesPrices {
    @SerializedName("lower")
    int lower;
    @SerializedName("top")
    int top;
    @SerializedName("side_lower")
    int sideLower;
    @SerializedName("side_top")
    int sideTop;
    @SerializedName("total")
    int total;

    public PlacesPrices() {

    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getSideLower() {
        return sideLower;
    }

    public void setSideLower(int sideLower) {
        this.sideLower = sideLower;
    }

    public int getSideTop() {
        return sideTop;
    }

    public void setSideTop(int sideTop) {
        this.sideTop = sideTop;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
