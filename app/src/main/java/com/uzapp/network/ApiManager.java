package com.uzapp.network;

import android.content.Context;

import com.uzapp.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vika on 14.07.16.
 */
public class ApiManager {
    private static ApiInterface api;

    private ApiManager() {
    }

    public static ApiInterface getApi(Context context) {
        if (api == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().
                    addInterceptor(loggingInterceptor).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.api_endpoint))
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(ApiInterface.class);
        }
        return api;
    }
}
