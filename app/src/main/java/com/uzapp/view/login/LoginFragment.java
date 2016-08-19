package com.uzapp.view.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.LoginInfo;
import com.uzapp.pojo.UserTokenResponse;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.main.MainActivity;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vika on 14.08.16.
 */
public class LoginFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.resetBtn) Button resetBtn;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.emailField) TextInputEditText emailField;
    @BindView(R.id.emailLayout) TextInputLayout emailLayout;
    @BindView(R.id.passwordField) TextInputEditText passwordField;
    @BindView(R.id.passwordLayout) TextInputLayout passwordLayout;
    @BindView(R.id.loginBtn) Button loginBtn;
    @BindDimen(R.dimen.hint_padding) int hintPadding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        resetBtn.setVisibility(View.GONE);
        toolbarTitle.setText(R.string.login_title);
        emailLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        passwordLayout.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        return view;
    }

    @OnFocusChange({R.id.emailField, R.id.passwordField})
    void onFieldFocusChanged(boolean focus, TextInputEditText view) {
        if (focus || view.getText().length() > 0) {
            view.setTranslationY(hintPadding);
        } else {
            view.setTranslationY(0);
        }
    }

    @OnClick(R.id.forgotPasswordBtn)
    void onForgotPasswordClicked() {
        ((BaseActivity) getActivity()).replaceFragment(new ResetPasswordFragment(), true);
    }

    @OnTextChanged({R.id.emailField, R.id.passwordField})
    void onFieldsChanged(Editable editable) {
        checkFieldState();
    }

    @OnClick(R.id.loginBtn)
    void onLoginBtnClicked() {
        String deviceId = CommonUtils.getDeviceId(getContext());
        LoginInfo loginInfo = new LoginInfo(deviceId, emailField.getText().toString(),
                passwordField.getText().toString());
        Call call = ApiManager.getApi(getContext()).login(loginInfo);
        call.enqueue(callback);
    }

    @OnClick(R.id.registrationBtn)
    void onRegistrationBtnClicked() {
        ((BaseActivity) getActivity()).replaceFragment(new CreateAccountFragment(), true);
    }

    private void checkFieldState() {
        boolean allowLogin = CommonUtils.isEmailValid(emailField.getText().toString()) &&
                CommonUtils.isPasswordValid(passwordField.getText().toString());
        loginBtn.setEnabled(allowLogin);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Callback<UserTokenResponse> callback = new Callback<UserTokenResponse>() {

        @Override
        public void onResponse(Call<UserTokenResponse> call, Response<UserTokenResponse> response) {
            if (getView() != null) {
                if (response.isSuccessful()) {
                    UserTokenResponse user = response.body();
                    PrefsUtil.saveUserInfo(getContext(), user.getUserId(), user.getAccessToken(), user.getRefreshToken());
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    //bring back main activity from stack and start profile fragment
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("profile",true);
                    startActivity(intent);
                } else {
                    CommonUtils.showMessage(getView(), response.message());
                }
            }
        }

        @Override
        public void onFailure(Call<UserTokenResponse> call, Throwable t) {
            if (getView() != null && t != null) {
                CommonUtils.showMessage(getView(), t.getMessage());
            }
        }
    };
}