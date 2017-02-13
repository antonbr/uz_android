package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;
import com.uzapp.R;

/**
 * Created by vika on 30.08.16.
 */
public enum WagonType {
    @SerializedName("Л")
    SUITE("Л", R.string.wagon_type_suite),
    @SerializedName("М")
    SOFT("М", R.string.wagon_type_soft),
    @SerializedName("К")
    COUPE("К", R.string.wagon_type_coupe),
    @SerializedName("П")
    PLATSKART("П", R.string.wagon_type_platskart),
    @SerializedName("С")
    SEATING("С", R.string.wagon_type_seating),
    @SerializedName("О")
    COMMON("О", R.string.wagon_type_common);

    private String shortName;
    private int longNameStringRes;

    WagonType(String shortName, int longNameStringRes) {
        this.shortName = shortName;
        this.longNameStringRes = longNameStringRes;
    }

    public String getShortName() {
        return shortName;
    }

    public int getLongNameStringRes() {
        return longNameStringRes;
    }
}
