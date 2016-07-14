package com.uzapp.network;

import com.uzapp.pojo.StationSearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vika on 14.07.16.
 */
public interface ApiInterface {
    @GET("order/nearest_stations")
    Call<List<StationSearchResult>> getNearestStations(@Query("latitude") double latitude,
                                                       @Query("longitude") double longitude);

    @GET("order/stations")
    Call<List<StationSearchResult>> searchStations(@Query("name") String name);
}
