package com.uzapp.view.main.wagon.model;

import java.io.Serializable;

/**
 * Created by Vladimir on 04.08.2016.
 */
public class Ticket implements Serializable {

    private int id;
    private String wagonNumber;
    private String placeNumber;
    private String placeType;
    private int ticketPrice;

    public Ticket() {

    }

    public Ticket(int id, String wagonNumber, String placeNumber, String placeType, int ticketPrice) {
        this.id = id;
        this.wagonNumber = wagonNumber;
        this.placeNumber = placeNumber;
        this.placeType = placeType;
        this.ticketPrice = ticketPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
