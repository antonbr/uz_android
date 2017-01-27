package com.uzapp.pojo.trains;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 26.07.16.
 */
@Parcel
public class TrainSearchResult {
    @SerializedName("station_from_code")
    int stationFromCode;
    @SerializedName("station_to_code")
    int stationToCode;
    @SerializedName("not_full_info")
    boolean notFullInfo;
    @SerializedName("trains")
    List<Train> trains = new ArrayList<Train>();

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
