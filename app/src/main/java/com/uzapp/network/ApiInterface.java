package com.uzapp.network;

import com.uzapp.pojo.CreateAccountInfo;
import com.uzapp.pojo.Station;
import com.uzapp.pojo.TrainSearchResult;
import com.uzapp.pojo.UserTokenResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("order/trains_and_prices")
    Call<TrainSearchResult> searchTrains(@Query("station_from_code") long stationFromCode,
                                         @Query("station_to_code") long stationToCode,
                                         @Query("date") long date);

    @POST("user/registration")
    Call<UserTokenResponse> createAccount(@Body CreateAccountInfo createAccountInfo);
}
