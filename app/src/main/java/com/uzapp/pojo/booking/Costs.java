package com.uzapp.pojo.booking;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Vladimir on 22.08.2016.
 */
@Parcel
public class Costs {

    @SerializedName("kind")
    String kind;
    @SerializedName("carrier")
    int carrier;
    @SerializedName("ticket")
    int ticket;
    @SerializedName("reserved_seat")
    int reservedSeat;
    @SerializedName("add_ticket")
    int addTicket;
    @SerializedName("add_reserved_seat")
    int addReservedSeat;
    @SerializedName("service")
    int service;
    @SerializedName("vat")
    int vat;
    @SerializedName("insurance")
    int insurance;
    @SerializedName("commission")
    int commission;
    @SerializedName("fee")
    double fee;
    @SerializedName("cost")
    int cost;
    @SerializedName("com_service")
    int comService;

    public Costs() {}

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getCarrier() {
        return carrier;
    }

    public void setCarrier(int carrier) {
        this.carrier = carrier;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public int getReservedSeat() {
        return reservedSeat;
    }

    public void setReservedSeat(int reservedSeat) {
        this.reservedSeat = reservedSeat;
    }

    public int getAddTicket() {
        return addTicket;
    }

    public void setAddTicket(int addTicket) {
        this.addTicket = addTicket;
    }

    public int getAddReservedSeat() {
        return addReservedSeat;
    }

    public void setAddReservedSeat(int addReservedSeat) {
        this.addReservedSeat = addReservedSeat;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getComService() {
        return comService;
    }

    public void setComService(int comService) {
        this.comService = comService;
    }
}
