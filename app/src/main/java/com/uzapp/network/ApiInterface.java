package com.uzapp.network;

import com.uzapp.pojo.login.CreateAccountInfo;
import com.uzapp.pojo.login.LoginInfo;
import com.uzapp.pojo.NewTicketDates;
import com.uzapp.pojo.route.RouteHistoryItem;
import com.uzapp.pojo.login.SocialLoginInfo;
import com.uzapp.pojo.trains.TrainSearchResult;
import com.uzapp.pojo.profile.User;
import com.uzapp.pojo.profile.UserTokenResponse;
import com.uzapp.pojo.booking.Booking;
import com.uzapp.pojo.booking.Uio;
import com.uzapp.pojo.placeslist.PricesPlacesList;
import com.uzapp.pojo.prices.Prices;
import com.uzapp.pojo.route.RouteResponse;
import com.uzapp.pojo.tickets.TicketsResponse;
import com.uzapp.pojo.transportation.Transportation;

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
    Call<List<RouteResponse.Station>> getNearestStations(@Query("latitude") double latitude,
                                                         @Query("longitude") double longitude);

    @GET("order/stations")
    Call<List<RouteResponse.Station>> searchStations(@Query("name") String name);

    @GET("order/trains_and_prices")
    Call<TrainSearchResult> searchTrains(@Query("station_from_code") long stationFromCode,
                                         @Query("station_to_code") long stationToCode,
                                         @Query("date") long date);

    @GET("order/train_route")
    Call<RouteResponse> getTrainRoute(@Query("station_from_code") long stationFromCode,
                                      @Query("station_to_code") long stationToCode,
                                      @Query("train") String train,
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

    @GET("order/tickets")
    Call<List<TicketsResponse>> getUserTickets(@Query("date") Long date, @Query("date_newer_than") Long dateNewerThan, @Query("date_older_than") Long dateOlderThan);

    /*
    request that returns array of dates on which user have tickets in future. Used for showing this dates in calendar
     */
    @GET("order/new_tickets_dates")
    Call<NewTicketDates> getNewTicketDates();

    @POST("user/registration")
    Call<UserTokenResponse> createAccount(@Body CreateAccountInfo createAccountInfo);

    @POST("user/login")
    Call<UserTokenResponse> login(@Body LoginInfo loginInfo);

    @POST("user/social_login")
    Call<UserTokenResponse> socialLogin(@Body SocialLoginInfo socialLoginInfo);

    @FormUrlEncoded
    @POST("user/refresh_token")
    Call<UserTokenResponse> refreshToken(@Field("refresh_token") String refreshToken);

    @FormUrlEncoded
    @POST("user/restore_password")
    Call<Object> restorePassword(@Field("email") String email, @Field("new_password") String newPassword);

    @GET("user")
    Call<User> getUser();

    @FormUrlEncoded
    @POST("user")
    Call<User> updateUser(@Field("first_name") String firstName, @Field("middle_name") String middleName,
                          @Field("last_name") String lastName, @Field("phone_number") String phoneNumber,
                          @Field("email") String email, @Field("student_id") String studentId);

    @FormUrlEncoded
    @POST("user")
    Call<User> changePassword(@Field("password") String password,
                              @Field("new_password") String newPassword);


    @GET("order/routes_history")
    Call<List<RouteHistoryItem>> getRouteHistory();

    @GET("order/booking")
    Call<Booking> getBooking(@Query("train") String train, @Query("station_from_code") int stationFromCode,
                             @Query("station_to_code") int stationToCode, @Query("wagon_type") String wagonType,
                             @Query("wagon_class") int wagonClass, @Query("wagon_number") int wagonNumber,
                             @Query("date") long date, @Query("places") String places,
                             @Query("documents") String documents);

    @GET("order/reserve")
    Call<Booking> getReserve(@Query("train") String train, @Query("station_from_code") int stationFromCode,
                             @Query("station_to_code") int stationToCode, @Query("wagon_type") String wagonType,
                             @Query("wagon_class") int wagonClass, @Query("wagon_number") int wagonNumber,
                             @Query("date") long date, @Query("places") String places,
                             @Query("no_bedding") int noBedding, @Query("services") String services,
                             @Query("documents") String documents);

    @FormUrlEncoded
    @POST("order/cancel")
    Call<Uio> cancelReserveTickets(@Field("uio") String uio);

    @FormUrlEncoded
    @POST("order/payment")
    Call<TicketsResponse> paymentTickets(@Field("uio") String uio, @Field("access_token") String accessToken,
                                         @Field("card") String card, @Field("exp_month") int expMonth, @Field("exp_year") int exp_year,
                                         @Field("cvv") String cvv, @Field("first_name") String firstName, @Field("last_name") String last_name,
                                         @Field("email") String email, @Field("phone") String phone);

    @GET("order/transportation")
    Call<Transportation> getTransportation(@Query("kind") String kind, @Query("uid") String uid, @Query("weight") String weight);
}
