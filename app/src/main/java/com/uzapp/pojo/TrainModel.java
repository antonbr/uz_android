package com.uzapp.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by viktoria on 2/6/17.
 */

public enum TrainModel {
    @SerializedName("1")HYUNDAI,
    @SerializedName("2") SKODA,
    @SerializedName("3") EKP1,
    @SerializedName("4") KVB3,
    @SerializedName("5") KVB32,
    @SerializedName("0") OTHER;
}
