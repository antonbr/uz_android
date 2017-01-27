package com.uzapp.pojo.route;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

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

    /**
     * Created by vika on 14.07.16.
     */
    @Parcel
    public static class Station {
        long code;
        String name;
        String railway;

        public Station(long code, String name, String railway) {
            this.code = code;
            this.name = name;
            this.railway = railway;
        }

        public Station() {
        }

        public long getCode() {
            return code;
        }

        public void setCode(long code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRailway() {
            return railway;
        }

        public void setRailway(String railway) {
            this.railway = railway;
        }
    }
}
