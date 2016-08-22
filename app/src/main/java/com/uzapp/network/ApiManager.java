package com.uzapp.network;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.uzapp.R;
import com.uzapp.pojo.UserTokenResponse;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.login.LoginFlowActivity;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vika on 14.07.16.
 */
public class ApiManager {
    private static ApiInterface api;
    private static Retrofit retrofit;

    private ApiManager() {
    }

    public static ApiInterface getApi(Context context) {
        if (api == null) {
            api = getRetrofit(context).create(ApiInterface.class);
        }
        return api;
    }

    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().
                    addInterceptor(getLoggingInterceptor()).
                    addInterceptor(getHeaderInterceptor(context)).
                    addInterceptor(getErrorInterceptor(context)).
                    build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.api_endpoint))
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static Interceptor getHeaderInterceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(getRequestWithHeadersAndToken(context, chain.request()));
            }
        };
    }

    private static Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    private static Interceptor getErrorInterceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                switch (response.code()) {
                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                        /*
                        If 403, try to refresh token if user is logged in, if not - go to login page.
                        If refresh token succeed, than sent the same request with new token
                         */
                        if (refreshToken(context, request)) {
                            request = getRequestWithHeadersAndToken(context, request);
                            response = chain.proceed(request);
                        }
                        break;
                }
                return response;
            }
        };
    }

    //add language header to request, add access token as query param
    private static Request getRequestWithHeadersAndToken(Context context, Request request) {
        Request.Builder requestBuilder = request.newBuilder();
        String accessToken = PrefsUtil.getStringPreference(context, PrefsUtil.USER_ACCESS_TOKEN);
        requestBuilder.header("Accept-Language", CommonUtils.getLanguage());
        if (!TextUtils.isEmpty(accessToken)) {
            HttpUrl originalHttpUrl = request.url();
            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("access_token", accessToken)
                    .build();
            requestBuilder.url(url);

        }
        return requestBuilder.build();
    }


    //return true if token was refreshed
    private static boolean refreshToken(Context context, Request request) {
        String refreshToken = PrefsUtil.getStringPreference(context, PrefsUtil.USER_REFRESH_TOKEN);
        if (!TextUtils.isEmpty(refreshToken)) {
            try {
                Call<UserTokenResponse> call = api.refreshToken(refreshToken);
                retrofit2.Response<UserTokenResponse> userTokenResponse = call.execute();
                if (userTokenResponse.isSuccessful()) {
                    UserTokenResponse user = userTokenResponse.body();
                    PrefsUtil.saveUserInfo(context, user.getUserId(), user.getAccessToken(), user.getRefreshToken());
                    return true;
                }
            } catch (IOException e) {
                Log.e(ApiManager.class.getName(), e.getMessage());
            }
        } else if (request.url().toString().contains("login")) {
            //if try to login and user doesn't exist
            return false;
        }
        //go to login if no access token or if refresh token failed
        Intent i = new Intent(context, LoginFlowActivity.class);
        context.startActivity(i);
        return false;
    }
}
