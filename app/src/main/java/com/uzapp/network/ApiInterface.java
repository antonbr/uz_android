package com.uzapp.network;

import com.uzapp.pojo.CreateAccountInfo;
import com.uzapp.pojo.LoginInfo;
import com.uzapp.pojo.Station;
import com.uzapp.pojo.TrainSearchResult;
import com.uzapp.pojo.User;
import com.uzapp.pojo.UserTokenResponse;
import com.uzapp.pojo.placeslist.PricesPlacesList;
import com.uzapp.pojo.prices.Prices;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @GET("order/prices")
    Call<Prices> getPrices(@Query("station_from_code") long stationFromCode,
                           @Query("station_to_code") long stationToCode,
                           @Query("train") String train,
                           @Query("date") long date);

    @GET("order/places_list")
    Call<List<PricesPlacesList>> getPlacesList(@Query("station_from_code") int stationFromCode,
                                               @Query("station_to_code") int stationToCode,
                                               @Query("train") String train,
                                               @Query("wagon_types") String wagonTypes,
                                               @Query("wagon_classes") String wagonClasses,
                                               @Query("wagon_numbers") String wagonNumbers,
                                               @Query("date") int date);

    @POST("user/registration")
    Call<UserTokenResponse> createAccount(@Body CreateAccountInfo createAccountInfo);

    @POST("user/login")
    Call<UserTokenResponse> login(@Body LoginInfo loginInfo);

    @FormUrlEncoded
    @POST("user/refresh_token")
    Call<UserTokenResponse> refreshToken(@Field("refresh_token") String refreshToken);

    @FormUrlEncoded
    @POST("user/restore_password")
    Call<Object> restorePassword(@Field("email") String email, @Field("new_password") String newPassword);

    @GET("user")
    Call<User> getUser(@Query("access_token") String accessToken);
}
