package com.uzapp.view;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;

/**
 * Created by vika on 14.08.16.
 */
public class UzApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(getApplicationContext());
        VKSdk.initialize(this);
    }

}
