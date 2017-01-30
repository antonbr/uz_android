package com.uzapp.view;

import android.content.Context;

/**
 * Created by Viktoria Chebotar on 12/26/16.
 */

public interface BaseView {
    Context getContext();
    void showProgress(boolean isLoading);
    void showError(String error);
}
