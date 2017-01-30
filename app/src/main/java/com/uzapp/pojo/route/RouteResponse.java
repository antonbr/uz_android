package com.uzapp.pojo.route;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 06.09.16.
 */
public class RouteResponse {
    private int date;
    @SerializedName("station_from_code")
    private int stationFromCode;
    @SerializedName("station_from_name")
    private String stationFromName;
    @SerializedName("station_to_code")
    private int stationToCode;
    @SerializedName("station_to_name")
    private String stationToName;
    private List<RouteCountry> countries = new ArrayList<RouteCountry>();

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getStationFromCode() {
        return stationFromCode;
    }

    public void setStationFromCode(int stationFromCode) {
        this.stationFromCode = stationFromCode;
    }

    public String getStationFromName() {
        return stationFromName;
    }

    public void setStationFromName(String stationFromName) {
        this.stationFromName = stationFromName;
    }

    public int getStationToCode() {
        return stationToCode;
    }

    public void setStationToCode(int stationToCode) {
        this.stationToCode = stationToCode;
    }

    public String getStationToName() {
        return stationToName;
    }

    public void setStationToName(String stationToName) {
        this.stationToName = stationToName;
    }

    public List<RouteCountry> getCountries() {
        return countries;
    }

    public void setCountries(List<RouteCountry> countries) {
        this.countries = countries;
    }
}
