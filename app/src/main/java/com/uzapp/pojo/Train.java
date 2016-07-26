package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 26.07.16.
 */
public class Train {
    @SerializedName("number")
    public String number;
    @SerializedName("model")
    public int model;
    @SerializedName("category")
    public int category;
    @SerializedName("station_from_name")
    public String stationFromName;
    @SerializedName("station_from_code")
    public int stationFromCode;
    @SerializedName("station_to_name")
    public String stationToName;
    @SerializedName("station_to_code")
    public int stationToCode;
    @SerializedName("class_name")
    public String className;
    @SerializedName("class_code")
    public int classCode;
    @SerializedName("fasted_name")
    public String fastedName;
    @SerializedName("fasted_code")
    public int fastedCode;
    @SerializedName("departure_date")
    public int departureDate;
    @SerializedName("arrival_date")
    public int arrivalDate;
    @SerializedName("travel_time")
    public String travelTime;
    @SerializedName("places")
    public List<TrainPlace> places = new ArrayList<TrainPlace>();

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getStationFromName() {
        return stationFromName;
    }

    public void setStationFromName(String stationFromName) {
        this.stationFromName = stationFromName;
    }

    public int getStationFromCode() {
        return stationFromCode;
    }

    public void setStationFromCode(int stationFromCode) {
        this.stationFromCode = stationFromCode;
    }

    public String getStationToName() {
        return stationToName;
    }

    public void setStationToName(String stationToName) {
        this.stationToName = stationToName;
    }

    public int getStationToCode() {
        return stationToCode;
    }

    public void setStationToCode(int stationToCode) {
        this.stationToCode = stationToCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getClassCode() {
        return classCode;
    }

    public void setClassCode(int classCode) {
        this.classCode = classCode;
    }

    public String getFastedName() {
        return fastedName;
    }

    public void setFastedName(String fastedName) {
        this.fastedName = fastedName;
    }

    public int getFastedCode() {
        return fastedCode;
    }

    public void setFastedCode(int fastedCode) {
        this.fastedCode = fastedCode;
    }

    public int getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(int departureDate) {
        this.departureDate = departureDate;
    }

    public int getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(int arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public List<TrainPlace> getPlaces() {
        return places;
    }

    public void setPlaces(List<TrainPlace> places) {
        this.places = places;
    }
}
