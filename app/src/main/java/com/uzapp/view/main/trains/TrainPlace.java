package com.uzapp.view.main.trains;

import com.uzapp.pojo.WagonClass;
import com.uzapp.pojo.WagonType;

/**
 * Created by viktoria on 2/2/17.
 */

public class TrainPlace {
    private String availablePlaceCount;
    private String minPrice;
    private String wagonTypeStr;
    private WagonType wagonType;
    private WagonClass wagonClass;

    public TrainPlace(String availablePlaceCount, String minPrice, String wagonTypeStr, WagonType wagonType, WagonClass wagonClass) {
        this.availablePlaceCount = availablePlaceCount;
        this.minPrice = minPrice;
        this.wagonTypeStr = wagonTypeStr;
        this.wagonType = wagonType;
        this.wagonClass = wagonClass;
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

    public WagonClass getWagonClass() {
        return wagonClass;
    }

    public void setWagonClass(WagonClass wagonClass) {
        this.wagonClass = wagonClass;
    }
}
