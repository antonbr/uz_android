package com.uzapp.pojo.tickets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 23.08.16.
 */
public class Withholding {
    @SerializedName("kind")
    @Expose
    public int kind;
    @SerializedName("ticket")
    @Expose
    public int ticket;
    @SerializedName("reserved_seat")
    @Expose
    public int reservedSeat;
    @SerializedName("reserved_seat_percent")
    @Expose
    public int reservedSeatPercent;
    @SerializedName("ticket_percent")
    @Expose
    public int ticketPercent;
    @SerializedName("vat")
    @Expose
    public int vat;
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
    @SerializedName("carrier")
    @Expose
    public int carrier;
    @SerializedName("cost")
    @Expose
    public int cost;
    @SerializedName("return_sum")
    @Expose
    public int returnSum;
    @SerializedName("vat_return")
    @Expose
    public int vatReturn;
    @SerializedName("return_vat")
    @Expose
    public int returnVat;
    @SerializedName("com_service")
    @Expose
    public int comService;
    @SerializedName("com_service_vat")
    @Expose
    public int comServiceVat;
    @SerializedName("pay_commision")
    @Expose
    public int payCommision;
    @SerializedName("pay_commision_vat")
    @Expose
    public int payCommisionVat;

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
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

    public int getReservedSeatPercent() {
        return reservedSeatPercent;
    }

    public void setReservedSeatPercent(int reservedSeatPercent) {
        this.reservedSeatPercent = reservedSeatPercent;
    }

    public int getTicketPercent() {
        return ticketPercent;
    }

    public void setTicketPercent(int ticketPercent) {
        this.ticketPercent = ticketPercent;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
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

    public int getCarrier() {
        return carrier;
    }

    public void setCarrier(int carrier) {
        this.carrier = carrier;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getReturnSum() {
        return returnSum;
    }

    public void setReturnSum(int returnSum) {
        this.returnSum = returnSum;
    }

    public int getVatReturn() {
        return vatReturn;
    }

    public void setVatReturn(int vatReturn) {
        this.vatReturn = vatReturn;
    }

    public int getReturnVat() {
        return returnVat;
    }

    public void setReturnVat(int returnVat) {
        this.returnVat = returnVat;
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

    public int getPayCommision() {
        return payCommision;
    }

    public void setPayCommision(int payCommision) {
        this.payCommision = payCommision;
    }

    public int getPayCommisionVat() {
        return payCommisionVat;
    }

    public void setPayCommisionVat(int payCommisionVat) {
        this.payCommisionVat = payCommisionVat;
    }
}
