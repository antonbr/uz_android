package com.uzapp.network;

import com.uzapp.pojo.StationSearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vika on 14.07.16.
 */
public interface ApiInterface {

    @GET("order/nearest_stations")
    Call<List<StationSearchResult>> getNearestStations(double latitude, double longitude);

    @GET("order/stations")
    Call<List<StationSearchResult>> searchStations(String name);
}
