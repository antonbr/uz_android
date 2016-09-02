package com.uzapp.pojo.tickets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vika on 23.08.16.
 */
public class TicketService {
    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("cost")
    @Expose
    public int cost;
    @SerializedName("name")
    @Expose
    public String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
