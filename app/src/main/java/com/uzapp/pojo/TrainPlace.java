package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by vika on 26.07.16.
 */
@Parcel
public class TrainPlace {
    @SerializedName("type")
    String type;
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
    @SerializedName("class_name")
    String className;
    @SerializedName("cost_currency")
    String costCurrency;
    @SerializedName("cost")
    double cost;
    @SerializedName("cost_reserve_currency")
    String costReserveCurrency;
    @SerializedName("cost_reserve")
    double costReserve;

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

    public String getCostCurrency() {
        return costCurrency;
    }

    public void setCostCurrency(String costCurrency) {
        this.costCurrency = costCurrency;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCostReserveCurrency() {
        return costReserveCurrency;
    }

    public void setCostReserveCurrency(String costReserveCurrency) {
        this.costReserveCurrency = costReserveCurrency;
    }

    public double getCostReserve() {
        return costReserve;
    }

    public void setCostReserve(double costReserve) {
        this.costReserve = costReserve;
    }
}
