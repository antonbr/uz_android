package com.uzapp.pojo.tickets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 23.08.16.
 */
public class TicketsResponse {
    @SerializedName("orders")
    @Expose
    public List<Order> orders = new ArrayList<Order>();

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
