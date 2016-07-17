package com.uzapp.network;

import com.uzapp.pojo.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vika on 14.07.16.
 */
public interface ApiInterface {
    @GET("order/nearest_stations")
    Call<List<Station>> getNearestStations(@Query("latitude") double latitude,
                                           @Query("longitude") double longitude);

    @GET("order/stations")
    Call<List<Station>> searchStations(@Query("name") String name);
}
