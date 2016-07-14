package com.uzapp.network;

import android.content.Context;

import com.uzapp.R;
import com.uzapp.pojo.Languages;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
            OkHttpClient client = new OkHttpClient.Builder().
                    addInterceptor(getLoggingInterceptor()).
                    addInterceptor(getHeaderInterceptor(context)).
                    build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.api_endpoint))
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(ApiInterface.class);
        }
        return api;
    }

    private static Interceptor getHeaderInterceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().
                        header("Accept-Language", Languages.UA.name()). //TODO use current locale
                        build();
                return chain.proceed(request);
            }
        };
    }

    private static Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return loggingInterceptor;
    }
}
