package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 28.07.16.
 */
public class RouteStation {
    private int code; //the code of station containing 7 numbers
    private String name; //station name
    @SerializedName("arrival_time")
    private String arrivalTime; // format HH:MM:SS
    @SerializedName("departure_time")
    private String departureTime;  // format HH:MM:SS
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

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
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
