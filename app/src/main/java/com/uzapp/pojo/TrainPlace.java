package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 26.07.16.
 */
public class TrainPlace {
    @SerializedName("type")
    public String type;
    @SerializedName("lower")
    public int lower;
    @SerializedName("top")
    public int top;
    @SerializedName("side_lower")
    public int sideLower;
    @SerializedName("side_top")
    public int sideTop;
    @SerializedName("total")
    public int total;
    @SerializedName("class_name")
    public String className;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
