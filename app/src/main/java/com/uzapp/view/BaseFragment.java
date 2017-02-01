package com.uzapp.view;

import android.support.v4.app.Fragment;

import com.uzapp.util.CommonUtils;

/**
 * Created by viktoria on 2/1/17.
 */

public class BaseFragment extends Fragment implements BaseView {
    @Override
    public void showProgress(boolean isLoading) {

    }

    @Override
    public void showError(String error) {
        CommonUtils.showSnackbar(getView(), error);
    }
}
