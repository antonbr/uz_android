package com.uzapp.pojo.route;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 28.07.16.
 */
public class RouteStation {
    private int code; //the code of station containing 7 numbers
    private String name; //station name
    @SerializedName("arrival_time")
    private int arrivalTime; // seconds from start of the day
    @SerializedName("departure_time")
    private int departureTime;  //  seconds from start of the day
    private int distance; //distance between stations in km
    private int days; //the amount of changed days

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
