package com.uzapp.view.main.trains;

import com.uzapp.pojo.WagonType;

/**
 * Created by viktoria on 2/2/17.
 */

public class TrainPlace {
    private String availablePlaceCount;
    private String minPrice;
    private String wagonTypeStr;
    private WagonType wagonType;


    public TrainPlace(String availablePlaceCount, String minPrice, String wagonTypeStr, WagonType wagonType) {
        this.availablePlaceCount = availablePlaceCount;
        this.minPrice = minPrice;
        this.wagonTypeStr = wagonTypeStr;
        this.wagonType = wagonType;
    }

    public String getAvailablePlaceCount() {
        return availablePlaceCount;
    }

    public void setAvailablePlaceCount(String availablePlaceCount) {
        this.availablePlaceCount = availablePlaceCount;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getWagonTypeStr() {
        return wagonTypeStr;
    }

    public void setWagonTypeStr(String wagonTypeStr) {
        this.wagonTypeStr = wagonTypeStr;
    }

    public WagonType getWagonType() {
        return wagonType;
    }

    public void setWagonType(WagonType wagonType) {
        this.wagonType = wagonType;
    }
}
