package com.uzapp.view.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.uzapp.R;
import com.uzapp.network.ApiManager;
import com.uzapp.pojo.LoginInfo;
import com.uzapp.pojo.UserTokenResponse;
import com.uzapp.util.ApiErrorUtil;
import com.uzapp.util.CommonUtils;
import com.uzapp.util.PrefsUtil;
import com.uzapp.view.BaseActivity;
import com.uzapp.view.main.MainActivity;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.Arrays;
import java.util.List;

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
    List<String> permissionNeeds = Arrays.asList("email");
    private Unbinder unbinder;
    @BindView(R.id.resetBtn) Button resetBtn;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.emailField) TextInputEditText emailField;
    @BindView(R.id.emailLayout) TextInputLayout emailLayout;
    @BindView(R.id.passwordField) TextInputEditText passwordField;
    @BindView(R.id.passwordLayout) TextInputLayout passwordLayout;
    @BindView(R.id.loginBtn) Button loginBtn;
    @BindDimen(R.dimen.hint_padding) int hintPadding;
    private CallbackManager callbackManager;//used for facebook login


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

    @OnClick(R.id.fbBtn)
    void onFbBtnClicked() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Toast.makeText(getContext(), accessToken.getToken(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException exception) {
                LoginManager.getInstance().logOut();
                Log.e(LoginFragment.class.getName(), "facebook exception: " + exception.toString());
            }
        });
        LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);
    }

    @OnClick(R.id.vkBtn)
    void onVkBtnClicked() {
        VKSdk.login(getActivity(), VKScope.EMAIL);
    }

    @OnClick(R.id.okBtn)
    void onOkBtnClicked() {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!handleVkResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean handleVkResult(int requestCode, int resultCode, Intent data) {
        return VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getContext(), res.accessToken, Toast.LENGTH_SHORT).show();
//                setVkToken(res.accessToken);
//                String email = res.email;
//                isVKontakte = true;
//                isFacebook = false;
//                handleSocialSignInResult(res.accessToken, email, TAG_SOCIAL_VKONTAKTE);
            }

            @Override
            public void onError(VKError error) {
                Log.e(LoginFragment.class.getName(), "vk exception: " + error.toString());
            }
        });
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
                    intent.putExtra("profile", true);
                    startActivity(intent);
                } else {
                    String error = ApiErrorUtil.parseError(response);
                    CommonUtils.showMessage(emailField, error);
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
