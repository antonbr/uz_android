package com.uzapp.pojo.tickets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 23.08.16.
 */
public class Order {
    @SerializedName("electronic")
    @Expose
    public boolean electronic;
    @SerializedName("pay_date")
    @Expose
    public int payDate;
    @SerializedName("order_number")
    @Expose
    public String orderNumber;
    @SerializedName("barcode_image")
    @Expose
    public String barcodeImage;
    @SerializedName("uio")
    @Expose
    public String uio;
    @SerializedName("tickets")
    @Expose
    public List<Ticket> tickets = new ArrayList<Ticket>();

    public boolean isElectronic() {
        return electronic;
    }

    public void setElectronic(boolean electronic) {
        this.electronic = electronic;
    }

    public int getPayDate() {
        return payDate;
    }

    public void setPayDate(int payDate) {
        this.payDate = payDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getBarcodeImage() {
        return barcodeImage;
    }

    public void setBarcodeImage(String barcodeImage) {
        this.barcodeImage = barcodeImage;
    }

    public String getUio() {
        return uio;
    }

    public void setUio(String uio) {
        this.uio = uio;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
