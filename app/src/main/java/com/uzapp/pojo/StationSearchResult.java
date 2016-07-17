package com.uzapp.pojo;

import org.parceler.Parcel;

/**
 * Created by vika on 14.07.16.
 */
@Parcel
public class StationSearchResult {
    int code;
    String name;
    String railway;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
