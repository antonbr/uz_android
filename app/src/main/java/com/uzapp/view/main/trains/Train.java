package com.uzapp.view.main.trains;


import java.util.List;

/**
 * Created by viktoria on 2/2/17.
 */

public class Train {
    private int position;
    private String departureTime;
    private String departureDate;
    private String arrivalTime;
    private String arrivalDate;
    private String travelTime;
    private String trainName;
    private String stationFrom;
    private String stationTo;
    private List<TrainPlace> trainPlaces;

    public Train(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getStationFrom() {
        return stationFrom;
    }

    public void setStationFrom(String stationFrom) {
        this.stationFrom = stationFrom;
    }

    public String getStationTo() {
        return stationTo;
    }

    public void setStationTo(String stationTo) {
        this.stationTo = stationTo;
    }

    public List<TrainPlace> getTrainPlaces() {
        return trainPlaces;
    }

    public void setTrainPlaces(List<TrainPlace> trainPlaces) {
        this.trainPlaces = trainPlaces;
    }
}
