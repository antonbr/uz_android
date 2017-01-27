package com.uzapp.pojo.route;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by vika on 06.09.16.
 */
@Parcel
public class RouteHistoryItem {
    @SerializedName("station_from_code")
    private long stationFromCode;
    @SerializedName("station_from_name")
    private String stationFromName;
    @SerializedName("station_to_code")
    private long stationToCode;
    @SerializedName("station_to_name")
    private String stationToName;

    public long getStationFromCode() {
        return stationFromCode;
    }

    public void setStationFromCode(long stationFromCode) {
        this.stationFromCode = stationFromCode;
    }

    public String getStationFromName() {
        return stationFromName;
    }

    public void setStationFromName(String stationFromName) {
        this.stationFromName = stationFromName;
    }

    public long getStationToCode() {
        return stationToCode;
    }

    public void setStationToCode(long stationToCode) {
        this.stationToCode = stationToCode;
    }

    public String getStationToName() {
        return stationToName;
    }

    public void setStationToName(String stationToName) {
        this.stationToName = stationToName;
    }
}
