package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by viktoria on 2/6/17.
 */

public enum TrainCategory {
   @SerializedName("1") INTERCITY_PLUS,
    @SerializedName("2") INTERCITY,
    @SerializedName("3") REGIONAL_EXPRESS,
    @SerializedName("4") REGIONAL_TRAIN,
    @SerializedName("5") NIGHT_EXPRESS,
    @SerializedName("6") NIGHT_FAST,
    @SerializedName("7") NIGHT_PASSENGER;
}
