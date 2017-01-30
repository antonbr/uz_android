package com.uzapp.pojo.route;

import org.parceler.Parcel;

/**
 * Created by viktoria on 1/30/17.
 */
@Parcel
public class Station {
    long code;
    String name;
    String railway;

    public Station(long code, String name, String railway) {
        this.code = code;
        this.name = name;
        this.railway = railway;
    }

    public Station() {
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRailway() {
        return railway;
    }

    public void setRailway(String railway) {
        this.railway = railway;
    }
}
