package com.uzapp.pojo.placeslist;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Vladimir on 01.08.2016.
 */
@Parcel
public class PricesPlacesList {
    @SerializedName("station_from_code")
    int stationFromCode;
    @SerializedName("station_from_name")
    String stationFromName;
    @SerializedName("station_to_code")
    int stationToCode;
    @SerializedName("station_to_name")
    String stationToName;
    @SerializedName("train")
    TrainPlacesList trainPlacesList;

    public PricesPlacesList() {

    }

    public PricesPlacesList(int stationFromCode, String stationFromName, int stationToCode, String stationToName, TrainPlacesList trainPlacesList) {
        this.stationFromCode = stationFromCode;
        this.stationFromName = stationFromName;
        this.stationToCode = stationToCode;
        this.stationToName = stationToName;
        this.trainPlacesList = trainPlacesList;
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

    public TrainPlacesList getTrainPlacesList() {
        return trainPlacesList;
    }

    public void setTrainPlacesList(TrainPlacesList trainPlacesList) {
        this.trainPlacesList = trainPlacesList;
    }
}