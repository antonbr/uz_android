package com.uzapp.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.uzapp.R;
import com.uzapp.pojo.UserTokenResponse;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.PrefsUtil;

import java.io.IOException;
import java.net.HttpURLConnection;

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

    private ApiManager() {
    }

    public static ApiInterface getApi(Context context) {
        if (api == null) {
            OkHttpClient client = new OkHttpClient.Builder().
                    addInterceptor(getLoggingInterceptor()).
                    addInterceptor(getHeaderInterceptor(context)).
                    addInterceptor(getErrorInterceptor(context)).
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
                return chain.proceed(getRequestWithHeaders(context, chain.request()));
            }
        };
    }

    private static Request getRequestWithHeaders(Context context, Request request) {
        Request.Builder requestBuilder = request.newBuilder();
        String accessToken = PrefsUtil.getStringPreference(context, PrefsUtil.USER_ACCESS_TOKEN);
        requestBuilder.header("Accept-Language", CommonUtils.getLanguage());
        if (!TextUtils.isEmpty(accessToken)) {
            requestBuilder.header("Authorization", context.getString(R.string.token, accessToken));
        }
        return requestBuilder.build();
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
                        if (refreshTokenIfNeeded(context)) {
                            request = getRequestWithHeaders(context, request);
                            response = chain.proceed(request);
                        }
                        break;
                }
                return response;
            }
        };
    }

    //return true if token was refreshed
    private static boolean refreshTokenIfNeeded(Context context) {
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
        }
        //todo go to login if no access token or if refresh token failed
        return false;
    }
}
