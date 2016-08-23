package com.uzapp.pojo.tickets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 23.08.16.
 */
public class TicketCost {
    @SerializedName("kind")
    @Expose
    public int kind;
    @SerializedName("carrier")
    @Expose
    public int carrier;
    @SerializedName("carrier_vat")
    @Expose
    public int carrierVat;
    @SerializedName("ticket")
    @Expose
    public int ticket;
    @SerializedName("reserved_seat")
    @Expose
    public int reservedSeat;
    @SerializedName("add_ticket")
    @Expose
    public int addTicket;
    @SerializedName("add_reserved_seat")
    @Expose
    public int addReservedSeat;
    @SerializedName("service")
    @Expose
    public int service;
    @SerializedName("vat")
    @Expose
    public int vat;
    @SerializedName("insurance")
    @Expose
    public int insurance;
    @SerializedName("commission")
    @Expose
    public int commission;
    @SerializedName("commission_vat")
    @Expose
    public int commissionVat;
    @SerializedName("fee")
    @Expose
    public int fee;
    @SerializedName("fee_vat")
    @Expose
    public int feeVat;
    @SerializedName("cost")
    @Expose
    public double cost;
    @SerializedName("com_service")
    @Expose
    public int comService;
    @SerializedName("com_service_vat")
    @Expose
    public int comServiceVat;

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getCarrier() {
        return carrier;
    }

    public void setCarrier(int carrier) {
        this.carrier = carrier;
    }

    public int getCarrierVat() {
        return carrierVat;
    }

    public void setCarrierVat(int carrierVat) {
        this.carrierVat = carrierVat;
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

    public int getCommissionVat() {
        return commissionVat;
    }

    public void setCommissionVat(int commissionVat) {
        this.commissionVat = commissionVat;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getFeeVat() {
        return feeVat;
    }

    public void setFeeVat(int feeVat) {
        this.feeVat = feeVat;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getComService() {
        return comService;
    }

    public void setComService(int comService) {
        this.comService = comService;
    }

    public int getComServiceVat() {
        return comServiceVat;
    }

    public void setComServiceVat(int comServiceVat) {
        this.comServiceVat = comServiceVat;
    }
}
