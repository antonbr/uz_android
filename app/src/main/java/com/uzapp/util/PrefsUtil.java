package com.uzapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * Created by vika on 14.08.16.
 */
public class PrefsUtil {
    public static final String USER_ACCESS_TOKEN = "user_access_token";
    public static final String USER_REFRESH_TOKEN = "user_refresh_token";
    public static final String USER_ID = "user_id";


    public static boolean setStringPreference(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null && !TextUtils.isEmpty(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    public static String getStringPreference(Context context, String key) {
        String value = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences != null) {
            value = preferences.getString(key, null);
        }
        return value;
    }

    public static void clearAllPrefs(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }

    public static void saveUserInfo(Context context, String userId, String accessToken, String refreshToken) {
        PrefsUtil.setStringPreference(context, PrefsUtil.USER_ACCESS_TOKEN, accessToken);
        PrefsUtil.setStringPreference(context, PrefsUtil.USER_ID, userId);
        PrefsUtil.setStringPreference(context, PrefsUtil.USER_REFRESH_TOKEN, refreshToken);
    }
}
