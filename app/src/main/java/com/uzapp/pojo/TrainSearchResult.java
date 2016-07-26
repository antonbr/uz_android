package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 26.07.16.
 */
public class TrainSearchResult {
    @SerializedName("station_from_code")
    private int stationFromCode;
    @SerializedName("station_to_code")
    private int stationToCode;
    @SerializedName("not_full_info")
    private boolean notFullInfo;
    @SerializedName("trains")
    private List<Train> trains = new ArrayList<Train>();

    public int getStationFromCode() {
        return stationFromCode;
    }

    public void setStationFromCode(int stationFromCode) {
        this.stationFromCode = stationFromCode;
    }

    public int getStationToCode() {
        return stationToCode;
    }

    public void setStationToCode(int stationToCode) {
        this.stationToCode = stationToCode;
    }

    public boolean isNotFullInfo() {
        return notFullInfo;
    }

    public void setNotFullInfo(boolean notFullInfo) {
        this.notFullInfo = notFullInfo;
    }

    public List<Train> getTrains() {
        return trains;
    }

    public void setTrains(List<Train> trains) {
        this.trains = trains;
    }
}
