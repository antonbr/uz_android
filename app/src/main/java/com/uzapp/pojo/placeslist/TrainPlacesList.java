package com.uzapp.pojo.placeslist;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Vladimir on 01.08.2016.
 */
@Parcel
public class TrainPlacesList {
    @SerializedName("number")
    String number;
    @SerializedName("station_from_code")
    int stationFromCode;
    @SerializedName("station_from_name")
    String stationFromName;
    @SerializedName("station_to_code")
    int stationToCode;
    @SerializedName("station_to_name")
    String stationToName;
    @SerializedName("class_code")
    int classCode;
    @SerializedName("class_name")
    String className;
    @SerializedName("fasted_code")
    int fastedCode;
    @SerializedName("fasted_name")
    String fastedName;
    @SerializedName("date")
    int date;
    @SerializedName("wagons")
    List<WagonsPlacesList> wagons;

    public TrainPlacesList() {

    }

    public TrainPlacesList(String number, int stationFromCode, String stationFromName, int stationToCode,
                           String stationToName, int classCode, String className, int fastedCode,
                           String fastedName, int date, List<WagonsPlacesList> wagons) {
        this.number = number;
        this.stationFromCode = stationFromCode;
        this.stationFromName = stationFromName;
        this.stationToCode = stationToCode;
        this.stationToName = stationToName;
        this.classCode = classCode;
        this.className = className;
        this.fastedCode = fastedCode;
        this.fastedName = fastedName;
        this.date = date;
        this.wagons = wagons;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public int getClassCode() {
        return classCode;
    }

    public void setClassCode(int classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getFastedCode() {
        return fastedCode;
    }

    public void setFastedCode(int fastedCode) {
        this.fastedCode = fastedCode;
    }

    public String getFastedName() {
        return fastedName;
    }

    public void setFastedName(String fastedName) {
        this.fastedName = fastedName;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public List<WagonsPlacesList> getWagons() {
        return wagons;
    }

    public void setWagons(List<WagonsPlacesList> wagons) {
        this.wagons = wagons;
    }
}
