package com.uzapp.pojo.route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vika on 06.09.16.
 */
public class RouteCountry {
    private String code;
    private String name;
    private List<RouteStation> stations = new ArrayList<RouteStation>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RouteStation> getStations() {
        return stations;
    }

    public void setStations(List<RouteStation> stations) {
        this.stations = stations;
    }
}
