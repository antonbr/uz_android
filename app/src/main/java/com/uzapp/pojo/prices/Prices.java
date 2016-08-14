package com.uzapp.pojo.prices;

import android.annotation.SuppressLint;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Vladimir on 01.08.2016.
 */
@SuppressLint("ParcelCreator")
@Parcel
public class Prices implements Parcelable {
    @SerializedName("station_from_code")
    int station_from_code;
    @SerializedName("station_from_name")
    String stationFromName;
    @SerializedName("station_to_code")
    int stationToCode;
    @SerializedName("station_to_name")
    String stationToName;
    @SerializedName("train")
    TrainPrices train;

    public Prices() {

    }

    public int getStation_from_code() {
        return station_from_code;
    }

    public void setStation_from_code(int station_from_code) {
        this.station_from_code = station_from_code;
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

    public TrainPrices getTrain() {
        return train;
    }

    public void setTrain(TrainPrices train) {
        this.train = train;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {

    }
}