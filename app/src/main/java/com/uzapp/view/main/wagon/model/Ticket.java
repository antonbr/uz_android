package com.uzapp.view.main.wagon.model;

import java.io.Serializable;

/**
 * Created by Vladimir on 04.08.2016.
 */
public class Ticket implements Serializable {

    private String wagonNumber;
    private String placeNumber;
    private String placeType;
    private int ticketPrice;
    private int departureDate;
    private int arrivalDate;
    private int wagonClasses;
    private String wagonType;
    private boolean isBooking;
    private boolean isReserve;
    private String firstName;
    private String lastName;
    private String kind;
    private String privilege;
    private String service;
    private String baggage;

    public Ticket() {}

    public Ticket(String wagonNumber, String placeNumber, String placeType, int ticketPrice,
                  int departureDate, int arrivalDate, int wagonClasses, String wagonType) {
        this.wagonNumber = wagonNumber;
        this.placeNumber = placeNumber;
        this.placeType = placeType;
        this.ticketPrice = ticketPrice;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.wagonClasses = wagonClasses;
        this.wagonType = wagonType;
    }

    public String getWagonNumber() {
        return wagonNumber;
    }

    public void setWagonNumber(String wagonNumber) {
        this.wagonNumber = wagonNumber;
    }

    public String getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(String placeNumber) {
        this.placeNumber = placeNumber;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
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

    public int getWagonClasses() {
        return wagonClasses;
    }

    public void setWagonClasses(int wagonClasses) {
        this.wagonClasses = wagonClasses;
    }

    public String getWagonType() {
        return wagonType;
    }

    public void setWagonType(String wagonType) {
        this.wagonType = wagonType;
    }

    public boolean isBooking() {
        return isBooking;
    }

    public void setIsBooking(boolean isBooking) {
        this.isBooking = isBooking;
    }

    public boolean isReserve() {
        return isReserve;
    }

    public void setIsReserve(boolean isReserve) {
        this.isReserve = isReserve;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBaggage() {
        return baggage;
    }

    public void setBaggage(String baggage) {
        this.baggage = baggage;
    }
}
