package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;
import com.uzapp.R;

/**
 * Created by vika on 30.08.16.
 */
public enum WagonClass {
    @SerializedName("Д")
    ECONOMY("Д", R.string.wagon_class_economy),
    @SerializedName("Б")
    FIRM("Б", R.string.wagon_class_firm),
    @SerializedName("В")
    FIRM_SUPERIOR("В", R.string.wagon_class_firm_superior),
    @SerializedName("О")
    FIRM_CHEAP("О", R.string.wagon_class_firm_cheap),
    @SerializedName("1")
    FIRST_SEATING("1", R.string.wagon_class_1),
    @SerializedName("2")
    SECOND_SEATING("2", R.string.wagon_class_2),
    @SerializedName("3")
    THIRD_SEATING("3", R.string.wagon_class_3);

    private String shortName;
    private int longNameStringRes;

    WagonClass(String shortName, int longNameStringRes) {
        this.shortName = shortName;
        this.longNameStringRes = longNameStringRes;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getLongNameStringRes() {
        return longNameStringRes;
    }

    public void setLongNameStringRes(int longNameStringRes) {
        this.longNameStringRes = longNameStringRes;
    }
}
